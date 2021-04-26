package it.smartcommunitylab.csengine.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.smartcommunitylab.csengine.connector.edit.EditStage;
import it.smartcommunitylab.csengine.connector.saa.SAACompetence;
import it.smartcommunitylab.csengine.connector.saa.SAAExam;
import it.smartcommunitylab.csengine.connector.saa.SAAInstitute;
import it.smartcommunitylab.csengine.connector.saa.SAAStage;
import it.smartcommunitylab.csengine.connector.saa.SAAStudent;
import it.smartcommunitylab.csengine.repository.ExperienceRepository;
import it.smartcommunitylab.csengine.repository.PersonRepository;

@RestController
public class TestController {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	ExperienceRepository experienceRepository;
	
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
		s.setName("Nome " + id);
		s.setSurname("Cognome " + id);
		s.setEmail("email " + id);
		s.setPhone("telefono " + id);
		s.setAddress("indirizzo " + id);
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
			e.setQualification("qualifica");
			e.setSchoolYear("2020-21");
			e.setType("ESAME DI STATO CONCLUSIVO DEL PRIMO CICLO");
			e.setInstituteRef("istituto1");
			SAACompetence comp = new SAACompetence();
			comp.setUri("http://data.europa.eu/esco/skill/09638218-695c-44c7-bac3-26b45a2ae418");
			comp.setConcentType("KnowledgeSkillCompetence");
			comp.getPreferredLabel().put("it", "svolgere i patch test");
			comp.getPreferredLabel().put("en", "conduct patch testing");
			e.getCompetences().add(comp);
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
	
	@GetMapping("/edit/stage")
	public List<EditStage> getEditStage(@RequestParam String fiscalCode) {
		List<EditStage> list = new ArrayList<>();
		EditStage s = new EditStage();
		String id = RandomStringUtils.randomNumeric(8);
		String saaId = fiscalCode + "_stage_1";
		int giorno = random.nextInt(max - min) + min;
		s.setExtId(id);
		s.setOrigin("INFOTNISTRUZIONE");
		s.setSaaId(saaId);
		s.setDateFrom("2021-04-" + giorno);
		s.setDateTo("2021-04-" + giorno);
		s.setType("Terzo anno");
		s.setTitle("3° OP. CARPENTERIA IN LEGNO - PRIMO PERIODO");
		s.setDuration("70");
		s.setLocation("EFFEFFE RESTAURI srl - LOCALITA' AL PONTE  38082 BORGO CHIESE (TN)");
		list.add(s);
		return list;
	}
	
	@GetMapping("/saa/institute")
	public SAAInstitute getSAAInstitute(@RequestParam String extId) {
		SAAInstitute i = new SAAInstitute();
		i.setExtId(extId);
		i.setOrigin("INFOTNISTRUZIONE");
		i.setName("istituto1");
		i.setAddress("indirizzo1");
		i.setEmail("email1");
		i.setPec("pec1");
		return i;
	}
	
}
