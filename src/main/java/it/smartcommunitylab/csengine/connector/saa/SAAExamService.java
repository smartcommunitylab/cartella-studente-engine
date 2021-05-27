package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.CompetenceAttr;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
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
public class SAAExamService implements ExperienceConnector {
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
		return client.get().uri("?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAExam.class).flatMapSequential(e -> this.getExam(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getExam(String personId, SAAExam e) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.exam.label);
		exp.getViews().put(EntityType.exam.label, getExamDataView(e));
		exp.getViews().put(viewName, getDataView(e));
		return getExpDataView(e).flatMap(view -> {
			exp.getViews().put(EntityType.exp.label, view);
			return Mono.just(exp);
		});		
	}
	
	private DataView getDataView(SAAExam e) {
		ExtRef identity = new ExtRef(e.getExtId(), e.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("extId", e.getExtId());
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

	private Mono<DataView> getExpDataView(SAAExam e) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, e.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, e.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, e.getType());
		if(e.getCompetences() != null) {
			List<Map<String, Object>> list = new ArrayList<>();
			e.getCompetences().forEach(c -> {
				list.add(getCompetence(c));
			});
			view.getAttributes().put(ExpAttr.competences.label, list);
		}
		if(Utils.isNotEmpty(e.getInstituteRef())) {
			return instituteService.refreshOrganisation(e.getInstituteRef()).flatMap(map -> {
				view.getAttributes().put(ExpAttr.organisation.label, map);
				return Mono.just(view);
			});
		}
		return Mono.just(view);
	}

	private Map<String, Object> getCompetence(SAACompetence c) {
		Map<String, Object> map = new HashMap<>();
		map.put(CompetenceAttr.uri.label, c.getUri());
		map.put(CompetenceAttr.concentType.label, c.getConcentType());
		map.put(CompetenceAttr.preferredLabel.label, c.getPreferredLabel());
		map.put(CompetenceAttr.altLabel.label, c.getAltLabel());
		return map;
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
