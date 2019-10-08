package de.ollie.library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.service.BookService;
import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.persistence.port.BookPersistencePort;
import de.ollie.library.service.so.BookSO;
import de.ollie.library.service.so.ResultPageSO;

/**
 * An implementation of the book service interface.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Service
public class BookServiceImpl implements BookService {

	private final BookPersistencePort bookPersistencePort;

	public BookServiceImpl(BookPersistencePort bookPersistencePort) {
		super();
		this.bookPersistencePort = bookPersistencePort;
	}

	@Override
	public boolean delete(long id) throws PersistenceException {
		return this.bookPersistencePort.delete(id);
	}

	@Override
	public ResultPageSO<BookSO> findAll() throws PersistenceException {
		List<BookSO> l = this.bookPersistencePort.findAll();
		return new ResultPageSO<BookSO>().setCurrentPage(0).setResultsPerPage(l.size()).setResults(l).setTotalResults(l.size());
	}

	@Override
	public Optional<BookSO> findById(long id) throws PersistenceException {
		return this.bookPersistencePort.findById(id);
	}

	@Override
	public void save(BookSO book) throws PersistenceException {
		this.bookPersistencePort.save(book);
	}

	@Override
	public ResultPageSO<BookSO> findBooksForRack(long rackId) throws PersistenceException {
		List<BookSO> l = this.bookPersistencePort.findBooksForRack(rackId);
		return new ResultPageSO<BookSO>().setCurrentPage(0).setResultsPerPage(l.size()).setResults(l).setTotalResults(l.size());
	}

}