package de.ollie.library.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.ollie.library.persistence.dbo.BookDBO;

/**
 * A CRUD repository for book access.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Repository
public interface BookRepository extends CrudRepository<BookDBO, Long> {

	@Query("SELECT b FROM Book b WHERE b.rackId.id=?1")
	List<BookDBO> findBooksForRack(long rackId);

}