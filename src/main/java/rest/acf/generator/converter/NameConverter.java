package rest.acf.generator.converter;

import static de.ollie.utils.Check.ensure;

import org.apache.commons.lang3.StringUtils;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;

/**
 * A converter for table names.
 *
 * @author Oliver.Lieshoff
 *
 */
public class NameConverter {

	/**
	 * Converts a column name into a Java attribute name.
	 * 
	 * @param columnSO The column service object whose name is to convert.
	 * @return A Java attribute name based on the name of the passed column service object, or a "null" value, if a
	 *         "null" value is passed.
	 */
	public String columnNameToAttributeName(ColumnSO columnSO) {
		if (columnSO == null) {
			return null;
		}
		String columnName = columnSO.getName();
		ensure(!columnName.isEmpty(), "column name cannot be empty.");
		if (containsUnderScores(columnName)) {
			columnName = buildTableNameFromUnderScoreString(columnName);
		} else if (allCharactersAreUpperCase(columnName)) {
			columnName = columnName.toLowerCase();
		}
		if (startsWithUpperCaseCharacter(columnName)) {
			columnName = firstCharToLowerCase(columnName);
		}
		return columnName;
	}

	private boolean allCharactersAreUpperCase(String s) {
		return s.equals(s.toUpperCase());
	}

	private boolean startsWithUpperCaseCharacter(String s) {
		String firstChar = StringUtils.left(s, 1);
		return firstChar.equals(firstChar.toUpperCase());
	}

	private String firstCharToLowerCase(String s) {
		return StringUtils.left(s, 1).toLowerCase() + (s.length() > 1 ? s.substring(1) : "");
	}

	/**
	 * Converts the name of the passed table service object into a database class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a database class name.
	 * @return The database class name for the passed table service object. Passing a "null" value delivers a "null"
	 *         value also.
	 */
	public String tableNameToDBOClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		String tableName = tableSO.getName();
		ensure(!tableName.isEmpty(), "table name cannot be empty.");
		if (containsUnderScores(tableName)) {
			tableName = buildTableNameFromUnderScoreString(tableName);
		} else if (allCharactersAreUpperCase(tableName)) {
			tableName = tableName.toLowerCase();
		}
		if (startsWithLowerCaseCharacter(tableName)) {
			tableName = firstCharToUpperCase(tableName);
		}
		return tableName + "DBO";
	}

	private boolean containsUnderScores(String s) {
		return s.contains("_");
	}

	private String buildTableNameFromUnderScoreString(String s) {
		StringBuilder name = new StringBuilder();
		String[] parts = StringUtils.split(s, "_");
		for (String p : parts) {
			if (allCharactersAreUpperCase(p)) {
				p = p.toLowerCase();
			}
			p = firstCharToUpperCase(p);
			name.append(p);
		}
		return name.toString();
	}

	private boolean startsWithLowerCaseCharacter(String s) {
		String firstChar = StringUtils.left(s, 1);
		return firstChar.equals(firstChar.toLowerCase());
	}

	private String firstCharToUpperCase(String s) {
		return StringUtils.left(s, 1).toUpperCase() + (s.length() > 1 ? s.substring(1) : "");
	}

}