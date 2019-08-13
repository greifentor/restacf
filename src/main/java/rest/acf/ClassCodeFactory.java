/*
 * CodeFactory.java
 *
 * (c) by Ollie
 *
 * 13.08.2019
 */
package rest.acf;

import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.model.ClassSourceModel;

/**
 * An interface for CodeFactory objects in the REST Archimedes code factory.
 *
 * @author ollie
 *
 */
public interface ClassCodeFactory {

	/**
	 * Creates a new ClassSourceModel for the passed table service object.
	 * 
	 * @param tableSO    The table service object which the ClassSourceModel is to create for.
	 * @param authorName The name of the author.
	 * @return The ClassSourceModel which is generated or a "null" value if a "null" value is passed.
	 */
	ClassSourceModel generate(TableSO tableSO, String authorName);

}