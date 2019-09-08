package de.ollie.library.service;

import java.util.List;
import java.util.Optional;

import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.so.RackSO;
import de.ollie.library.service.so.ResultPageSO;

/**
 * An interface for a rack service.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public interface RackService {

	boolean delete(long id) throws PersistenceException;

	ResultPageSO<RackSO> findAll() throws PersistenceException;

	Optional<RackSO> findById(long id) throws PersistenceException;

	void save(RackSO rack) throws PersistenceException;

}