package rest.acf.generator.converter;

import java.sql.Types;

import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * A converter for type objects.
 *
 * @author ollie
 *
 */
public class TypeConverter {

	/**
	 * Converts a service object into a string with the Java type name..
	 * 
	 * @param typeSO The service object which should be converted.
	 * @return A string with the name of the Java type.
	 * @throws IllegalArgumentException If the passed type cannot be converted.
	 */
	public String typeSOToTypeString(TypeSO typeSO) {
		if (typeSO == null) {
			return null;
		}
		if (typeSO.getSqlType() == Types.BIGINT) {
			return "long";
		} else if (typeSO.getSqlType() == Types.BOOLEAN) {
			return "boolean";
		} else if ((typeSO.getSqlType() == Types.CHAR) || (typeSO.getSqlType() == Types.LONGNVARCHAR)
				|| (typeSO.getSqlType() == Types.LONGVARCHAR)) {
			if (typeSO.getLength() == 1) {
				return "char";
			}
			return "String";
		} else if (typeSO.getSqlType() == Types.DATE) {
			return "LocalDate";
		} else if ((typeSO.getSqlType() == Types.DECIMAL) || (typeSO.getSqlType() == Types.DOUBLE)) {
			return "double";
		} else if (typeSO.getSqlType() == Types.FLOAT) {
			return "float";
		} else if (typeSO.getSqlType() == Types.INTEGER) {
			return "int";
		}
		throw new IllegalArgumentException("type " + typeSO.getSqlType() + " cannot be converted to a Java type.");
	}

}