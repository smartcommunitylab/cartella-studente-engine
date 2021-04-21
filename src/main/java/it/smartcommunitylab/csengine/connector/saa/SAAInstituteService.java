package it.smartcommunitylab.csengine.connector.saa;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.ExtRef;
import reactor.core.publisher.Mono;

@Component(value="saaInstitute")
public class SAAInstituteService {

	public Mono<DataView> refreshOrganisation(String extId) {
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/institute?extId=" + extId).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAInstitute.class).flatMap(i -> this.updateIstitute(i));
					}
					return Mono.empty();
				});		
	}
	
	private Mono<DataView> updateIstitute(SAAInstitute ist) {
		return Mono.just(getDataView(ist));
	}
	
	private DataView getDataView(SAAInstitute i) {
		ExtRef identity = new ExtRef(i.getExtId(), i.getOrigin());
		DataView view = new DataView();
		view.setIdentity(identity);
		view.getAttributes().put("name", i.getName());
		view.getAttributes().put("address", i.getAddress());
		view.getAttributes().put("email", i.getEmail());
		view.getAttributes().put("pec", i.getPec());
		return view;
	}


}
