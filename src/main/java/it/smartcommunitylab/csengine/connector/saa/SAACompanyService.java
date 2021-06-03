package it.smartcommunitylab.csengine.connector.saa;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.OrganisationAttr;
import it.smartcommunitylab.csengine.model.Address;
import reactor.core.publisher.Mono;

@Component(value="saaCompany")
public class SAACompanyService {

	public Mono<Map<String, Object>> refreshOrganisation(String extId, String uri) {
		WebClient client = WebClient.create(uri);
		return client.get().uri("/company?extId=" + extId).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAACompany.class).flatMap(i -> this.getDataView(i));
					}
					return Mono.empty();
				});		
	}
	
	private Mono<Map<String, Object>> getDataView(SAACompany c) {
		Map<String, Object> view = new HashMap<>();
		Address address = new Address();
		address.setExtendedAddress(c.getAddress());
		view.put(OrganisationAttr.name.label, c.getName());
		view.put(OrganisationAttr.address.label, address);
		view.put(OrganisationAttr.email.label, c.getEmail());
		view.put(OrganisationAttr.pec.label, c.getPec());
		view.put(OrganisationAttr.fiscalCode.label, c.getPartita_iva());
		view.put(OrganisationAttr.phone.label, c.getPhone());
		return Mono.just(view);
	}


}
