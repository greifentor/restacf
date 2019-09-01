package de.ollie.library.rest.v1.converter;

import org.springframework.stereotype.Component;

import de.ollie.library.rest.v1.dto.RackDTO;
import de.ollie.library.service.so.RackSO;

/**
 * A converter for rack DTO's.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class RackDTOConverter {

	public RackDTO convertSOToDTO(RackSO so) {
		if (so == null) {
			return null;
		}
		return new RackDTO().setId(so.getId()).setName(so.getName());
	}

	public RackSO convertDTOToSO(RackDTO dto) {
		if (dto == null) {
			return null;
		}
		return new RackSO().setId(dto.getId()).setName(dto.getName());
	}

}