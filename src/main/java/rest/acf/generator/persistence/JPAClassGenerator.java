package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class JPAClassGenerator {

	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new JPA class generator with the passed parameters.
	 * 
	 * @param nameConverter
	 *            An access to the name converter of the application.
	 * @param typeConverter
	 *            An access to the type converter of the application.
	 */
	public JPAClassGenerator(NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generiert eine JPA mapping class for the passed database table service
	 * object.
	 * 
	 * @param tableSO
	 *            The database table service object which the class is to create
	 *            for.
	 * @returns A JPA mapping class for passed database table or a "null" value
	 *          if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		List<AttributeSourceModel> attributes = new ArrayList<>();
		for (ColumnSO column : tableSO.getColumns()) {
			String attributeName = this.nameConverter
					.columnNameToAttributeName(column);
			String typeName = this.typeConverter
					.typeSOToTypeString(column.getType(), column.isNullable());
			attributes.add(new AttributeSourceModel().setName(attributeName)
					.setType(typeName));
		}
		return new ClassSourceModel().setAttributes(attributes)
				.setName(tableSO.getName() + "DBO");
	}

}