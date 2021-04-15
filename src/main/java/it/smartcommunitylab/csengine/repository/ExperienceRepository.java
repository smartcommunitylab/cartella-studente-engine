package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExperienceRepository extends ReactiveMongoRepository<Experience, String>, ExperienceRepositoryCustom {
	Flux<Experience> findAllByPersonIdAndEntityType(String personId, String entityType);
	
	@Query(value="{'views.?0.identity.extUri':?1, 'views.?0.identity.origin':?2}")
	Mono<Experience> findByExtRef(String view, String extUri, String origin);
}
