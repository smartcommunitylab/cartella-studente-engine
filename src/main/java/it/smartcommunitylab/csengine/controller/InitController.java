package it.smartcommunitylab.csengine.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.ExamAttr;
import it.smartcommunitylab.csengine.connector.saa.SAAExam;
import it.smartcommunitylab.csengine.connector.saa.SAAStage;
import it.smartcommunitylab.csengine.connector.saa.SAAStudent;
import it.smartcommunitylab.csengine.model.Competence;
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
	Random random = new Random();
	int min = 10;
	int max = 31;
	
	@GetMapping("/saa/student")
	public SAAStudent getSAAStudent(@RequestParam String fiscalCode) {
		String id = RandomStringUtils.randomNumeric(8);
		SAAStudent s = new SAAStudent();
		s.setOrigin("INFOTNISTRUZIONE");
		s.setExtId(id);
		s.setCf(fiscalCode);
		s.setName("Nome" + id);
		s.setSurname("Cognome" + id);
		s.setEmail("email" + id);
		return s;
	}
	
	@GetMapping("/saa/exam")
	public List<SAAExam> getSAAExam(@RequestParam String fiscalCode) {
		List<SAAExam> list = new ArrayList<>();
		for(int i=0; i<2; i++) {
			String id = fiscalCode + "_exam_" + i;
			int giorno = random.nextInt(max - min) + min;
			SAAExam e = new SAAExam();
			e.setOrigin("INFOTNISTRUZIONE");
			e.setExtId(id);
			e.setDateFrom("2021-04-" + giorno);
			e.setDateTo("2021-04-" + giorno);
			e.setQualification("qualifica" + id);
			e.setSchoolYear("2020-21");
			e.setType("ESAME DI STATO CONCLUSIVO DEL PRIMO CICLO");
			list.add(e);
		}
		return list;
	}
	
	@GetMapping("/saa/stage")
	public List<SAAStage> getSAAStage(@RequestParam String fiscalCode) {
		List<SAAStage> list = new ArrayList<>();
		SAAStage s = new SAAStage();
		String id = fiscalCode + "_stage_1";
		int giorno = random.nextInt(max - min) + min;
		s.setExtId(id);
		s.setOrigin("INFOTNISTRUZIONE");
		s.setDateFrom("2021-04-" + giorno);
		s.setDateTo("2021-04-" + giorno);
		s.setType("Terzo anno");
		s.setTitle("3° OP. CARPENTERIA IN LEGNO - PRIMO PERIODO");
		s.setDuration("80");
		s.setLocation("EFFEFFE RESTAURI srl - LOCALITA' AL PONTE  38082 BORGO CHIESE (TN)");
		list.add(s);
		return list;
	}
	
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
		for(int i = 0; i < 3; i++) {
			Experience exp = new Experience();
			exp.setPersonId(p.getId());
			exp.setOrganisationId(o.getId());
			exp.setTitle("title" + i);
			exp.setEntityType(EntityType.EXAM.label);
			int giorno = random.nextInt(max - min) + min;
			exp.setDateFrom(LocalDate.parse("2021-04-" + giorno, dtf));
			exp.setDateTo(LocalDate.parse("2021-04-" + giorno, dtf));
			exp.setAttributes(new HashMap<>());
			exp.getAttributes().put(ExamAttr.TYPE.label, "INFORMATICA");
			exp.getAttributes().put(ExamAttr.QUALIFICATION.label, "DOTTORE");
			exp.getAttributes().put(ExamAttr.HONOUR.label, Boolean.TRUE);
			exp.getAttributes().put(ExamAttr.GRADE.label, "110/110");
			exp.getAttributes().put(ExamAttr.RESULT.label, Boolean.TRUE);
			exp.getAttributes().put(ExamAttr.EXTCANDIDATE.label, Boolean.FALSE);			
			exp = experienceRepository.save(exp).block();
			
			ExpCompetence expComp = new ExpCompetence(p.getId(), exp.getId(), c.getId());
			expCompetenceRepository.save(expComp).block();
		}
	}
}
