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
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Organisation;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.OrganisationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component(value="saaExam")
public class SAAExamService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	@Autowired
	OrganisationRepository organisationRepository;
	
	@Autowired
	SAAInstituteService instituteService;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
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
		.switchIfEmpty(this.addNewExperience(personId, EntityType.EXAM))
		.flatMap(exp -> experienceRepository.updateView(exp.getId(), View.SAA.label, getDataView(e)));
	}
	
	private Mono<Experience> addNewExperience(String personId, EntityType type) {
		Experience exp = new Experience();
		exp.setEntityType(type.label);
		exp.setPersonId(personId);
		return experienceRepository.save(exp);
	}
	
	private Mono<Organisation> addNewOrganisation(String extId, String origin) {
		ExtRef identity = new ExtRef(extId, origin);
		DataView dw = new DataView();
		dw.setIdentity(identity);
		Organisation o = new Organisation();
		o.setViews(new HashMap<>());
		o.getViews().put(View.SAA.label, dw);
		Mono<Organisation> mono = organisationRepository.save(o);
		return mono.flatMap(instituteService::refreshOrganisation)
				.flatMap(instituteService::fillOrganisationFields);
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
		view.getAttributes().put("instituteRef", e.getInstituteRef());
		return view;
	}

	@Override
	public Mono<Experience> fillExpFields(Experience e) {
		DataView view = e.getViews().get(View.SAA.label);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(ExamAttr.TYPE.label, view.getAttributes().get("type"));
		attributes.put(ExamAttr.QUALIFICATION.label, view.getAttributes().get("qualification"));
		attributes.put(ExamAttr.RESULT.label, Boolean.TRUE);
		String instituteRef = (String) view.getAttributes().get("instituteRef");
		return organisationRepository.findByExtRef(View.SAA.label, instituteRef, view.getIdentity().getOrigin())
			.switchIfEmpty(this.addNewOrganisation(instituteRef, view.getIdentity().getOrigin()))
			.flatMap(o -> {
				return experienceRepository.updateFields(e.getId(), 
						(String) view.getAttributes().get("title"), "", 
						LocalDate.parse((String) view.getAttributes().get("dateFrom"), dtf), 
						LocalDate.parse((String) view.getAttributes().get("dateTo"), dtf),
						o.getId(), attributes); 				
			});
	}

}
