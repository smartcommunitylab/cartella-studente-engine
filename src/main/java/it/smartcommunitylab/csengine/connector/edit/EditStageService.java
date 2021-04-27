package it.smartcommunitylab.csengine.connector.edit;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.common.StageAttr;
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.Address;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EditStageService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	
	String viewName;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/edit/stage?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(EditStage.class).flatMapSequential(e -> this.getStage(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getStage(String personId, EditStage s) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.stage.label);
		exp.getViews().put(EntityType.exp.label, getExpDataView(s));
		exp.getViews().put(EntityType.stage.label, getStageDataView(s));
		exp.getViews().put(viewName, getDataView(s));
		return Mono.just(exp);
	}
	
	private DataView getStageDataView(EditStage s) {
		DataView view = new DataView();
		view.getAttributes().put(StageAttr.type.label, s.getType());
		view.getAttributes().put(StageAttr.duration.label, s.getDuration());
		Address address = new Address();
		address.setExtendedAddress(s.getLocation());
		view.getAttributes().put(StageAttr.address.label, address);
		return view;
	}
	
	private DataView getExpDataView(EditStage s) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, s.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, s.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, s.getTitle());
		return view;
	}	
	
	private DataView getDataView(EditStage s) {
		ExtRef identity = new ExtRef(s.getExtId(), s.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("dateFrom", s.getDateFrom());
		view.getAttributes().put("dateTo", s.getDateTo());
		view.getAttributes().put("title", s.getTitle());
		view.getAttributes().put("type", s.getType());
		view.getAttributes().put("duration", s.getDuration());
		view.getAttributes().put("location", s.getLocation());
		view.getAttributes().put("saaId", s.getSaaId());
		return view;
	}

	@Override
	public void setView(String view) {
		this.viewName = view;
	}

}
