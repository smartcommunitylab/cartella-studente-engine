package it.smartcommunitylab.csengine.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Competence;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompetenceRepository extends ReactiveMongoRepository<Competence, String> {
	Mono<Competence> findByUri(String uri);
	Flux<Competence> findByUriIn(List<String> list);
}
