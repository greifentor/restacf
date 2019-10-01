package de.ollie.library.rest.v1.converter;

import org.springframework.stereotype.Component;

import de.ollie.library.rest.v1.dto.BookDTO;
import de.ollie.library.service.so.BookSO;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A converter for book DTO's.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class BookDTOConverter {

	@Autowired
	private RackDTOConverter rackDTOConverter;

	public BookDTO convertSOToDTO(BookSO so) {
		if (so == null) {
			return null;
		}
		return new BookDTO().setId(so.getId()).setRack(this.rackDTOConverter.convertSOToDTO(so.getRack())).setReferenceLibrary(so.getReferenceLibrary()).setTitle(so.getTitle());
	}

	public BookSO convertDTOToSO(BookDTO dto) {
		if (dto == null) {
			return null;
		}
		return new BookSO().setId(dto.getId()).setRack(this.rackDTOConverter.convertDTOToSO(dto.getRack())).setReferenceLibrary(dto.getReferenceLibrary()).setTitle(dto.getTitle());
	}

}