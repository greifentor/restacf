package de.ollie.library.persistence.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import de.ollie.library.persistence.dbo.BookDBO;


/**
 * A repository for book access.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Repository
public interface BookRepository extends CrudRepository<BookDBO, Long> {
}