package it.smartcommunitylab.csengine.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Flux;

public interface PersonRepository extends ReactiveMongoRepository<Person, String> {
  Flux<Person> findAllByFiscalCodeRegex(String text);
  Flux<Person> findByFiscalCode(String code);
}
