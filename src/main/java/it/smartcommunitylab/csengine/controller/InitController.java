package it.smartcommunitylab.csengine.controller;

import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;

@RestController
public class InitController {
	@Autowired
	PersonRepository personRepository;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@GetMapping("/init")
	public void init() {
		int count = 0;
			Person[] data = new Person[1000];
		for(int i = 0; i < 1000; i++) {
			Person person = new Person();
			person.setFiscalCode(RandomStringUtils.randomAlphanumeric(16));
			person.setName("name" + count);
			person.setSurname("surname" + count);
			data[i] = person;
			count++;
		}
		personRepository.deleteAll()
		.thenMany(Flux.just(data))
		.flatMap(personRepository::save)
		.subscribe();
	}
}
