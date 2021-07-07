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
	
	@GetMapping("/edit/stage")
	public List<EditStage> getEditStage(@RequestParam String fiscalCode) {
		List<EditStage> list = new ArrayList<>();
		EditStage s = new EditStage();
		String id = RandomStringUtils.randomNumeric(8);
		String saaId = "stage111";
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
	
}
