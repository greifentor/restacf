package rest.acf.generator.utils;

/**
 * An interface vor name finders.
 *
 * @author Oliver.Lieshoff
 *
 */
@FunctionalInterface
public interface NameFinder<T> {

	/**
	 * Returns the name for the passed object.
	 *
	 * @param t The object which the name is to find for.
	 * @return The name for the passed object.
	 */
	String getName(T t);

}