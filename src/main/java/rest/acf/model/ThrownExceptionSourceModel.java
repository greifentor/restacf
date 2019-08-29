/*
 * ThrownExceptionSourceModel.java
 *
 * (c) by Ollie
 *
 * 29.08.2019
 */
package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for a thrown exception.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ThrownExceptionSourceModel {

	private String name;

}