package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.common.MobilityAttr;
import it.smartcommunitylab.csengine.connector.ExperienceConnector;
import it.smartcommunitylab.csengine.model.Address;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.util.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SAAMobilityService implements ExperienceConnector {
	@Autowired
	SAACompanyService companyService;
	
	String viewName;
	String uri;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("/mobility?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAAMobility.class).flatMapSequential(e -> this.getMobility(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getMobility(String personId, SAAMobility mob) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.stage.label);
		exp.getViews().put(EntityType.stage.label, getMobilityDataView(mob));
		exp.getViews().put(viewName, getDataView(mob));
		return getExpDataView(mob).flatMap(view -> {
			exp.getViews().put(EntityType.exp.label, view);
			return Mono.just(exp);
		});		
	}
	
	private DataView getMobilityDataView(SAAMobility mob) {
		DataView view = new DataView();
		view.getAttributes().put(MobilityAttr.type.label, mob.getType());
		view.getAttributes().put(MobilityAttr.duration.label, mob.getDuration());
		Address address = new Address();
		address.setExtendedAddress(mob.getLocation());
		view.getAttributes().put(MobilityAttr.address.label, address);
		return view;
	}
	
	private Mono<DataView> getExpDataView(SAAMobility mob) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, mob.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, mob.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, mob.getTitle());
		if(Utils.isNotEmpty(mob.getCompanyRef())) {
			companyService.refreshOrganisation(mob.getCompanyRef(), uri).flatMap(map -> {
				view.getAttributes().put(ExpAttr.organisation.label, map);
				return Mono.just(view);
			});
		}
		return Mono.just(view);
	}	
	
	private DataView getDataView(SAAMobility mob) {
		ExtRef identity = new ExtRef(mob.getExtId(), mob.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("extId", mob.getExtId());
		view.getAttributes().put("dateFrom", mob.getDateFrom());
		view.getAttributes().put("dateTo", mob.getDateTo());
		view.getAttributes().put("title", mob.getTitle());
		view.getAttributes().put("type", mob.getType());
		view.getAttributes().put("duration", mob.getDuration());
		view.getAttributes().put("location", mob.getLocation());
		view.getAttributes().put("companyRef", mob.getCompanyRef());
		view.getAttributes().put("contact", mob.getContact());
		view.getAttributes().put("description", mob.getDescription());
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
