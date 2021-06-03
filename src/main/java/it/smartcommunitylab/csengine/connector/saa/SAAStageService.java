package it.smartcommunitylab.csengine.connector.saa;

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
import it.smartcommunitylab.csengine.util.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SAAStageService implements ExperienceConnector {
	@Autowired
	ExperienceRepository experienceRepository;
	
	@Autowired
	SAACompanyService companyService;
	
	String viewName;
	String uri;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("/stage?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAStage.class).flatMapSequential(e -> this.getStage(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getStage(String personId, SAAStage s) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.stage.label);
		exp.getViews().put(EntityType.stage.label, getStageDataView(s));
		exp.getViews().put(viewName, getDataView(s));
		return getExpDataView(s).flatMap(view -> {
			exp.getViews().put(EntityType.exp.label, view);
			return Mono.just(exp);
		});		
	}
	
	private DataView getStageDataView(SAAStage s) {
		DataView view = new DataView();
		view.getAttributes().put(StageAttr.type.label, s.getType());
		view.getAttributes().put(StageAttr.duration.label, s.getDuration());
		Address address = new Address();
		address.setExtendedAddress(s.getLocation());
		view.getAttributes().put(StageAttr.address.label, address);
		return view;
	}
	
	private Mono<DataView> getExpDataView(SAAStage s) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, s.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, s.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, s.getTitle());
		if(Utils.isNotEmpty(s.getCompanyRef())) {
			companyService.refreshOrganisation(s.getCompanyRef(), uri).flatMap(map -> {
				view.getAttributes().put(ExpAttr.organisation.label, map);
				return Mono.just(view);
			});
		}
		return Mono.just(view);
	}	
	
	private DataView getDataView(SAAStage s) {
		ExtRef identity = new ExtRef(s.getExtId(), s.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("extId", s.getExtId());
		view.getAttributes().put("dateFrom", s.getDateFrom());
		view.getAttributes().put("dateTo", s.getDateTo());
		view.getAttributes().put("title", s.getTitle());
		view.getAttributes().put("type", s.getType());
		view.getAttributes().put("duration", s.getDuration());
		view.getAttributes().put("location", s.getLocation());
		view.getAttributes().put("companyRef", s.getCompanyRef());
		return view;
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
