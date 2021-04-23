package it.smartcommunitylab.csengine.connector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConnectorManager {
	@Autowired
	private ApplicationContext context;
	
	List<ConnectorConf> experiencesConfs;
	Map<String, ExperienceConnector> experiencesMaps = new HashMap<>();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initExpServices(String file) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:" + file);
		experiencesConfs = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<ConnectorConf>>(){});
		experiencesConfs.forEach(conf -> {
			try {
				Class c = Class.forName(conf.getImplementor());
				ExperienceConnector connector = (ExperienceConnector) context.getBean(c);
				connector.setView(conf.getView());
				experiencesMaps.put(conf.getView(), connector);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});		
	}
	
	public ExperienceConnector getExpService(String view) {
		return experiencesMaps.get(view);
	}
	
	public ConnectorConf getExpConnector(String entityType, int priority) {
		for(ConnectorConf conf : experiencesConfs) {
			if(conf.getEntityType().equals(entityType) && (conf.getPriority() == priority)) {
				return conf;
			}
		}
		return null;
	}
	
}
