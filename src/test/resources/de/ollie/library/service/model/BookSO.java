package de.ollie.library.service.model;


/**
 * A model class book objects.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Accessor(chain = true)
@AllArgsConstructor
@Data
public BookSO {

	private long id;
	private CitySO cityOfPublication;
	private String title;
	private int yearOfPublication;

}