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
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
public class UserDataManager {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;

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
			String entityType, Map<String, Object> attributes) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not editable"));
						}
						return updateViews((Experience) exp, entityType, attributes);
					});			
		});
	}
	
	private Mono<Experience> updateViews(Experience exp, String entityType, 
			Map<String, Object> attributes) {
		DataView expView = getExpDataView(attributes);
		if(!exp.getViews().containsKey(EntityType.exp.label)) {
			exp.getViews().put(EntityType.exp.label, expView);
		} else {
			exp.getViews().get(EntityType.exp.label).getAttributes().putAll(expView.getAttributes());
		}
		if(!exp.getViews().containsKey(entityType)) {
			DataView entityView = new DataView();
			entityView.getAttributes().putAll(attributes);			
			exp.getViews().put(entityType, entityView);
		} else {
			exp.getViews().get(entityType).getAttributes().putAll(attributes);
		}		
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
