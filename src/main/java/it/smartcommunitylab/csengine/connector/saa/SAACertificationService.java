package it.smartcommunitylab.csengine.connector.saa;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.CertificationAttr;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
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
public class SAACertificationService implements ExperienceConnector {
	@Autowired
	SAACompanyService companyService;
	
	String viewName;
	String uri;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Override
	public Flux<Experience> refreshExp(Person person) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("/certification?fiscalCode=" + person.getFiscalCode()).accept(MediaType.APPLICATION_JSON)
				.exchangeToFlux(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToFlux(SAACertification.class).flatMapSequential(e -> this.getCertification(person.getId(), e));
					}
					return Flux.empty();
				});		
	}

	private Mono<Experience> getCertification(String personId, SAACertification cert) {
		Experience exp = new Experience();
		exp.setPersonId(personId);
		exp.setEntityType(EntityType.certification.label);
		exp.getViews().put(EntityType.certification.label, getCertificationDataView(cert));
		exp.getViews().put(viewName, getDataView(cert));
		return getExpDataView(cert).flatMap(view -> {
			exp.getViews().put(EntityType.exp.label, view);
			return Mono.just(exp);
		});		
	}
	
	private DataView getCertificationDataView(SAACertification cert) {
		DataView view = new DataView();
		view.getAttributes().put(CertificationAttr.type.label, cert.getType());
		view.getAttributes().put(CertificationAttr.duration.label, cert.getDuration());
		Address address = new Address();
		address.setExtendedAddress(cert.getLocation());
		view.getAttributes().put(CertificationAttr.address.label, address);
		view.getAttributes().put(CertificationAttr.contact.label, cert.getContact());
		view.getAttributes().put(CertificationAttr.grade.label, cert.getJudgment());
		view.getAttributes().put(CertificationAttr.language.label, cert.getLanguage());
		return view;
	}
	
	private Mono<DataView> getExpDataView(SAACertification cert) {
		DataView view = new DataView();
		view.getAttributes().put(ExpAttr.dateFrom.label, cert.getDateFrom());
		view.getAttributes().put(ExpAttr.dateTo.label, cert.getDateTo());
		view.getAttributes().put(ExpAttr.title.label, cert.getTitle());
		if(Utils.isNotEmpty(cert.getCompanyRef())) {
			companyService.refreshOrganisation(cert.getCompanyRef(), uri).flatMap(map -> {
				view.getAttributes().put(ExpAttr.organisation.label, map);
				return Mono.just(view);
			});
		}
		return Mono.just(view);
	}	
	
	private DataView getDataView(SAACertification cert) {
		ExtRef identity = new ExtRef(cert.getExtId(), cert.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("extId", cert.getExtId());
		view.getAttributes().put("dateFrom", cert.getDateFrom());
		view.getAttributes().put("dateTo", cert.getDateTo());
		view.getAttributes().put("title", cert.getTitle());
		view.getAttributes().put("type", cert.getType());
		view.getAttributes().put("duration", cert.getDuration());
		view.getAttributes().put("location", cert.getLocation());
		view.getAttributes().put("companyRef", cert.getCompanyRef());
		view.getAttributes().put("certifier", cert.getCertifier());
		view.getAttributes().put("contact", cert.getContact());
		view.getAttributes().put("description", cert.getDescription());
		view.getAttributes().put("judgment", cert.getJudgment());
		view.getAttributes().put("dateCertification", cert.getDateCertification());
		view.getAttributes().put("language", cert.getLanguage());
		view.getAttributes().put("judgmentmax", cert.getJudgmentmax());
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
