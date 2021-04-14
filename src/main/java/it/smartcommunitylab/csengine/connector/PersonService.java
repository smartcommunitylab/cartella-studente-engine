package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.connector.saa.SAAService;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
public class PersonService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	SAAService saaService;
	
	public Mono<Person> refreshPerson(String fiscalCode) {
		// TODO add logic to manage connectors choice
		return personRepository.findByFiscalCode(fiscalCode).switchIfEmpty(this.addNewPerson(fiscalCode)).flatMap(this::mergeView);
	}
	
	private Mono<Person> mergeView(Person person) {
		return saaService.refreshPerson(person).flatMap(p -> {
			//TODO add logic to manage views
			return saaService.fillPersonFields(p);			
		});
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return personRepository.save(p);
	}
}
