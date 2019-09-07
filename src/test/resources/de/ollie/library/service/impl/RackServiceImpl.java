package de.ollie.library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.service.RackService;
import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.persistence.port.RackPersistencePort;
import de.ollie.library.service.so.RackSO;

/**
 * An implementation of the rack service interface.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Service
public class RackServiceImpl implements RackService {

	private final RackPersistencePort rackPersistencePort;

	public RackServiceImpl(RackPersistencePort rackPersistencePort) {
		super();
		this.rackPersistencePort = rackPersistencePort;
	}

	@Override
	public boolean delete(long id) throws PersistenceException {
		return this.rackPersistencePort.delete(id);
	}

	@Override
	public List<RackSO> findAll() throws PersistenceException {
		return this.rackPersistencePort.findAll();
	}

	@Override
	public Optional<RackSO> findById(long id) throws PersistenceException {
		return this.rackPersistencePort.findById(id);
	}

	@Override
	public void save(RackSO rack) throws PersistenceException {
		this.rackPersistencePort.save(rack);
	}

}