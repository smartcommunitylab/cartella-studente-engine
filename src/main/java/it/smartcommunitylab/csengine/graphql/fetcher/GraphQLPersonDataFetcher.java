package it.smartcommunitylab.csengine.graphql.fetcher;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetcher;
import it.smartcommunitylab.csengine.common.EntityType;
import it.smartcommunitylab.csengine.common.PersonAttr;
import it.smartcommunitylab.csengine.model.Address;
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

	public DataFetcher<Address> getPersonAddress() {
		return dataFetchingEnvironment -> {
			PersonDTO p = dataFetchingEnvironment.getSource();
			return p.getAddress();
		};
	}

	private PersonDTO getPersonDTO(Person p) {
		PersonDTO dto = new PersonDTO();
		dto.setId(p.getId());
		dto.setFiscalCode((String) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.fiscalCode.label));
		dto.setName((String) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.name.label));
		dto.setSurname((String) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.surname.label));
		dto.setAddress((Address) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.address.label));
		dto.setEmail((String) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.email.label));
		dto.setPhone((String) p.getViews().get(EntityType.person.label).getAttributes().get(PersonAttr.phone.label));
		return dto;
	}

}
