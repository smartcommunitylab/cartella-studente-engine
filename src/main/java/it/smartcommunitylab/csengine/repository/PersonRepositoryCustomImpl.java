package it.smartcommunitylab.csengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {
	@Autowired
	private ReactiveMongoTemplate template;

	@Override
	public Mono<Person> updateView(String personId, String view, DataView dw) {
		return template.update(Person.class)
			.matching(Criteria.where("id").is(personId))
			.apply(new Update().set("views." + view, dw))
			.findAndModify();
	}

	@Override
	public Mono<Person> updateFields(String personId, String name, String surname, String fiscalCode) {
		return template.update(Person.class)
				.matching(Criteria.where("id").is(personId))
				.apply(new Update().set("name", name).set("surname", surname).set("fiscalCode", fiscalCode))
				.findAndModify();
	}

}
