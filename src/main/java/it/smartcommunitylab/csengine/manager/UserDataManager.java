package it.smartcommunitylab.csengine.manager;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.exception.BadRequestException;
import it.smartcommunitylab.csengine.exception.NotFoundException;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserDataManager {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;

	public Mono<Experience> getExperience(String fiscalCode, String expId) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")));
		});		
	}
	
	public Flux<Experience> getExperienceByFiscalCode(String fiscalCode, String entityType) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(this.addNewPerson(fiscalCode))
		.flatMapMany(person -> {
			if(person.getId() != null) {
				return experienceRepository.findAllByPersonIdAndEntityType(person.getId(), entityType);
			} else {
				return Flux.empty();
			}
		});
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return Mono.just(p);
	}

	public Mono<Experience> deleteExperience(String fiscalCode, String expId) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not erasable"));
						}						
						return experienceRepository.deleteByExpId(expId);
					});
		});
	}

	public Mono<Experience> addExperience(String fiscalCode, String entityType, 
			Map<String, Object> attributes) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			Experience exp = new Experience();
			exp.setPersonId(person.getId());
			exp.setEntityType(entityType);
			exp.setPersonal(true);
			DataView expView = getExpDataView(attributes);
			exp.getViews().put(EntityType.exp.label, expView);
			DataView entityView = new DataView();
			entityView.getAttributes().putAll(attributes);
			exp.getViews().put(entityType, entityView);
			return experienceRepository.save(exp);
		});		
	}
	
	public Mono<Experience> updateExperience(String fiscalCode, String expId,
			Map<String, DataView> views) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not editable"));
						}
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateViews((Experience) exp, views);
					});			
		});
	}
	
	public Mono<Experience> updateExperienceView(String fiscalCode, String expId,
			String viewName, DataView view) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not editable"));
						}
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateView((Experience) exp, viewName, view);
					});			
		});
	}
	
	public Mono<Experience> updateUserExperienceView(String fiscalCode, String expId,
			String viewName, DataView view) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateView((Experience) exp, viewName + "User", view);
					});			
		});
	}
	
	public Mono<Experience> updateExperienceAnnotation(String fiscalCode, String expId, Map<String, Object> annotation) {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateAnnotation((Experience) exp, annotation);
					});			
		});
	}
	
	private Mono<Experience> updateAnnotation(Experience exp, Map<String, Object> annotation) {
		exp.getViews().get(EntityType.exp.label).getAttributes().put(ExpAttr.annotation.label, annotation);
		return experienceRepository.save(exp);
	}	
	
	private Mono<Experience> updateView(Experience exp, String viewName, DataView view) {
		exp.getViews().put(viewName, view);
		return experienceRepository.save(exp);
	}

	private Mono<Experience> updateViews(Experience exp, Map<String, DataView> views) {
		exp.setViews(views);
		return experienceRepository.save(exp);
	}

	/**
	 * Extract from attributes the main level Experience Data View
	 */
	private DataView getExpDataView(Map<String, Object> attributes) {
		DataView view = new DataView();
		for(ExpAttr attr : ExpAttr.values()) {
			if(attributes.containsKey(attr.label)) {
				view.getAttributes().put(attr.label, attributes.get(attr.label));
				attributes.remove(attr.label);
			}			
		}
		return view;
	}

	
	
	
}
