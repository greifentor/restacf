package rest.acf.generator.converter;

import static de.ollie.utils.Check.ensure;

import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import rest.acf.generator.openapi.OpenAPIModel;

/**
 * A converter for database service object to OpenAPI model.
 *
 * @author ollie (17.11.2019)
 */
public class DatabaseSOToOpenAPIModelConverter {

	public OpenAPIModel convert(DatabaseSO database) {
		ensure(database != null, "database cannot be null.");
		return new OpenAPIModel() //
				.setTitle(database.getName());
	}

}