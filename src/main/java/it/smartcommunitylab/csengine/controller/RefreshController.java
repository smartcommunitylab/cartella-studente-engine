package it.smartcommunitylab.csengine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.connector.ExperienceService;
import it.smartcommunitylab.csengine.connector.PersonService;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class RefreshController {
	@Autowired
	PersonService personService;
	@Autowired
	ExperienceService experienceService;

	@GetMapping("/person/refresh")
	public Mono<Person> refreshPerson(@RequestParam String fiscalCode) {
		return personService.refreshPerson(fiscalCode);
	}
	
	@GetMapping("/exam/refresh")
	public Flux<Experience> refreshExam(@RequestParam String fiscalCode) {
		return experienceService.refreshExam(fiscalCode);
	}

}
