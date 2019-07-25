package de.ollie.library.persistence.dbo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * A ORM mapping and database access class for books.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Accessors(chain = true)
@Data
@Entity(name = "Book")
@Table(name = "BOOK")
public class BookDBO {

	@Id
	@Column(name = "ID")
	private long id;
	@ManyToOne
	@JoinColumn(name = "RACK_ID", referencedColumnName = "ID")
	private RackDBO rackId;
	@Column(name = "REFERENCE_LIBRARY")
	private Boolean referenceLibrary;
	@Column(name = "TITLE")
	private String title;

}