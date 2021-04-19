package it.smartcommunitylab.csengine.repository;

import java.time.LocalDate;
import java.util.Map;

import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Mono;

public interface ExperienceRepositoryCustom {
	public Mono<Experience> updateView(String expId, String view, DataView dw); 
	public Mono<Experience> updateFields(String expId, String title, String description, 
			LocalDate dateFrom, LocalDate dateTo, String organisationId, Map<String, Object> attributes);
}
