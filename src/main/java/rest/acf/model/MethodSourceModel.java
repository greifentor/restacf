package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for a method source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class MethodSourceModel {

	private String name;
	private String code;

}