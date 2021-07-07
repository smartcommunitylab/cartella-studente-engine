package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EnrollmentAttr;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.util.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SAAEnrollmentService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	
	@Autowired
	SAAInstituteService instituteService;
	
	String viewName;
	String uri;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("/enrollment?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAEnrollment.class).flatMapSequential(e -> this.getEnrollment(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getEnrollment(String personId, SAAEnrollment e) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.enrollment.label);
		exp.getViews().put(EntityType.enrollment.label, getEnrollmentDataView(e));
		exp.getViews().put(viewName, getDataView(e));
		return getExpDataView(e).flatMap(view -> {
			exp.getViews().put(EntityType.exp.label, view);
			return Mono.just(exp);
		});		
	}
	
	private DataView getDataView(SAAEnrollment e) {
		ExtRef identity = new ExtRef(e.getExtId(), e.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("extId", e.getExtId());
		view.getAttributes().put("dateFrom", e.getDateFrom());
		view.getAttributes().put("dateTo", e.getDateTo());
		view.getAttributes().put("course", e.getCourse());
		view.getAttributes().put("schoolYear", e.getSchoolYear());
		view.getAttributes().put("classroom", e.getClassroom());
		view.getAttributes().put("instituteRef", e.getInstituteRef());
		return view;
	}
	
	private DataView getEnrollmentDataView(SAAEnrollment e) {
		DataView view = new DataView();
		view.getAttributes().put(EnrollmentAttr.schoolYear.label, e.getSchoolYear());
		view.getAttributes().put(EnrollmentAttr.course.label, e.getCourse());
		view.getAttributes().put(EnrollmentAttr.classroom.label, e.getClassroom());
		return view;
	}

	private Mono<DataView> getExpDataView(SAAEnrollment e) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, e.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, e.getDateTo());
		if(Utils.isNotEmpty(e.getInstituteRef())) {
			return instituteService.refreshOrganisation(e.getInstituteRef(), uri).flatMap(map -> {
				view.getAttributes().put(ExpAttr.organisation.label, map);
				return Mono.just(view);
			});
		}
		return Mono.just(view);
	}

	@Override
	public void setView(String view) {
		this.viewName = view;
	}

	@Override
	public void setUri(String uri) {
		this.uri = uri;
	}

}
