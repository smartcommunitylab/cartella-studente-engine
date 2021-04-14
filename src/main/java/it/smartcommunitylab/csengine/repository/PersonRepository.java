package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository extends ReactiveMongoRepository<Person, String>, PersonRepositoryCustom {
  Flux<Person> findAllByFiscalCodeRegex(String text);
  Flux<Person> findByFiscalCode(String code);
  
  @Query(value="{'views.?0.identity.extUri':?1, 'views.?0.identity.origin':?2}")
  Mono<Person> findByExtRef(String view, String extUri, String origin);
}
