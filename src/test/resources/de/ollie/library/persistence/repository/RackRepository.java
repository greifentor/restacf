package de.ollie.library.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.ollie.library.persistence.dbo.RackDBO;

/**
 * A CRUD repository for rack access.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Repository
public interface RackRepository extends CrudRepository<RackDBO, Long> {
}