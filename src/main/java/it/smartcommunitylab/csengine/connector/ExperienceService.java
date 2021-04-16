package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.connector.saa.SAAService;
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
	SAAService saaService;
	
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
		return saaService.refreshExam(person).flatMap(e -> {
			//TODO add logic to manage views
			return saaService.fillExamFields(e);			
		});
	}
	
	private Flux<Experience> mergeStageView(Person person) {
		return saaService.refreshStage(person).flatMap(e -> {
			//TODO add logic to manage views
			return saaService.fillStageFields(e);			
		});
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return personRepository.save(p);
	}
}
