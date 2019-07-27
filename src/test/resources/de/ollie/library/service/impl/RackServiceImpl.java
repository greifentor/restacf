package de.ollie.library.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.service.RackService;
import de.ollie.library.service.persistence.port.RackPersistencePort;
import de.ollie.library.service.so.RackSO;

/**
 * An implementation of the rack service interface.
 *
 * @author ollie
 *
 */
@Service
public class RackServiceImpl implements RackService {

	private final RackPersistencePort rackPersistencePort;

	public RackServiceImpl(RackPersistencePort rackPersistencePort) {
		super();
		this.rackPersistencePort = rackPersistencePort;
	}

	@Override
	public Optional<RackSO> findById(long id) {
		return this.rackPersistencePort.findById(id);
	}

}