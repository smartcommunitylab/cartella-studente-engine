package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ExperienceService {
	@Autowired
	PersonRepository personRepository;
	
	@Autowired
	@Qualifier("saaStage")
	ExperienceConnector saaStageConnector;
	
	@Autowired
	@Qualifier("saaExam")	
	ExperienceConnector saaExamConnector;
	
	public Flux<Experience> refreshExam(String fiscalCode) {
		// TODO add logic to manage connectors choice
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMapMany(p -> this.mergeExamView(p));
	}
	
	public Flux<Experience> refreshStage(String fiscalCode) {
		// TODO add logic to manage connectors choice
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMapMany(p -> this.mergeStageView(p));
	}
	
	private Flux<Experience> mergeExamView(Person person) {
		return saaExamConnector.refreshExp(person).flatMap(e -> {
			//TODO add logic to manage views
			return saaExamConnector.fillExpFields(e);			
		});
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
