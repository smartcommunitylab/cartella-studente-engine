package it.smartcommunitylab.csengine.manager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import it.smartcommunitylab.csengine.common.CompetenceAttr;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.exception.BadRequestException;
import it.smartcommunitylab.csengine.exception.NotFoundException;
import it.smartcommunitylab.csengine.exception.ServiceException;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.FileDocument;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.model.dto.CompetenceReport;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.FileDocumentRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;
import it.smartcommunitylab.csengine.util.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class UserDataManager {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	@Autowired
	private FileDocumentRepository documentRepository;
	@Autowired
	private DocumentManager documentManager;
	
	public Mono<Person> getPerson(String fiscalCode) {
		return personRepository.findByFiscalCode(fiscalCode);
	}

	public Mono<Experience> getExperience(String fiscalCode, String expId) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")));
		});		
	}
	
	public Flux<Experience> getExperienceByFiscalCode(String fiscalCode, String entityType) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(this.addNewPerson(fiscalCode))
		.flatMapMany(person -> {
			if(person.getId() != null) {
				return experienceRepository.findAllByPersonIdAndEntityType(person.getId(), entityType);
			} else {
				return Flux.empty();
			}
		});
	}
	
	private Mono<Person> addNewPerson(String fiscalCode) {
		Person p = new Person();
		p.setFiscalCode(fiscalCode);
		return Mono.just(p);
	}

	public Mono<Experience> deleteExperience(String fiscalCode, String expId) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not erasable"));
						}						
						return experienceRepository.deleteByExpId(expId);
					});
		});
	}

	public Mono<Experience> addExperience(String fiscalCode, String entityType, 
			Map<String, Object> attributes) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			Experience exp = new Experience();
			exp.setPersonId(person.getId());
			exp.setEntityType(entityType);
			exp.setPersonal(true);
			DataView expView = getExpDataView(attributes);
			exp.getViews().put(EntityType.exp.label, expView);
			DataView entityView = new DataView();
			entityView.getAttributes().putAll(attributes);
			exp.getViews().put(entityType, entityView);
			return experienceRepository.save(exp);
		});		
	}
	
	public Mono<Experience> updateExperience(String fiscalCode, String expId,
			Map<String, DataView> views) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not editable"));
						}
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateViews((Experience) exp, views);
					});			
		});
	}
	
	public Mono<Experience> updateExperienceView(String fiscalCode, String expId,
			String viewName, DataView view) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.isPersonal()) {
							return Mono.error(new BadRequestException("entity not editable"));
						}
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateView((Experience) exp, viewName, view);
					});			
		});
	}
	
	public Mono<Experience> updateUserExperienceView(String fiscalCode, String expId,
			String viewName, DataView view) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateView((Experience) exp, viewName + "User", view);
					});			
		});
	}
	
	public Mono<Experience> updateExperienceAnnotation(String fiscalCode, String expId, Map<String, Object> annotation) {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						return updateAnnotation((Experience) exp, annotation);
					});			
		});
	}
	
	private Mono<Experience> updateAnnotation(Experience exp, Map<String, Object> annotation) {
		exp.getViews().get(EntityType.exp.label).getAttributes().put(ExpAttr.annotation.label, annotation);
		return experienceRepository.save(exp);
	}	
	
	private Mono<Experience> updateView(Experience exp, String viewName, DataView view) {
		exp.getViews().put(viewName, view);
		return experienceRepository.save(exp);
	}

	private Mono<Experience> updateViews(Experience exp, Map<String, DataView> views) {
		exp.setViews(views);
		return experienceRepository.save(exp);
	}

	/**
	 * Extract from attributes the main level Experience Data View
	 */
	private DataView getExpDataView(Map<String, Object> attributes) {
		DataView view = new DataView();
		for(ExpAttr attr : ExpAttr.values()) {
			if(attributes.containsKey(attr.label)) {
				view.getAttributes().put(attr.label, attributes.get(attr.label));
				attributes.remove(attr.label);
			}			
		}
		return view;
	}
	
	public Flux<CompetenceReport> getUserCompetences(String fiscalCode) {
		return personRepository.findByFiscalCode(fiscalCode)
		.flatMapMany(person -> {
			if(person.getId() != null) {
				Map<String, CompetenceReport> map = new HashMap<>();
				return experienceRepository.findAllByPersonId(person.getId())
						.collectList().flatMapMany(list -> {
							for(Experience e : list) {
								DataView view = e.getViews().get(EntityType.exp.label);
								if((view != null) && (view.getAttributes().containsKey(ExpAttr.competences.label))) {
									List<Map<String, Object>> competences = (List<Map<String, Object>>) view.getAttributes().get(ExpAttr.competences.label);
									for(Map<String, Object> c : competences) {
										String uri = (String) c.get(CompetenceAttr.uri.label);
										if(Utils.isNotEmpty(uri)) {
											CompetenceReport report = map.get(uri);
											if(report == null) {
												report = new CompetenceReport();
												report.setCompetence(c);
												report.setExperiences(new ArrayList<>());
											}
											report.getExperiences().add(e);
											map.put(uri, report);
										}
									}
								}
							}
							return Flux.fromIterable(map.values());
						});
			} else {
				return Flux.empty();
			}
		});		
	}
	
	public Mono<FileDocument> uploadDocumentFile(String fiscalCode, String expId, MultipartFile file) throws Exception {
		return personRepository.findByFiscalCode(fiscalCode)
		.switchIfEmpty(Mono.error(new NotFoundException("person not found")))
		.flatMap(person -> {
			return experienceRepository.findById(expId)
					.switchIfEmpty(Mono.error(new NotFoundException("entity not found")))
					.flatMap(exp -> {						
						if(!exp.getPersonId().equals(person.getId())) {
							return Mono.error(new BadRequestException("person id not corrispondig"));
						}
						FileDocument fd = new FileDocument();
						fd.setPersonId(person.getId());
						fd.setExperienceId(expId);
						fd.setFileKey(Utils.getUUID());
						fd.setContentType(file.getContentType());
						fd.setFilename(file.getOriginalFilename());
						fd.setSize(file.getSize());
						fd.setDataUpload(LocalDate.now());
						try {
							documentManager.uploadFile(file, fd.getFileKey());
						} catch (Exception e) {
							return Mono.error(new ServiceException("storing file error:" + e.getMessage()));
						}
						return documentRepository.save(fd);
					});			
		});
	}
	
	public Mono<Void> deleteDocumentFile(String personId, String expId, String docId) throws Exception {
		return documentRepository.findById(docId)
				.switchIfEmpty(Mono.error(new NotFoundException("doc not found")))
				.flatMap(fd -> {
					if(!expId.equals(fd.getExperienceId()) || !personId.equals(fd.getPersonId())) {
						return Mono.error(new BadRequestException("person or experience not corrispondig"));
					}
					try {
						documentManager.deleteFile(fd.getFileKey());
					} catch (Exception e) {
						return Mono.error(new ServiceException("deleting file error:" + e.getMessage()));
					}
					return documentRepository.delete(fd);
				});
	}
	
	public Mono<FileDocument> downloadDocumentFile(String personId, String expId, String docId) throws Exception {
		return documentRepository.findById(docId)
				.switchIfEmpty(Mono.error(new NotFoundException("doc not found")))
				.flatMap(fd -> {
					if(!expId.equals(fd.getExperienceId()) || !personId.equals(fd.getPersonId())) {
						return Mono.error(new BadRequestException("person or experience not corrispondig"));
					}
					try {
						String tmpFile = documentManager.downloadFile(fd.getFileKey());
						fd.setLocalPath(tmpFile);
						return Mono.just(fd);
					} catch (Exception e) {
						return Mono.error(new ServiceException("downloding file error:" + e.getMessage()));
					}
				});		
	}

}
