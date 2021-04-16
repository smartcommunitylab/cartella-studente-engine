package it.smartcommunitylab.csengine.connector;

import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExperienceConnector {
	public Flux<Experience> refreshExp(Person person);
	public Mono<Experience> fillExpFields(Experience e);
}
