package de.ollie.library.persistence.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.persistence.converter.RackDBOConverter;
import de.ollie.library.persistence.dbo.RackDBO;
import de.ollie.library.persistence.repository.RackRepository;
import de.ollie.library.service.persistence.exception.PersistenceException;
import de.ollie.library.service.persistence.port.RackPersistencePort;
import de.ollie.library.service.so.RackSO;

/**
 * An implementation of the rack persistence port interface for RDBMS.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Service
public class RackRDBMSPersistenceAdapter implements RackPersistencePort {

	private final RackDBOConverter rackDBOConverter;
	private final RackRepository rackRepository;

	public RackRDBMSPersistenceAdapter(RackDBOConverter rackDBOConverter, RackRepository rackRepository) {
		super();
		this.rackDBOConverter = rackDBOConverter;
		this.rackRepository = rackRepository;
	}

	@Override
	public boolean delete(long id) throws PersistenceException {
		boolean result = false;
		try {
			Optional<RackDBO> dbo = this.rackRepository.findById(id);
			if (dbo.isPresent()) {
				this.rackRepository.delete(dbo.get());
				result = true;
			}
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.WriteError, "error while deleting rack with id: " + id, e);
		}
		return result;
	}

	@Override
	public List<RackSO> findAll() throws PersistenceException {
		try {
			List<RackSO> sos = new ArrayList<>();
			for (RackDBO dbo : this.rackRepository.findAll()) {
				sos.add(this.rackDBOConverter.convertDBOToSO(dbo));
			}
			return sos;
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.ReadError, "error while finding all racks.", e);
		}
	}

	@Override
	public Optional<RackSO> findById(long id) throws PersistenceException {
		try {
			Optional<RackDBO> dbo = this.rackRepository.findById(id);
			if (dbo.isEmpty()) {
				return Optional.empty();
			}
			return Optional.of(this.rackDBOConverter.convertDBOToSO(dbo.get()));
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.ReadError, "error while finding by id: " + id, e);
		}
	}

	@Override
	public long save(RackSO so) throws PersistenceException {
		try {
			RackDBO dbo = this.rackDBOConverter.convertSOToDBO(so);
			return this.rackRepository.save(dbo).getId();
		} catch (Exception e) {
			throw new PersistenceException(PersistenceException.Type.WriteError, "error while saving: " + so, e);
		}
	}

}