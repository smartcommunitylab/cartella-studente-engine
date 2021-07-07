package it.smartcommunitylab.csengine.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import it.smartcommunitylab.csengine.common.CertificationAttr;
import it.smartcommunitylab.csengine.common.ExpAttr;
import it.smartcommunitylab.csengine.common.StageAttr;
import it.smartcommunitylab.csengine.graphql.fetcher.GraphQLExperienceDataFetcher;
import it.smartcommunitylab.csengine.graphql.fetcher.GraphQLPersonDataFetcher;

@Component
public class GraphQLProvider {
	private GraphQL graphQL;
	
	@Autowired
	GraphQLPersonDataFetcher personDataFetcher;
	@Autowired
	GraphQLExperienceDataFetcher experienceDataFetcher;
	
	@Bean
  public GraphQL graphQL() { 
      return graphQL;
  }

  @PostConstruct
  public void init() throws IOException {
      URL url = Resources.getResource("schema.graphqls");
      String sdl = Resources.toString(url, Charsets.UTF_8);
      GraphQLSchema graphQLSchema = buildSchema(sdl);
      this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);  	
  }
  
  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
    		.type(newTypeWiring("Query")
		    		.dataFetcher("personByFiscalCode", personDataFetcher.searchPersonByFiscalCode())
						.dataFetcher("getExp", experienceDataFetcher.searchExpByPersonId())
		    		.dataFetcher("getExam", experienceDataFetcher.searchExamByPersonId())
						.dataFetcher("getStage", experienceDataFetcher.searchStageByPersonId())
						.dataFetcher("getCertification", experienceDataFetcher.searchCertificationByPersonId())
						.dataFetcher("getMobility", experienceDataFetcher.searchMobilityByPersonId())
						.dataFetcher("getEnrollment", experienceDataFetcher.searchEnrollmentByPersonId())
		    )
		    .type(newTypeWiring("Person")
		    		.dataFetcher("address", personDataFetcher.getPersonAddress())
		    )
		    .type(newTypeWiring("Exp")
		    		.dataFetcher("organisation", experienceDataFetcher.getOrganisation())
		    		.dataFetcher("competences", experienceDataFetcher.getCompetences())
						.dataFetcher("location", experienceDataFetcher.getGeoPoint(ExpAttr.location.label))
		    )    		
		    .type(newTypeWiring("Exam")
		    		.dataFetcher("organisation", experienceDataFetcher.getOrganisation())
		    		.dataFetcher("competences", experienceDataFetcher.getCompetences())
		    )    		
		    .type(newTypeWiring("Stage")
		    		.dataFetcher("organisation", experienceDataFetcher.getOrganisation())
		    		.dataFetcher("competences", experienceDataFetcher.getCompetences())
						.dataFetcher("address", experienceDataFetcher.getAddress(StageAttr.address.label))
		    )    		
		    .type(newTypeWiring("Certification")
		    		.dataFetcher("organisation", experienceDataFetcher.getOrganisation())
		    		.dataFetcher("competences", experienceDataFetcher.getCompetences())
						.dataFetcher("address", experienceDataFetcher.getAddress(CertificationAttr.address.label))
		    )    		
		    .type(newTypeWiring("Enrollment")
		    		.dataFetcher("organisation", experienceDataFetcher.getOrganisation())
		    		.dataFetcher("competences", experienceDataFetcher.getCompetences())
		    )    		
		    .type(newTypeWiring("Organisation")
		    		.dataFetcher("address", experienceDataFetcher.getOrganisationAddress())
		    )
		    .build();
  }
  
}
