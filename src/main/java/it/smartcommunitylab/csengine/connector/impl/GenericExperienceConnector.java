package it.smartcommunitylab.csengine.connector.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GenericExperienceConnector implements ExperienceConnector {

	@Override
	public Flux<Experience> refreshExp(Person person, String entityType, String viewName, String uri) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("?fiscalCode=" + person.getFiscalCode() + "&entityType=" + entityType + "&viewName=" + viewName)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(Experience.class).flatMapSequential(e -> this.getExperience(person, entityType, e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getExperience(Person person, String entityType, Experience e) {
		e.setPersonId(person.getId());
		e.setPersonal(false);
		e.setEntityType(entityType);
		return Mono.just(e);
	}

}
