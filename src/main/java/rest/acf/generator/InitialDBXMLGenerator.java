package rest.acf.generator;

import java.util.List;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;

/**
 * A generator for an initial Liquibase DB update script.
 *
 * @author ollie
 *
 */
public class InitialDBXMLGenerator {

	private static final Logger LOG = Logger.getLogger(InitialDBXMLGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;

	/**
	 * Creates a new Liquibase DB update script file generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 */
	public InitialDBXMLGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
	}

	/**
	 * Generates a Liquibase DB update script file for the passed database service object.
	 * 
	 * @param databaseSO The database service object which the class is to create for.
	 * @param authorName The name of the author.
	 * @returns A Liquibase DB update script file for passed database or a "null" value if a "null" value is passed.
	 */
	public String generate(DatabaseSO databaseSO, String authorName) {
		NameConverter nameConverter = new NameConverter();
		String code = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" //
				+ "<databaseChangeLog \n" //
				+ "		xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog/1.9\" \n" //
				+ "		xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" //
				+ "		xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog/1.9\n" //
				+ "				http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.9.xsd\">\n";
		int i = 1;
		for (SchemeSO schemeSO : databaseSO.getSchemes()) {
			for (TableSO tableSO : schemeSO.getTables()) {
				code += "	<changeSet author=\"" + authorName + "\" id=\"" + getChangeSetNumber(i++) + "\">\n" //
						+ "		<createTable tableName=\"" + tableSO.getName() + "\">\n" //
						+ getColumnsXML(tableSO.getColumns()) //
						+ "		</createTable>\n" //
						+ "	</changeSet>\n";
			}
		}
		return code + "</databaseChangeLog>";
	}

	private String getChangeSetNumber(int i) {
		String s = String.valueOf(i);
		while (s.length() < 3) {
			s = "0" + s;
		}
		return "initial-db-" + s;
	}

	private String getColumnsXML(List<ColumnSO> columns) {
		String code = "";
		for (ColumnSO columnSO : columns) {
			code += "			<column name=\"" + columnSO.getName() //
					+ "\" type=\"" + columnSO.getType().toString() + "\">\n" //
					+ "				<constraints nullable=\"" + columnSO.isNullable() //
					+ "\" primaryKey=\"" + columnSO.isPkMember() + "\"/>\n" //
					+ "			</column>\n";
		}
		return code;
	}

}