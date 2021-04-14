package it.smartcommunitylab.csengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.connector.PersonService;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

@RestController
public class PersonController {
	@Autowired
	PersonService personService;

	@GetMapping("/person/refresh")
	public Mono<Person> refreshPerson() {
		return personService.refreshPerson("111111");
	}
	
	public void log(Person p) {
		System.out.println(p.getFiscalCode());
	}
}
