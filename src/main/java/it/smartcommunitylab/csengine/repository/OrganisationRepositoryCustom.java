package it.smartcommunitylab.csengine.repository;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Organisation;
import reactor.core.publisher.Mono;

public interface OrganisationRepositoryCustom {
	public Mono<Organisation> updateView(String organisationId, String view, DataView dw); 
	public Mono<Organisation> updateFields(String organisationId, String name, String fiscalCode, String email);
}
