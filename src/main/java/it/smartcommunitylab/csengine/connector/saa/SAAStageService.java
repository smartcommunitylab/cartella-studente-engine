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
import it.smartcommunitylab.csengine.common.StageAttr;
import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component(value="saaStage")
public class SAAStageService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/stage?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAStage.class).flatMap(e -> this.updateStage(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> updateStage(String personId, SAAStage s) {
		return experienceRepository.findByExtRef(View.SAA.label, s.getExtId(), s.getOrigin())
		.switchIfEmpty(this.addNewExperience(personId, EntityType.STAGE))
		.flatMap(stage -> experienceRepository.updateView(stage.getId(), View.SAA.label, getDataView(s)));
	}
	
	private Mono<Experience> addNewExperience(String personId, EntityType type) {
		Experience exp = new Experience();
		exp.setEntityType(type.label);
		exp.setPersonId(personId);
		return experienceRepository.save(exp);
	}

	private DataView getDataView(SAAStage s) {
		ExtRef identity = new ExtRef(s.getExtId(), s.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("dateFrom", s.getDateFrom());
		view.getAttributes().put("dateTo", s.getDateTo());
		view.getAttributes().put("title", s.getTitle());
		view.getAttributes().put("type", s.getType());
		view.getAttributes().put("duration", s.getDuration());
		view.getAttributes().put("location", s.getLocation());
		return view;
	}

	@Override
	public Mono<Experience> fillExpFields(Experience e) {
		DataView view = e.getViews().get(View.SAA.label);
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(StageAttr.TYPE.label, view.getAttributes().get("type"));
		attributes.put(StageAttr.DURATION.label, view.getAttributes().get("duration"));
		attributes.put(StageAttr.LOCATION.label, view.getAttributes().get("location"));		
		return experienceRepository.updateFields(e.getId(), 
				(String) view.getAttributes().get("title"), "", 
				LocalDate.parse((String) view.getAttributes().get("dateFrom"), dtf), 
				LocalDate.parse((String) view.getAttributes().get("dateTo"), dtf), 
				null, attributes);
	}

}
