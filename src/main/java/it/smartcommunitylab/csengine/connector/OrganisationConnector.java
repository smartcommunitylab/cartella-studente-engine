package it.smartcommunitylab.csengine.connector;

import it.smartcommunitylab.csengine.model.Organisation;
import reactor.core.publisher.Mono;

public interface OrganisationConnector {
	public Mono<Organisation> refreshOrganisation(Organisation o);
	public Mono<Organisation> fillOrganisationFields(Organisation o);
}
