package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component(value="saaExam")
public class SAAExamService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	
	@Autowired
	SAAInstituteService instituteService;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/exam?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAExam.class).flatMap(e -> this.getExam(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getExam(String personId, SAAExam e) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.exam.label);
		exp.getViews().put(EntityType.exp.label, getExpDataView(e));
		exp.getViews().put(EntityType.exam.label, getExamDataView(e));
		exp.getViews().put(View.SAA.label, getDataView(e));
		return Mono.just(exp);
//		return experienceRepository.findByExtRef(View.SAA.label, e.getExtId(), e.getOrigin())
//		.switchIfEmpty(this.addNewExperience(personId, EntityType.exam))
//		.flatMap(exp -> experienceRepository.updateView(exp.getId(), View.SAA.label, getDataView(e)));
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
	
	private DataView getExamDataView(SAAExam e) {
		DataView view = new DataView();
		view.getAttributes().put(ExamAttr.qualification.label, e.getQualification());
		view.getAttributes().put(ExamAttr.type.label, e.getType());
		return view;
	}

	private DataView getExpDataView(SAAExam e) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, e.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, e.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, e.getType());
		return view;
	}

	@Override
	public Mono<Experience> fillExpFields(Experience e) {
		// TODO Auto-generated method stub
		return null;
	}

}
