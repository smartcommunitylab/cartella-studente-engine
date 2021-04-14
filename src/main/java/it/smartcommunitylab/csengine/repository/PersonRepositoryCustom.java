package it.smartcommunitylab.csengine.repository;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

public interface PersonRepositoryCustom {
	public Mono<Person> updateView(String personId, String view, DataView dw); 
}
