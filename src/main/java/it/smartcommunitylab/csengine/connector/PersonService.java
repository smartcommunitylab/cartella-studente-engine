package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
public class PersonService {
	@Autowired
	PersonRepository personRepository;
	
	public Mono<Person> refreshPerson(String fiscalCode) {
		// TODO add logic to manage connectors choice
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/person").accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						Mono<Person> p = response.bodyToMono(Person.class);
						
						return p;
					}
					return Mono.empty();
				});
	}
}
