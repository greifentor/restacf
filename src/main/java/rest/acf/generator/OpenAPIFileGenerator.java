package rest.acf.generator;

import static de.ollie.utils.Check.ensure;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import rest.acf.generator.converter.DatabaseSOToOpenAPIModelConverter;
import rest.acf.generator.openapi.OpenAPIModel;

/**
 * A generator for an OpenAPI file of the application.
 *
 * @author ollie (17.11.2019)
 */
public class OpenAPIFileGenerator {

	private static final DatabaseSOToOpenAPIModelConverter converter = new DatabaseSOToOpenAPIModelConverter();

	/**
	 * Generates a OpenAPI file content for the passed database service object.
	 * 
	 * @param database   The database service object which the class is to create for.
	 * @param authorName The name of the author.
	 * @returns A OpenAPI file content for passed database.
	 * @throws IllegalArgumentException Passing a null value.
	 */
	public String generate(DatabaseSO database, String authorName) {
		ensure(database != null, "database cannot be null.");
		OpenAPIModel openAPI = converter.convert(database);
		Yaml yaml = new Yaml(new Representer() {
			@Override
			protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue,
					Tag customTag) {
				// if value of property is null, ignore it.
				if (propertyValue == null) {
					return null;
				}
				return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
			}
		});
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.dumpAsMap(openAPI);
	}

}