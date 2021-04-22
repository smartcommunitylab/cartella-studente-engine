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

@Component(value="saaInstitute")
public class SAAInstituteService {

	public Mono<Map<String, Object>> refreshOrganisation(String extId) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/institute?extId=" + extId).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAInstitute.class).flatMap(i -> this.getDataView(i));
					}
					return Mono.empty();
				});		
	}
	
	private Mono<Map<String, Object>> getDataView(SAAInstitute i) {
		Map<String, Object> view = new HashMap<>();
		Address address = new Address();
		address.setExtendedAddress(i.getAddress());
		view.put(OrganisationAttr.name.label, i.getName());
		view.put(OrganisationAttr.address.label, address);
		view.put(OrganisationAttr.email.label, i.getEmail());
		view.put(OrganisationAttr.pec.label, i.getPec());
		return Mono.just(view);
	}


}
