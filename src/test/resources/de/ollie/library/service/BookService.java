package de.ollie.library.service;

import java.util.Optional;

import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.so.BookSO;
import de.ollie.library.service.so.ResultPageSO;

/**
 * An interface for a book service.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public interface BookService {

	boolean delete(long id) throws PersistenceException;

	ResultPageSO<BookSO> findAll() throws PersistenceException;

	Optional<BookSO> findById(long id) throws PersistenceException;

	long save(BookSO book) throws PersistenceException;

	ResultPageSO<BookSO> findBooksForRack(long rackId) throws PersistenceException;

}