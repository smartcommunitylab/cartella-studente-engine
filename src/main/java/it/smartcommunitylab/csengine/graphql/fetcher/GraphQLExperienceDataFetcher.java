package it.smartcommunitylab.csengine.graphql.fetcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.dto.CompetenceDTO;
import it.smartcommunitylab.csengine.model.dto.ExamDTO;
import it.smartcommunitylab.csengine.model.dto.ExperienceDTO;
import it.smartcommunitylab.csengine.model.dto.OrganisationDTO;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import reactor.core.publisher.Flux;

@Component
public class GraphQLExperienceDataFetcher {
	@Autowired
	ExperienceRepository experienceRepository;
	
	public DataFetcher<Stream<ExamDTO>> searchExamsByPersonId() {
		return dataFetchingEnvironment -> {
			String personId = dataFetchingEnvironment.getArgument("personId");
			return experienceRepository.findAllByPersonIdAndEntityType(personId, EntityType.exam.label)
					.map(this::getExamDTO)
					.toStream();
		};
	}
	
	public DataFetcher<OrganisationDTO> getOrganization() {
		return dataFetchingEnvironment -> {
			ExperienceDTO exp = dataFetchingEnvironment.getSource();
			return experienceRepository.findById(exp.getId()).map(this::getOrganisationDTO).block();
		};
	}
	
	public DataFetcher<Stream<CompetenceDTO>> getCompetences() {
		return dataFetchingEnvironment -> {
			ExperienceDTO exp = dataFetchingEnvironment.getSource();
			return experienceRepository.findById(exp.getId()).flatMapMany(this::getCompetenceDTO).toStream();
		};
	}
	
	private ExamDTO getExamDTO(Experience e) {
		ExamDTO dto = new ExamDTO();
		DataView examView = e.getViews().get(EntityType.exam.label);
		DataView expView = e.getViews().get(EntityType.exp.label);
		
		if(expView != null) {
			dto.setId((String) expView.getAttributes().get(ExpAttr.id.label));
			dto.setId((String) expView.getAttributes().get(ExpAttr.title.label));
			dto.setId((String) expView.getAttributes().get(ExpAttr.dateFrom.label));
			dto.setId((String) expView.getAttributes().get(ExpAttr.dateTo.label));
		}
		
		if(examView != null) {
			dto.setType((String) examView.getAttributes().get(ExamAttr.type.label));
			dto.setQualification((String) examView.getAttributes().get(ExamAttr.qualification.label));
			dto.setHonour((Boolean) examView.getAttributes().get(ExamAttr.honour.label));
			dto.setGrade((String) examView.getAttributes().get(ExamAttr.grade.label));
			dto.setResult((Boolean) examView.getAttributes().get(ExamAttr.result.label));
			dto.setExternalCandidate((Boolean) examView.getAttributes().get(ExamAttr.externalCandidate.label));			
		}
		
		return dto;
	}
	
	@SuppressWarnings("unchecked")
	private OrganisationDTO getOrganisationDTO(Experience e) {
		if(e.getViews().containsKey(EntityType.exam.label)) {
			DataView view = e.getViews().get(EntityType.exam.label);
			if(view.getAttributes().containsKey(ExpAttr.organisation.label)) {
				Map<String, Object> organisationView = (Map<String, Object>) view.getAttributes().get(ExpAttr.organisation.label);
				return this.getOrganisationDTO(organisationView);
			}
		}
		return null;
	}
	
	private OrganisationDTO getOrganisationDTO(Map<String, Object> view) {
		OrganisationDTO dto = new OrganisationDTO();
		//TODO convert view to dto
		return dto;
	}
	
	private Flux<CompetenceDTO> getCompetenceDTO(Experience e) {
		List<CompetenceDTO> list = new ArrayList<>();
		return Flux.fromIterable(list);
	}
	
	private CompetenceDTO getCompetenceDTO(Map<String, Object> view) {
		CompetenceDTO dto = new CompetenceDTO();
		return dto;		
	}

}
