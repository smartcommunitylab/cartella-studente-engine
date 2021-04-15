package it.smartcommunitylab.csengine.graphql.fetcher;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import it.smartcommunitylab.csengine.model.Person;
import it.smartcommunitylab.csengine.model.dto.PersonDTO;
import it.smartcommunitylab.csengine.repository.PersonRepository;

@Component
public class GraphQLPersonDataFetcher {
	@Autowired
	PersonRepository personRepository;
	
	public DataFetcher<Stream<PersonDTO>> searchPersonByFiscalCode() {
		return dataFetchingEnvironment -> {
			String text = dataFetchingEnvironment.getArgument("text");
			return personRepository.findAllByFiscalCodeRegex(text)
					.map(this::getPersonDTO)
					.toStream();
		};
	}
	
	private PersonDTO getPersonDTO(Person p) {
		return new PersonDTO(p);
	}

}
