package rest.acf.model;

import java.util.List;

/**
 * A interface for classes which could bearing annotations.
 * 
 * @author Oliver.Lieshoff
 *
 */
public interface AnnotationBearer {

	/**
	 * The annotations beared by the object.
	 * 
	 * @return The annotations beared by the object.
	 */
	List<AnnotationSourceModel> getAnnotations();

	/**
	 * Sets a annotations to the object.
	 * 
	 * @param annotations
	 *            The new annotations for the object.
	 */
	AnnotationBearer setAnnotations(List<AnnotationSourceModel> annotations);

}