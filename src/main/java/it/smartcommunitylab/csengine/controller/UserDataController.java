package it.smartcommunitylab.csengine.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.manager.UserDataManager;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserDataController implements CSController {
	
	@Autowired
	private UserDataManager userDataManager;

	@GetMapping("/api/user/experience/{expId}")
	public Mono<Experience> getExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode) throws Exception {
		//TODO check role
		return userDataManager.getExperience(fiscalCode, expId);
	}
	
	@GetMapping("/api/user/experience/entityType")
	public Flux<Experience> getExperienceByFiscalCode(
			@RequestParam String fiscalCode,
			@RequestParam String entityType) throws Exception {
		//TODO check role
		return userDataManager.getExperienceByFiscalCode(fiscalCode, entityType);
	}
	
	@PostMapping("/api/user/experience")
	public Mono<Experience> addExperience(
			@RequestParam String fiscalCode,
			@RequestParam String entityType,
			@RequestBody Map<String, Object> attributes) throws Exception {
		//TODO check role
		return userDataManager.addExperience(fiscalCode, entityType, attributes);
	}

	@PutMapping("/api/user/experience/{expId}")
	public Mono<Experience> updateExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestBody Map<String, DataView> views) throws Exception {
		//TODO check role
		return userDataManager.updateExperience(fiscalCode, expId, views);
	}

	@PutMapping("/api/user/experience/{expId}/view")
	public Mono<Experience> updateExperienceView(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestParam String viewName,
			@RequestBody DataView view) throws Exception {
		//TODO check role
		return userDataManager.updateExperienceView(fiscalCode, expId, viewName, view);
	}
	
	@PutMapping("/api/user/experience/{expId}/userView")
	public Mono<Experience> updateUserExperienceView(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestParam String viewName,
			@RequestBody DataView view) throws Exception {
		//TODO check role
		return userDataManager.updateUserExperienceView(fiscalCode, expId, viewName, view);
	}
	
	@PutMapping("/api/user/experience/{expId}/annotation")
	public Mono<Experience> updateExperienceAnnotation(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestBody Map<String, Object> annotation) throws Exception {
		//TODO check role
		return userDataManager.updateExperienceAnnotation(fiscalCode, expId, annotation);
	}
	
	@DeleteMapping("/api/user/experience/{expId}")
	public Mono<Experience> deleteExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode) throws Exception {
		//TODO check role
		return userDataManager.deleteExperience(fiscalCode, expId);
	}
	
}
