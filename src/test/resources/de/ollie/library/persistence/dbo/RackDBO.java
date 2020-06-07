package de.ollie.library.persistence.dbo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A ORM mapping and database access class for racks.
 *
 * @author rest-acf
 *
 * GENERATED CODE!!! DO NOT CHANGE!!!
 */
@Accessors(chain = true)
@Data
@Entity(name = "Rack")
@Table(name = "RACK")
public class RackDBO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "RACK_IDS")
	@Column(name = "ID")
	private long id;
	@Column(name = "NAME")
	private String name;

}