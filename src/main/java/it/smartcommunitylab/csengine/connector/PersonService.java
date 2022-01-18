package it.smartcommunitylab.csengine.connector;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class PersonService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ConnectorManager connectorManager;

	public Mono<Person> refreshPerson(String fiscalCode) {
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMap(this::mergeView);
	}
	
	private Mono<Person> mergeView(Person person) {
		List<ConnectorConf> list = connectorManager.getExpConnectorsReverse(EntityType.person.label);
		return Flux.fromIterable(list)
				.concatMap(conf -> {
					PersonConnector connector = connectorManager.getPersonService(conf.getView());
					return connector.refreshPerson(person, conf.getView(), conf.getUri()).flatMap(p -> {
							DataView view = p.getViews().get(conf.getView());
							if(view != null) {
								return personRepository.findByFiscalCode(p.getFiscalCode())
										.flatMap(db -> this.updatePerson(db, p, conf.getView()));
							}
							return Mono.empty();
						});					
				}).then(personRepository.findByFiscalCode(person.getFiscalCode()));
	}
	
	private Mono<Person> updatePerson(Person db, Person p, String view) {
		if(db.getId().equals(p.getId())) {
			return Mono.just(db);
		}
		if(db.getViews().get(EntityType.person.label) != null) {
			db.getViews().get(EntityType.person.label).getAttributes()
			.putAll(p.getViews().get(EntityType.person.label).getAttributes());
		} else {
			db.getViews().put(EntityType.person.label, p.getViews().get(EntityType.person.label));
		}
		db.getViews().put(view, p.getViews().get(view));
		return personRepository.save(db);
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return personRepository.save(p);
	}
}
