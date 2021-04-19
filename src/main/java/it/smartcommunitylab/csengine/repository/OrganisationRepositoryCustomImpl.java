package it.smartcommunitylab.csengine.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Organisation;
import reactor.core.publisher.Mono;

public class OrganisationRepositoryCustomImpl implements OrganisationRepositoryCustom {
	@Autowired
	private ReactiveMongoTemplate template;

	@Override
	public Mono<Organisation> updateView(String organisationId, String view, DataView dw) {
		Query query = new Query(Criteria.where("id").is(organisationId));
		Update update = new Update().set("views." + view, dw);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);
		return template.findAndModify(query, update, options, Organisation.class);
	}

	@Override
	public Mono<Organisation> updateFields(String organisationId, String name, String fiscalCode, String email) {
		Query query = new Query(Criteria.where("id").is(organisationId));
		Update update = new Update().set("name", name).set("fiscalCode", fiscalCode).set("email", email);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);
		return template.findAndModify(query, update, options, Organisation.class);
	}

}
