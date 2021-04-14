package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.ExpCompetence;
import reactor.core.publisher.Flux;

public interface ExpCompetenceRepository extends ReactiveMongoRepository<ExpCompetence, String> {
	Flux<ExpCompetence> findByPersonId(String personId);
	Flux<ExpCompetence> findByExperienceIdAndPersonId(String experienceId, String personId);
}
