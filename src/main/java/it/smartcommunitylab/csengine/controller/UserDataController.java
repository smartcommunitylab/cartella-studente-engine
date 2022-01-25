package it.smartcommunitylab.csengine.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.smartcommunitylab.csengine.exception.BadRequestException;
import it.smartcommunitylab.csengine.exception.ServiceException;
import it.smartcommunitylab.csengine.manager.UserDataManager;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.FileDocument;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.model.dto.CompetenceReport;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserDataController implements CSController {
	
	@Autowired
	private UserDataManager userDataManager;

	@GetMapping("/api/user/profile")
	public Mono<Person> getProfile(HttpServletRequest request) throws Exception {
		return userDataManager.getPerson("111222");
	}
	
	@GetMapping("/api/user/experience/{expId}")
	public Mono<Experience> getExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.getExperience(fiscalCode, expId);
	}
	
	@GetMapping("/api/user/experience/entityType")
	public Flux<Experience> getExperienceByFiscalCode(
			@RequestParam String fiscalCode,
			@RequestParam String entityType,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.getExperienceByFiscalCode(fiscalCode, entityType);
	}
	
	@PostMapping("/api/user/experience")
	public Mono<Experience> addExperience(
			@RequestParam String fiscalCode,
			@RequestParam String entityType,
			@RequestBody Map<String, Object> attributes,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.addExperience(fiscalCode, entityType, attributes);
	}

	@PutMapping("/api/user/experience/{expId}")
	public Mono<Experience> updateExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestBody Map<String, DataView> views,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.updateExperience(fiscalCode, expId, views);
	}

	@PutMapping("/api/user/experience/{expId}/view")
	public Mono<Experience> updateExperienceView(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestParam String viewName,
			@RequestBody DataView view,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.updateExperienceView(fiscalCode, expId, viewName, view);
	}
	
	@PutMapping("/api/user/experience/{expId}/userView")
	public Mono<Experience> updateUserExperienceView(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestParam String viewName,
			@RequestBody DataView view,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.updateUserExperienceView(fiscalCode, expId, viewName, view);
	}
	
	@PutMapping("/api/user/experience/{expId}/annotation")
	public Mono<Experience> updateExperienceAnnotation(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			@RequestBody Map<String, Object> annotation,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.updateExperienceAnnotation(fiscalCode, expId, annotation);
	}
	
	@DeleteMapping("/api/user/experience/{expId}")
	public Mono<Experience> deleteExperience(
			@PathVariable String expId,
			@RequestParam String fiscalCode,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.deleteExperience(fiscalCode, expId);
	}
	
	@GetMapping("/api/user/competences")
	public Flux<CompetenceReport> getUserCompetences(
			@RequestParam String fiscalCode,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.getUserCompetences(fiscalCode);		
	}
	
	@PostMapping("/api/user/document/upload")
	public Mono<FileDocument> uploadFileDocument(
			@RequestParam String fiscalCode,
			@RequestParam String expId,
			@RequestParam("data") MultipartFile file,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.uploadDocumentFile(fiscalCode, expId, file);
	}
	
	@DeleteMapping("/api/user/document")
	public Mono<Void> deleteFileDocument(
			@RequestParam String personId,
			@RequestParam String expId,
			@RequestParam String docId,
			HttpServletRequest request) throws Exception {
		//TODO check role
		return userDataManager.deleteDocumentFile(personId, expId, docId);
	}
	
	@GetMapping("/api/user/document/download")
	public Mono<Void> downloadFileDocument(
			@RequestParam String personId,
			@RequestParam String expId,
			@RequestParam String docId,
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		return userDataManager.downloadDocumentFile(personId, expId, docId).flatMap(fd -> {
			try {
				downloadContent(fd, response);
			} catch (Exception e) {
				return Mono.error(new ServiceException("downloding file error:" + e.getMessage()));
			}
			return Mono.empty();
		});
	}
	
	private void downloadContent(FileDocument doc, HttpServletResponse response) throws Exception {
		try {
			response.setContentType(doc.getContentType());
			response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.getFilename() + "\"");
			response.getOutputStream().write(FileUtils.readFileToByteArray(new File(doc.getLocalPath())));
		} catch (FileNotFoundException e) {
			throw new BadRequestException("file documento non trovato");
		}			
	}
	
}
