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
 * @author ollie
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Service
public class RackRDBMSPersistenceAdapter implements RackPersistencePort {

	private final RackDBOConverter rackConverter;
	private final RackRepository rackRepository;

	public RackRDBMSPersistenceAdapter(RackDBOConverter rackConverter, RackRepository rackRepository) {
		super();
		this.rackConverter = rackConverter;
		this.rackRepository = rackRepository;
	}

	@Override
	public Optional<RackSO> findById(long id) {
		Optional<RackDBO> dbo = this.rackRepository.findById(id);
		if (dbo.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(this.rackConverter.convertDBOToSO(dbo.get()));
	}

}