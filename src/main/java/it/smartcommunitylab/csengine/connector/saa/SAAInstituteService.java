package it.smartcommunitylab.csengine.connector.saa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import it.smartcommunitylab.csengine.common.View;
import it.smartcommunitylab.csengine.connector.OrganisationConnector;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.ExtRef;
import it.smartcommunitylab.csengine.model.Organisation;
import it.smartcommunitylab.csengine.repository.OrganisationRepository;
import reactor.core.publisher.Mono;

@Component(value="saaInstitute")
public class SAAInstituteService implements OrganisationConnector {
	@Autowired
	OrganisationRepository organisationRepository;

	@Override
	public Mono<Organisation> refreshOrganisation(Organisation o) {
		String extId = o.getViews().get(View.SAA.label).getIdentity().getExtUri();
		WebClient client = WebClient.create("http://localhost:8080");
		return client.get().uri("/saa/institute?extId=" + extId).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().equals(HttpStatus.OK)) {
						return response.bodyToMono(SAAInstitute.class).flatMap(i -> this.updateIstitute(i));
					}
					return Mono.empty();
				});		
	}
	
	private Mono<Organisation> updateIstitute(SAAInstitute ist) {
		return organisationRepository.findByExtRef(View.SAA.label, ist.getExtId(), ist.getOrigin())
		.flatMap(o -> organisationRepository.updateView(o.getId(), View.SAA.label, getDataView(ist)));
	}
	
	@Override
	public Mono<Organisation> fillOrganisationFields(Organisation o) {
		DataView view = o.getViews().get(View.SAA.label);
		return organisationRepository.updateFields(o.getId(), 
				(String) view.getAttributes().get("name"), 
				null, 
				(String) view.getAttributes().get("email"));
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
