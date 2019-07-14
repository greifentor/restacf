package de.ollie.library.persistence.dbo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * A mapping class city objects.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Entity
@Table(name="City")
public class CityDBO {

	@Id
	@Column(name="Id")
	private long id;
	@Column(name="Name")
	private String name;

}