package it.smartcommunitylab.csengine.repository;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Mono;

public class ExperienceRepositoryCustomImpl implements ExperienceRepositoryCustom {
	@Autowired
	private ReactiveMongoTemplate template;

	@Override
	public Mono<Experience> updateView(String expId, String view, DataView dw) {
		Query query = new Query(Criteria.where("id").is(expId));
		Update update = new Update().set("views." + view, dw);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);
		return template.findAndModify(query, update, options, Experience.class);
	}

	@Override
	public Mono<Experience> updateFields(String expId, String title, String description, 
			LocalDate dateFrom, LocalDate dateTo, String organisationId, Map<String, Object> attributes) {
		Query query = new Query(Criteria.where("id").is(expId));
		Update update = new Update().set("title", title).set("description", description)
				.set("dateFrom", dateFrom).set("dateTo", dateTo).set("organisationId", organisationId)
				.set("attributes", attributes);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);
		options.upsert(false);		
		return template.findAndModify(query, update, options, Experience.class);
	}

	@Override
	public Mono<Experience> findByAttr(String path, Object value) {
		Query query = new Query(Criteria.where(path).is(value));
		return template.findOne(query, Experience.class);
	}
	
	public Mono<Experience> deleteByExpId(String expId) {
		Query query = new Query(Criteria.where("id").is(expId));
		return template.findAndRemove(query, Experience.class);
	}


}
