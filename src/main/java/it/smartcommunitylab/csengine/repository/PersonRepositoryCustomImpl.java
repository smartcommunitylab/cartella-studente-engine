package it.smartcommunitylab.csengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Person;
import reactor.core.publisher.Mono;

public class PersonRepositoryCustomImpl implements PersonRepositoryCustom {
	@Autowired
	private ReactiveMongoTemplate template;

	@Override
	public Mono<Person> updateView(String personId, String view, DataView dw) {
		Query query = new Query(Criteria.where("id").is(personId));
		Update update = new Update().set("views." + view, dw);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);
		return template.findAndModify(query, update, options, Person.class);
	}

	@Override
	public Mono<Person> updateFields(String personId, String name, String surname, String fiscalCode) {
		Query query = new Query(Criteria.where("id").is(personId));
		Update update = new Update().set("name", name).set("surname", surname).set("fiscalCode", fiscalCode);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);
		return template.findAndModify(query, update, options, Person.class);
	}

}
