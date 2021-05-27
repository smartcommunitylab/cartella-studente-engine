package it.smartcommunitylab.csengine.connector;

import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;

public interface ExperienceConnector {
	public Flux<Experience> refreshExp(Person person);
	public void setView(String view);
	public void setUri(String uri);
}
