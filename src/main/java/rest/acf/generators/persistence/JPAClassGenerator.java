package rest.acf.generators.persistence;

import de.ollie.archimedes.alexandrian.service.TableSO;
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
		return null;
	}

}