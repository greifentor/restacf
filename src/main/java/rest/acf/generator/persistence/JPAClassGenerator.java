package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class JPAClassGenerator {

	/**
	 * Generiert eine JPA mapping class for the passed database table service object.
	 * 
	 * @param tableSO The database table service object which the class is to create for.
	 * @returns A JPA mapping class for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		List<AttributeSourceModel> attributes = new ArrayList<>();
		for (ColumnSO column : tableSO.getColumns()) {
			attributes.add(new AttributeSourceModel().setName(column.getName()).setType(null));
		}
		return new ClassSourceModel().setAttributes(attributes).setName(tableSO.getName() + "Dbo");
	}

}