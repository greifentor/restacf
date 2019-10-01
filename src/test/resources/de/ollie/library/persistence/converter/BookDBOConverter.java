package de.ollie.library.persistence.converter;

import org.springframework.stereotype.Component;

import de.ollie.library.persistence.dbo.BookDBO;
import de.ollie.library.service.so.BookSO;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * A converter for book DBO's.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class BookDBOConverter {

	@Autowired
	private RackDBOConverter rackDBOConverter;

	public BookSO convertDBOToSO(BookDBO dbo) {
		if (dbo == null) {
			return null;
		}
		return new BookSO().setId(dbo.getId()).setRack(this.rackDBOConverter.convertDBOToSO(dbo.getRack())).setReferenceLibrary(dbo.getReferenceLibrary()).setTitle(dbo.getTitle());
	}

	public BookDBO convertSOToDBO(BookSO so) {
		if (so == null) {
			return null;
		}
		return new BookDBO().setId(so.getId()).setRack(this.rackDBOConverter.convertSOToDBO(so.getRack())).setReferenceLibrary(so.getReferenceLibrary()).setTitle(so.getTitle());
	}

}