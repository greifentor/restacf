package de.ollie.library.service.persistence.port;

import java.util.Optional;

import de.ollie.library.service.so.RackSO;

/**
 * An interface for rack persistence ports.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public interface RackPersistencePort {

	Optional<RackSO> findById(long id);

}