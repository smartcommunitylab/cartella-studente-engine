package it.smartcommunitylab.csengine.connector.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.connector.PersonConnector;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

@Component
public class GenericPersonConnector implements PersonConnector {

	@Override
	public Mono<Person> refreshPerson(Person person, String viewName, String uri) {
		WebClient client = WebClient.create();
		return client.get().uri(uri + "?fiscalCode=" + person.getFiscalCode() + "&viewName=" + viewName)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(Person.class);
					}
					return Mono.empty();
				});		
	}
}
