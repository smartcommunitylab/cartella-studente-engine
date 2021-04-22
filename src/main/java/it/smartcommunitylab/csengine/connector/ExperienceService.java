package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ExperienceService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	
	
	@Autowired
	@Qualifier("saaStage")
	ExperienceConnector saaStageConnector;
	
	@Autowired
	@Qualifier("saaExam")	
	ExperienceConnector saaExamConnector;
	
	public Flux<Experience> refreshExam(String fiscalCode) {
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMapMany(p -> this.mergeExamView(p));
	}
	
	private Flux<Experience> mergeExamView(Person person) {
		// TODO add logic to manage connectors choice
		return saaExamConnector.refreshExp(person).flatMapSequential(e -> {
			//TODO add logic to manage views
			DataView view = e.getViews().get(View.SAA.label);
			if(view != null) {
				return experienceRepository.findByExtRef(View.SAA.label, 
						view.getIdentity().getExtUri(), view.getIdentity().getOrigin())
						.switchIfEmpty(experienceRepository.save(e))
						.flatMap(db -> this.updateExam(db, e));
			}
			return Mono.empty();
		});
	}
	
	public Flux<Experience> refreshStage(String fiscalCode) {
		// TODO add logic to manage connectors choice
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMapMany(p -> this.mergeStageView(p));
	}
	
	private Mono<Experience> updateExam(Experience db, Experience e) {
		if(db.getId().equals(e.getId())) {
			return Mono.just(db);
		}
		return experienceRepository.updateView(db.getId(), EntityType.exp.label, e.getViews().get(EntityType.exp.label))
		.then(experienceRepository.updateView(db.getId(), EntityType.exam.label, e.getViews().get(EntityType.exam.label)))
		.then(experienceRepository.updateView(db.getId(), View.SAA.label, e.getViews().get(View.SAA.label)));
	}
	
	
	private Flux<Experience> mergeStageView(Person person) {
		return saaStageConnector.refreshExp(person).flatMap(e -> {
			//TODO add logic to manage views
			return saaStageConnector.fillExpFields(e);			
		});
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return personRepository.save(p);
	}
}
