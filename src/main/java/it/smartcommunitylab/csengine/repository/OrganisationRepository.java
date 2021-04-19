package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Organisation;
import reactor.core.publisher.Mono;

public interface OrganisationRepository extends ReactiveMongoRepository<Organisation, String>, OrganisationRepositoryCustom {
	@Query(value="{'views.?0.identity.extUri':?1, 'views.?0.identity.origin':?2}")
	Mono<Organisation> findByExtRef(String view, String extUri, String origin);
}
