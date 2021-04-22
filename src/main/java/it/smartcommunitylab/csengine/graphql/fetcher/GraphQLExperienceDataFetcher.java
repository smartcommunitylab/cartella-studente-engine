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
import it.smartcommunitylab.csengine.model.Competence;
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
			dto.setId(e.getId());
			dto.setTitle((String) expView.getAttributes().get(ExpAttr.title.label));
			dto.setDateFrom((String) expView.getAttributes().get(ExpAttr.dateFrom.label));
			dto.setDateTo((String) expView.getAttributes().get(ExpAttr.dateTo.label));
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
	
	@SuppressWarnings("unchecked")
	private Flux<CompetenceDTO> getCompetenceDTO(Experience e) {
		List<CompetenceDTO> list = new ArrayList<>();
		DataView view = e.getViews().get(EntityType.exp.label);
		if(view.getAttributes().containsKey(ExpAttr.competences.label)) {
			List<Competence> compList = (List<Competence>) view.getAttributes().get(ExpAttr.competences.label);
			compList.forEach(c -> list.add(getCompetenceDTO(c, "it")));
		}
		return Flux.fromIterable(list);
	}
	
	private CompetenceDTO getCompetenceDTO(Competence c, String lang) {
		CompetenceDTO dto = new CompetenceDTO();
		dto.setUri(c.getUri());
		dto.setConcentType(c.getConcentType());
		dto.setPreferredLabel(c.getPreferredLabel().get(lang));
		dto.setAltLabel(c.getAltLabel().get(lang));
		return dto;
	}
	
}
