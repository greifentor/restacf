package de.ollie.library.persistence.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.persistence.converter.BookDBOConverter;
import de.ollie.library.persistence.dbo.BookDBO;
import de.ollie.library.persistence.repository.BookRepository;
import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.persistence.port.BookPersistencePort;
import de.ollie.library.service.so.BookSO;

/**
 * An implementation of the book persistence port interface for RDBMS.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Service
public class BookRDBMSPersistenceAdapter implements BookPersistencePort {

	private final BookDBOConverter bookDBOConverter;
	private final BookRepository bookRepository;

	public BookRDBMSPersistenceAdapter(BookDBOConverter bookDBOConverter, BookRepository bookRepository) {
		super();
		this.bookDBOConverter = bookDBOConverter;
		this.bookRepository = bookRepository;
	}

	@Override
	public boolean delete(long id) throws PersistenceException {
		boolean result = false;
		try {
			Optional<BookDBO> dbo = this.bookRepository.findById(id);
			if (dbo.isPresent()) {
				this.bookRepository.delete(dbo.get());
				result = true;
			}
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.WriteError,
					"error while deleting book with id: " + id, e);
		}
		return result;
	}

	@Override
	public List<BookSO> findAll() throws PersistenceException {
		try {
			List<BookSO> sos = new ArrayList<>();
			for (BookDBO dbo : this.bookRepository.findAll()) {
				sos.add(this.bookDBOConverter.convertDBOToSO(dbo));
			}
			return sos;
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.ReadError, "error while finding all books.", e);
		}
	}

	@Override
	public Optional<BookSO> findById(long id) throws PersistenceException {
		try {
			Optional<BookDBO> dbo = this.bookRepository.findById(id);
			if (dbo.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(this.bookDBOConverter.convertDBOToSO(dbo.get()));
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.ReadError, "error while finding by id: " + id, e);
		}
	}

	@Override
	public void save(BookSO so) throws PersistenceException {
		try {
			BookDBO dbo = this.bookDBOConverter.convertSOToDBO(so);
			this.bookRepository.save(dbo);
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.WriteError, "error while saving: " + so, e);
		}
	}

	@Override
	public List<BookSO> findBooksForRack(long rackId) throws PersistenceException {
		try {
			List<BookSO> sos = new ArrayList<>();
			for (BookDBO dbo : this.bookRepository.findBooksForRack(rackId)) {
				sos.add(this.bookDBOConverter.convertDBOToSO(dbo));
			}
			return sos;
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.ReadError, "error while finding all books for rack:" + rackId, e);
		}
	}

}