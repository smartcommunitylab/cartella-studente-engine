package it.smartcommunitylab.csengine.connector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
public class PersonService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ConnectorManager connectorManager;

	public Mono<Person> refreshPerson(String fiscalCode) {
		// TODO add logic to manage connectors choice
		return personRepository.findByFiscalCode(fiscalCode)
				.switchIfEmpty(this.addNewPerson(fiscalCode))
				.flatMap(this::mergeView);
	}
	
	private Mono<Person> mergeView(Person person) {
		// TODO add logic to manage connectors choice
		ConnectorConf conf = connectorManager.getExpConnector(EntityType.person.label, 1);
		PersonConnector connector = connectorManager.getPersonService(conf.getView());
		return connector.refreshPerson(person).flatMap(p -> {
				//TODO add logic to manage views
				DataView view = p.getViews().get(conf.getView());
				if(view != null) {
					return personRepository.findByFiscalCode(p.getFiscalCode())
							.flatMap(db -> this.updatePerson(db, p, conf.getView()));
				}
				return Mono.empty();
			});
	}
	
	private Mono<Person> updatePerson(Person db, Person p, String view) {
		if(db.getId().equals(p.getId())) {
			return Mono.just(db);
		}
		return personRepository.updateView(db.getId(), EntityType.person.label, p.getViews().get(EntityType.person.label))
		.then(personRepository.updateView(db.getId(), view, p.getViews().get(view)));
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return personRepository.save(p);
	}
}
