package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A source model for properties (e. g. of annotations),
 *
 * @author Oliver.Lieshoff
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class PropertySourceModel<T> {

	private String name;
	private T content;
	private boolean quoted;

}