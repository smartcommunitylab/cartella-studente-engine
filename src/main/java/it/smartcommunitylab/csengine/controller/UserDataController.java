package it.smartcommunitylab.csengine.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.manager.UserDataManager;
import it.smartcommunitylab.csengine.model.Experience;
import reactor.core.publisher.Mono;

@RestController
public class UserDataController implements CSController {
	
	@Autowired
	private UserDataManager userDataManager;

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
			@RequestParam String entityType,
			@RequestBody Map<String, Object> attributes) throws Exception {
		//TODO check role
		return userDataManager.updateExperience(fiscalCode, expId, entityType, attributes);
	}

	@DeleteMapping("/api/user/experience/{expId}")
	public Mono<Experience> deleteExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode) throws Exception {
		//TODO check role
		return userDataManager.deleteExperience(fiscalCode, expId);
	}

}
