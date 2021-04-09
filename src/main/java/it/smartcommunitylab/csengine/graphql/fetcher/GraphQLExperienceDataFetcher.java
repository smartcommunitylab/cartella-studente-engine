package it.smartcommunitylab.csengine.graphql.fetcher;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.dto.ExamDTO;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;

@Component
public class GraphQLExperienceDataFetcher {
	@Autowired
	ExperienceRepository experienceRepository;
	
	public DataFetcher<Stream<ExamDTO>> searchExamsByPersonId() {
		return dataFetchingEnvironment -> {
			String personId = dataFetchingEnvironment.getArgument("personId");
			return experienceRepository.findAllByPersonIdAndEntityType(personId, EntityType.EXAM.label)
					.map(this::getExamDTO)
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

}
