package rest.acf.generator.converter;

import static de.ollie.utils.Check.ensure;

import java.sql.Types;

import org.apache.commons.lang3.StringUtils;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.OptionSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;

/**
 * A converter for table names.
 *
 * @author Oliver.Lieshoff
 *
 */
public class NameConverter {

	/**
	 * Converts a class name to an attribute name.
	 *
	 * @param className The name of the class which is to convert to an attribute name.
	 * @return The attribute name for the passed class name.
	 */
	public String classNameToAttrName(String className) {
		if ((className == null) || className.isEmpty()) {
			return className;
		}
		if (containsUnderScores(className)) {
			className = buildTableNameFromUnderScoreString(className);
		} else if (allCharactersAreUpperCase(className)) {
			className = className.toLowerCase();
		}
		if (startsWithUpperCaseCharacter(className)) {
			className = firstCharToLowerCase(className);
		}
		return className;

	}

	/**
	 * Converts a column name into a Java attribute name.
	 * 
	 * @param columnSO The column service object whose name is to convert.
	 * @return A Java attribute name based on the name of the passed column service object, or a "null" value, if a
	 *         "null" value is passed.
	 */
	public String columnNameToAttributeName(ColumnSO columnSO) {
		return columnNameToAttributeName(columnSO, false);
	}

	/**
	 * Converts a column name into a Java attribute name.
	 * 
	 * @param columnSO               The column service object whose name is to convert.
	 * @param useQualifiedColumnName Set this flag to get an attribute name for the qualified column name.
	 * @return A Java attribute name based on the name of the passed column service object, or a "null" value, if a
	 *         "null" value is passed.
	 */
	public String columnNameToAttributeName(ColumnSO columnSO, boolean useQualifiedColumnName) {
		if (columnSO == null) {
			return null;
		}
		String columnName = (useQualifiedColumnName ? columnSO.getTable().getName() + "_" : "") + columnSO.getName();
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
	 * Returns the getter name for the passed column service object.
	 * 
	 * @param columnSO The column service object whose getter name is to return.
	 * @return The getter name for the passed column service object.
	 */
	public String getGetterName(ColumnSO column) {
		if (column == null) {
			return null;
		}
		return (isBooleanColumn(column) && !column.isNullable() ? "is" : "get")
				+ this.firstCharToUpperCase(this.columnNameToAttributeName(column));
	}

	private boolean isBooleanColumn(ColumnSO column) {
		return (column.getType().getSqlType() == Types.BIT) || (column.getType().getSqlType() == Types.BOOLEAN);
	}

	/**
	 * Returns the setter name for the passed column service object.
	 * 
	 * @param columnSO The column service object whose setter name is to return.
	 * @return The setter name for the passed column service object.
	 */
	public String getSetterName(ColumnSO column) {
		if (column == null) {
			return null;
		}
		return "set" + this.firstCharToUpperCase(this.columnNameToAttributeName(column));
	}

	/**
	 * Returns a plural name for the passed table with a starting uppercase letter.
	 * 
	 * @param tableSO The table whose plural name is to create.
	 * @return the plural name for the passed table.
	 */
	public String getPluralName(TableSO tableSO) {
		return tableSO.getOptionByName("PLURAL_NAME") //
				.map(OptionSO::getValue) //
				.orElseGet(() -> {
					String n = getClassName(tableSO) + "s";
					if (n.endsWith("ys")) {
						n = n.substring(0, n.length() - 2) + "ies";
					}
					return n;
				}) //
		;
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
		return getClassName(tableSO) + "DBO";
	}

	public String getClassName(TableSO tableSO) {
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
		return tableName;
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

	/**
	 * Converts the name of the passed database service object into an application class name.
	 * 
	 * @param databaseSO The database service object whose name is to convert into an application class name.
	 * @return The application class name for the passed database service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String schemeNameToApplicationClassName(DatabaseSO databaseSO) {
		if (databaseSO == null) {
			return null;
		}
		return getClassName(databaseSO) + "Application";
	}

	private String getClassName(DatabaseSO databaseSO) {
		String databaseName = databaseSO.getName();
		ensure(!databaseName.isEmpty(), "database name cannot be empty.");
		if (containsUnderScores(databaseName)) {
			databaseName = buildTableNameFromUnderScoreString(databaseName);
		} else if (allCharactersAreUpperCase(databaseName)) {
			databaseName = databaseName.toLowerCase();
		}
		if (startsWithLowerCaseCharacter(databaseName)) {
			databaseName = firstCharToUpperCase(databaseName);
		}
		return databaseName;
	}

	/**
	 * Returns a singular name for the passed table with a starting uppercase letter.
	 * 
	 * @param tableSO The table whose singular name is to create.
	 * @return the singular name for the passed table.
	 */
	public String getSingularName(TableSO tableSO) {
		return getClassName(tableSO);
	}

	/**
	 * Converts the name of the passed table service object into a DBO converter class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a DBO converter class name.
	 * @return The DBO converter class name for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToDBOConverterClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "DBOConverter";
	}

	/**
	 * Converts the name of the passed table service object into a DTO class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a DTO class name.
	 * @return The DTO class name for the passed table service object. Passing a "null" value delivers a "null" value
	 *         also.
	 */
	public String tableNameToDTOClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "DTO";
	}

	/**
	 * Converts the name of the passed table service object into a DTO converter class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a DTO converter class name.
	 * @return The DTO converter class name for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToDTOConverterClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "DTOConverter";
	}

	/**
	 * Converts the name of the passed table service object into a persistence adapter class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a persistence adapter class name.
	 * @return The persistence adapter class for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToPersistenceAdapterClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "RDBMSPersistenceAdapter";
	}

	/**
	 * Converts the name of the passed table service object into a persistence port interface name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a persistence port interface name.
	 * @return The persistence port interface for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToPersistencePortInterfaceName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "PersistencePort";
	}

	/**
	 * Converts the name of the passed table service object into a repository interface name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a repository interface name.
	 * @return The repository interface name for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToRepositoryInterfaceName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "Repository";
	}

	/**
	 * Converts the name of the passed table service object into a REST controller class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a REST controller class name.
	 * @return The REST controller class name for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToRESTControllerClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "RESTController";
	}

	/**
	 * Converts the name of the passed table service object into a service interface name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a service interface name.
	 * @return The service interface name for the passed table service object. Passing a "null" value delivers a "null"
	 *         value also.
	 */
	public String tableNameToServiceInterfaceName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "Service";
	}

	/**
	 * Converts the name of the passed table service object into a service impl class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a service impl class name.
	 * @return The service impl class name for the passed table service object. Passing a "null" value delivers a "null"
	 *         value also.
	 */
	public String tableNameToServiceImplClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return tableNameToServiceInterfaceName(tableSO) + "Impl";
	}

	/**
	 * Converts the name of the passed table service object into a service object class name.
	 * 
	 * @param tableSO The table service object whose name is to convert into a service object class name.
	 * @return The service object class name for the passed table service object. Passing a "null" value delivers a
	 *         "null" value also.
	 */
	public String tableNameToServiceObjectClassName(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		return getClassName(tableSO) + "SO";
	}

}