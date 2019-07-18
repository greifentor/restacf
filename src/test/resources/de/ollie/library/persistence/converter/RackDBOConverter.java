package de.ollie.library.persistence.converter;

import org.springframework.stereotype.Component;

import de.ollie.library.persistence.dbo.RackDBO;
import de.ollie.library.service.so.RackSO;

/**
 * A converter for rack DBO's.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Component
public class RackDBOConverter {

	public RackSO convertDBOToSO(RackDBO dbo) {
		if (dbo == null) {
			return null;
		}
		return new RackSO().setId(dbo.getId()).setName(dbo.getName());
	}

}