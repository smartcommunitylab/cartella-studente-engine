package it.smartcommunitylab.csengine.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.csengine.common.EntityType;

@Component
public class ConnectorManager {
	@Autowired
	private ApplicationContext context;
	
	List<ConnectorConf> servicesConfs;
	Map<String, ExperienceConnector> experienceMap = new HashMap<>();
	Map<String, PersonConnector> personMap = new HashMap<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostConstruct
	public void initServices() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:connectors.json");
		servicesConfs = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<ConnectorConf>>(){});
		for(ConnectorConf conf : servicesConfs) {
			try {
				Class c = Class.forName(conf.getImplementor());
				if(EntityType.person.label.equals(conf.getEntityType())) {
					PersonConnector connector = (PersonConnector) context.getBean(c);
					connector.setView(conf.getView());
					personMap.put(conf.getView(), connector);
				} else {
					ExperienceConnector connector = (ExperienceConnector) context.getBean(c);
					connector.setView(conf.getView());
					experienceMap.put(conf.getView(), connector);					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		System.out.println("initServices");
	}
	
	public PersonConnector getPersonService(String view) {
		return personMap.get(view);
	}
	
	public ExperienceConnector getExpService(String view) {
		return experienceMap.get(view);
	}
	
	public ConnectorConf getExpConnector(String entityType, int priority) {
		for(ConnectorConf conf : servicesConfs) {
			if(conf.getEntityType().equals(entityType) && (conf.getPriority() == priority)) {
				return conf;
			}
		}
		return null;
	}
	
}
