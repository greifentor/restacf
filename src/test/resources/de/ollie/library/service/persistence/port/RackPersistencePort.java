package de.ollie.library.service.persistence.port;

import java.util.List;
import java.util.Optional;

import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.so.RackSO;

/**
 * An interface for rack persistence ports.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public interface RackPersistencePort {

	boolean delete(long id) throws PersistenceException;

	List<RackSO> findAll() throws PersistenceException;

	Optional<RackSO> findById(long id) throws PersistenceException;

	long save(RackSO so) throws PersistenceException;

}