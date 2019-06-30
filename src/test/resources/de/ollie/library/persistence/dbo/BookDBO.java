package de.ollie.library.persistence.dbo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * A mapping class book objects.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Entity
@Table(name="Book")
public BookDBO {

	@Id
	@Column(name="Id")
	private long id;
	@Column(name="CityOfPublication")
	private CityDBO cityOfPublication;
	@Column(name="Title")
	private String title;
	@Column(name="YearOfPublication")
	private int yearOfPublication;

}