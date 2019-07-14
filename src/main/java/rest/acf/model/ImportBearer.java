package rest.acf.model;

import java.util.List;

/**
 * An interface which describes an object which is bearing an import list.
 * 
 * @author ollie
 *
 */
public interface ImportBearer {

	/**
	 * Returns the import list of the object.
	 * 
	 * @return The import list of the object.
	 */
	List<ImportSourceModel> getImports();

	/**
	 * Sets the passed import list to the object.
	 * 
	 * @param imports The new import list for the import bearer.
	 * @return The import bearer.
	 */
	ImportBearer setImports(List<ImportSourceModel> imports);

}