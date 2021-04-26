package it.smartcommunitylab.csengine.connector;

import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

public interface PersonConnector {
	public Mono<Person> refreshPerson(Person person);
	public void setView(String view);
}
