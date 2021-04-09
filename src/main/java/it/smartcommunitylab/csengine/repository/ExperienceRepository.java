package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Flux;

public interface ExperienceRepository extends ReactiveMongoRepository<Experience, String> {
	Flux<Experience> findAllByPersonIdAndEntityType(String personId, String entityType);
}
