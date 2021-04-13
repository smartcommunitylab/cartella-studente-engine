package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Organisation;

public interface OrganisationRepository extends ReactiveMongoRepository<Organisation, String> {
}
