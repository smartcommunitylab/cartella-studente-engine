package it.smartcommunitylab.csengine.connector.saa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Mono;

@Component
public class SAAService {
	@Autowired
	PersonRepository personRepository;
	
	public Mono<Person> refreshPerson(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/student?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAStudent.class).flatMap(s -> {
							return personRepository.updateView(person.getId(), View.SAA.label, getDataView(s));
						});
					}
					return Mono.empty();
				});		
	}
	
	private DataView getDataView(SAAStudent s) {
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
		return view;
	}
	
	public Mono<Person> fillPersonFields(Person p) {
		DataView view = p.getViews().get(View.SAA.label);
		return personRepository.updateFields(p.getId(), 
				(String) view.getAttributes().get("name"), 
				(String) view.getAttributes().get("surname"), 
				(String) view.getAttributes().get("cf"));
	}

}
