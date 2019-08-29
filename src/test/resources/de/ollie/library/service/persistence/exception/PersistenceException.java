package de.ollie.library.service.persistence.exception;

/**
 * An exception for persistence errors.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
public class PersistenceException extends Exception {

	public enum Type { ReadError, WriteError }

	private Type type;

	public PersistenceException(Type type, String message, Throwable cause) {
		super(message, cause);
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

}