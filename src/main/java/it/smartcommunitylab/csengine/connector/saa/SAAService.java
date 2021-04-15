package it.smartcommunitylab.csengine.connector.saa;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SAAService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	public Mono<Person> refreshPerson(String personId, String fiscalCode) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/student?fiscalCode=" + fiscalCode).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAStudent.class).flatMap(s -> {
							return personRepository.updateView(personId, View.SAA.label, getDataView(s));
						});
					}
					return Mono.empty();
				});		
	}
	
	public Flux<Experience> refreshExam(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/exam?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAExam.class).flatMap(e -> this.updateExam(person.getId(), e));
					}
					return Flux.empty();
				});		
	}
	
	private Mono<Experience> updateExam(String personId, SAAExam e) {
		return experienceRepository.findByExtRef(View.SAA.label, e.getExtId(), e.getOrigin())
		.switchIfEmpty(this.addNewExperience(personId, e))
		.flatMap(exp -> experienceRepository.updateView(exp.getId(), View.SAA.label, getDataView(e)));
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
	
	private DataView getDataView(SAAExam e) {
		ExtRef identity = new ExtRef(e.getExtId(), e.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("dateFrom", e.getDateFrom());
		view.getAttributes().put("dateTo", e.getDateTo());
		view.getAttributes().put("qualification", e.getQualification());
		view.getAttributes().put("schoolYear", e.getSchoolYear());
		view.getAttributes().put("type", e.getType());
		return view;
	}
	
	public Mono<Person> fillPersonFields(Person p) {
		DataView view = p.getViews().get(View.SAA.label);
		return personRepository.updateFields(p.getId(), 
				(String) view.getAttributes().get("name"), 
				(String) view.getAttributes().get("surname"), 
				(String) view.getAttributes().get("cf"));
	}
	
	public Mono<Experience> fillExamFields(Experience e) {
		DataView view = e.getViews().get(View.SAA.label);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(ExamAttr.TYPE.label, view.getAttributes().get("type"));
		attributes.put(ExamAttr.QUALIFICATION.label, view.getAttributes().get("qualification"));
		attributes.put(ExamAttr.RESULT.label, Boolean.TRUE);		
		return experienceRepository.updateFields(e.getId(), 
				(String) view.getAttributes().get("type"), "", 
				LocalDate.parse((String) view.getAttributes().get("dateFrom"), dtf), 
				LocalDate.parse((String) view.getAttributes().get("dateFrom"), dtf), 
				attributes);
	}
	
	private Mono<Experience> addNewExperience(String personId, SAAExam e) {
		Experience exp = new Experience();
		exp.setEntityType(EntityType.EXAM.label);
		exp.setPersonId(personId);
		return experienceRepository.save(exp);
	}


}
