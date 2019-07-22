package de.ollie.library.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.ollie.library.persistence.converter.RackDBOConverter;
import de.ollie.library.persistence.dbo.RackDBO;
import de.ollie.library.persistence.repository.RackRepository;
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
	public Optional<RackSO> findById(long id) {
		Optional<RackDBO> dbo = this.rackRepository.findById(id);
		if (dbo.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(this.rackDBOConverter.convertDBOToSO(dbo.get()));
	}

}