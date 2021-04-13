package it.smartcommunitylab.csengine.graphql.fetcher;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.model.Competence;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Organisation;
import it.smartcommunitylab.csengine.model.dto.CompetenceDTO;
import it.smartcommunitylab.csengine.model.dto.ExamDTO;
import it.smartcommunitylab.csengine.model.dto.ExperienceDTO;
import it.smartcommunitylab.csengine.model.dto.OrganisationDTO;
import it.smartcommunitylab.csengine.repository.CompetenceRepository;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.OrganisationRepository;

@Component
public class GraphQLExperienceDataFetcher {
	@Autowired
	ExperienceRepository experienceRepository;
	@Autowired
	OrganisationRepository organisationRepository;
	@Autowired
	CompetenceRepository competenceRepository;
	
	public DataFetcher<Stream<ExamDTO>> searchExamsByPersonId() {
		return dataFetchingEnvironment -> {
			String personId = dataFetchingEnvironment.getArgument("personId");
			return experienceRepository.findAllByPersonIdAndEntityType(personId, EntityType.EXAM.label)
					.map(this::getExamDTO)
					.toStream();
		};
	}
	
	public DataFetcher<OrganisationDTO> getOrganization() {
		return dataFetchingEnvironment -> {
			ExperienceDTO exp = dataFetchingEnvironment.getSource();
			if(exp.getOrganisationId() != null) {
				return organisationRepository.findById(exp.getOrganisationId())
						.map(this::getOrganisationDTO).block();
			}
			return null;
		};
	}
	
	public DataFetcher<Stream<CompetenceDTO>> getCompetences() {
		return dataFetchingEnvironment -> {
			ExperienceDTO exp = dataFetchingEnvironment.getSource();
			return competenceRepository.findByUriIn(exp.getCompetences())
					.map(this::getCompetenceDTO)
					.toStream();
		};
	}
	
	private ExamDTO getExamDTO(Experience e) {
		ExamDTO dto = new ExamDTO(e);
		//TODO add logic to manage multiple view
		DataView dataView = e.getViews().get("view1");
		if(dataView != null) {
			dto.setType((String) dataView.getAttributes().get(ExamAttr.TYPE.label));
			dto.setQualification((String) dataView.getAttributes().get(ExamAttr.QUALIFICATION.label));
			dto.setHonour((Boolean) dataView.getAttributes().get(ExamAttr.HONOUR.label));
			dto.setGrade((String) dataView.getAttributes().get(ExamAttr.GRADE.label));
			dto.setResult((Boolean) dataView.getAttributes().get(ExamAttr.RESULT.label));
			dto.setExternalCandidate((Boolean) dataView.getAttributes().get(ExamAttr.EXTCANDIDATE.label));
		}
		return dto;
	}
	
	private OrganisationDTO getOrganisationDTO(Organisation o) {
		OrganisationDTO dto = new OrganisationDTO(o);
		//TODO add logic to manage multiple view
		return dto;
	}
	
	private CompetenceDTO getCompetenceDTO(Competence c) {
		CompetenceDTO dto = new CompetenceDTO(c, "it");
		//TODO add logic to manage multiple view
		return dto;
	}

}
