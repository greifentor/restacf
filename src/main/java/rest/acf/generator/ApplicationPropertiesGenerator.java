package rest.acf.generator;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;

/**
 * A generator for an initial "application.properties" file.
 * 
 * @author ollie
 *
 */
public class ApplicationPropertiesGenerator {

	private static final Logger LOG = Logger.getLogger(ApplicationPropertiesGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;

	/**
	 * Create a new application properties file generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 */
	public ApplicationPropertiesGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
	}

	/**
	 * Generates a application properties file for the passed database service object.
	 * 
	 * @param databaseSO The database service object which the class is to create for.
	 * @returns A application properties file for passed database or a "null" value if a "null" value is passed.
	 */
	public String generate(DatabaseSO databaseSO) {
		NameConverter nameConverter = new NameConverter();
		String code = "app.version=@project.version@\n" //
				+ "\n" //
				+ "spring.liquibase.change-log=classpath:db/change-log/change-log-master.xml\n" //
				+ "\n" //
				+ "spring.jpa.hibernate.ddl-auto=update\n" //
				+ "\n" //
				+ "logging.level.root=INFO\n" //
				+ "\n" //
				+ "spring.datasource.url=jdbc:hsqldb:mem:" + nameConverter.classNameToAttrName(databaseSO.getName())
				+ "\n" //
				+ "spring.datasource.driverClassName=org.hsqldb.jdbc.JDBCDriver\n" //
				+ "spring.datasource.username=sa\n" //
				+ "spring.datasource.password=";
		return code;
	}

}