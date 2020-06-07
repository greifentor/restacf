package de.ollie.library.service.persistence.port;

import java.util.List;
import java.util.Optional;

import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.so.BookSO;

/**
 * An interface for book persistence ports.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public interface BookPersistencePort {

	boolean delete(long id) throws PersistenceException;

	List<BookSO> findAll() throws PersistenceException;

	Optional<BookSO> findById(long id) throws PersistenceException;

	long save(BookSO so) throws PersistenceException;

	List<BookSO> findBooksForRack(long rackId) throws PersistenceException;

}