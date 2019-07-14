package de.ollie.library.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * A persistence port implementation for book objects.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class BookPersistenceAdapter {

	private final BookRepository bookRepository;
	private  final BookSOConverter bookSOConverter;
	
	/**
	 * Creates a new persistence adapter for books.
	 * 
	 * @param bookConverter A converter for book objects.
	 * @param bookRepository A repository for books.
	 */
	public BookPersistenceAdapter(BookSOConverter bookSOConverter, BookRepository bookRepository) {
		super();
		this.bookSOConverter = bookSOConverter;
		this.bookRepository = bookRepository;
	}

	public Optional<BookSO> findById(long id) {
		return this.bookSOConverter.toOptionalSO(this.bookRepository.findById(id));
	}

}