package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.PersonAttr;
import it.smartcommunitylab.csengine.connector.PersonConnector;
import it.smartcommunitylab.csengine.model.Address;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component("saaPerson")
public class SAAPersonService implements PersonConnector {
	@Autowired
	PersonRepository personRepository;
	
	String viewName;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Mono<Person> refreshPerson(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/student?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAStudent.class).flatMap(s -> {
							return updateView(s);
						});
					}
					return Mono.empty();
				});		
	}

	private Mono<Person> updateView(SAAStudent s) {
		Person p = new Person();
		
		ExtRef identity = new ExtRef(s.getExtId(), s.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("name", s.getName());
		view.getAttributes().put("surname", s.getSurname());
		view.getAttributes().put("address", s.getAddress());
		view.getAttributes().put("birthdate", s.getBirthdate());
		view.getAttributes().put("cf", s.getCf());
		view.getAttributes().put("email", s.getEmail());
		view.getAttributes().put("phone", s.getPhone());
		view.getAttributes().put("mobilePhone", s.getMobilePhone());
		
		p.getViews().put(viewName, view);
		p.getViews().put(EntityType.person.label, getDataView(s));
		p.setFiscalCode(s.getCf());
		return Mono.just(p);
	}
	
	private DataView getDataView(SAAStudent s) {
		DataView view = new DataView();
		view.getAttributes().put(PersonAttr.name.label, s.getName());
		view.getAttributes().put(PersonAttr.surname.label, s.getSurname());
		Address address = new Address();
		address.setExtendedAddress(s.getAddress());
		view.getAttributes().put(PersonAttr.address.label, address);
		view.getAttributes().put(PersonAttr.birthdate.label, s.getBirthdate());
		view.getAttributes().put(PersonAttr.fiscalCode.label, s.getCf());
		view.getAttributes().put(PersonAttr.email.label, s.getEmail());
		view.getAttributes().put(PersonAttr.phone.label, s.getPhone() + " / " + s.getMobilePhone());
		return view;
	}
	
	@Override
	public void setView(String view) {
		this.viewName = view;
	}
	
}
