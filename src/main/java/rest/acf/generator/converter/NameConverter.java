package rest.acf.generator.converter;

import org.apache.commons.lang3.StringUtils;

import de.ollie.archimedes.alexandrian.service.TableSO;

/**
 * A converter for table names.
 *
 * @author Oliver.Lieshoff
 *
 */
public class NameConverter {

	/**
	 * Converts the name of the passed table service object into a database
	 * class name.
	 * 
	 * @param tableSO
	 *            The table service object whose name is to convert into a
	 *            database class name.
	 * @return The database class name for the passed table service object.
	 *         Passing a "null" value delivers a "null" value also.
	 */
	public String tableNameToDBOClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		String tableName = tableSO.getName();
		// TODO: ensure(!tableName.isEmpty, "table name cannot be empty.");
		if (startsWithLowerCaseCharacter(tableName)) {
			tableName = firstCharToUpperCase(tableName);
		}
		return tableName + "DBO";
	}

	private boolean startsWithLowerCaseCharacter(String s) {
		String firstChar = StringUtils.left(s, 1);
		return firstChar.equals(firstChar.toLowerCase());
	}

	private String firstCharToUpperCase(String s) {
		return StringUtils.left(s, 1).toUpperCase()
				+ (s.length() > 1 ? s.substring(1) : "");
	}

}