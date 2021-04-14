package it.smartcommunitylab.csengine.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.model.Competence;
import it.smartcommunitylab.csengine.model.DataView;
import it.smartcommunitylab.csengine.model.ExpCompetence;
import it.smartcommunitylab.csengine.model.Experience;
import it.smartcommunitylab.csengine.model.Organisation;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.repository.CompetenceRepository;
import it.smartcommunitylab.csengine.repository.ExpCompetenceRepository;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.OrganisationRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;

@RestController
public class InitController {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	@Autowired
	OrganisationRepository organisationRepository;
	@Autowired
	CompetenceRepository competenceRepository;
	@Autowired
	ExpCompetenceRepository expCompetenceRepository;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	@GetMapping("/init")
	public void init() throws InterruptedException {
		Person p = new Person();
		p.setFiscalCode(RandomStringUtils.randomAlphanumeric(16));
		p.setName("name1");
		p.setSurname("surname1");
		personRepository.deleteAll().block();
		p = personRepository.save(p).block();
		
		Organisation o = new Organisation();
		o.setName("Organisation1");
		o.setDescription("description");
		o.setFiscalCode("111111111");
		organisationRepository.deleteAll().block();
		o = organisationRepository.save(o).block();
		
		Competence c = new Competence();
		c.setUri("http://data.europa.eu/esco/skill/09638218-695c-44c7-bac3-26b45a2ae418");
		c.setConcentType("KnowledgeSkillCompetence");
		c.setPreferredLabel(ImmutableMap.of("it", "svolgere i patch test", "en", "conduct patch testing"));
		competenceRepository.deleteAll().block();
		c = competenceRepository.save(c).block();
		
		expCompetenceRepository.deleteAll().block();
		experienceRepository.deleteAll().block();
		int min = 10;
		int max = 31;
		Random random = new Random();
		for(int i = 0; i < 3; i++) {
			Experience exp = new Experience();
			exp.setPersonId(p.getId());
			exp.setOrganisationId(o.getId());
			exp.setTitle("title" + i);
			exp.setEntityType(EntityType.EXAM.label);
			int giorno = random.nextInt(max - min) + min;
			exp.setDateFrom(LocalDate.parse("2021-04-" + giorno, dtf));
			exp.setDateTo(LocalDate.parse("2021-04-" + giorno, dtf));
			
			DataView dataView = new DataView();
			dataView.getAttributes().put(ExamAttr.TYPE.label, "INFORMATICA");
			dataView.getAttributes().put(ExamAttr.QUALIFICATION.label, "DOTTORE");
			dataView.getAttributes().put(ExamAttr.HONOUR.label, Boolean.TRUE);
			dataView.getAttributes().put(ExamAttr.GRADE.label, "110/110");
			dataView.getAttributes().put(ExamAttr.RESULT.label, Boolean.TRUE);
			dataView.getAttributes().put(ExamAttr.EXTCANDIDATE.label, Boolean.FALSE);
			exp.setViews(ImmutableMap.of("view1", dataView));
			
			exp = experienceRepository.save(exp).block();
			
			ExpCompetence expComp = new ExpCompetence(p.getId(), exp.getId(), c.getId());
			expCompetenceRepository.save(expComp).block();
		}
	}
}
