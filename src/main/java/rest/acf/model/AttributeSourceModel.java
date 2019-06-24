package rest.acf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A source model for attributes.
 *
 * @author Oliver.Lieshoff
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class AttributeSourceModel {

	private String name;

}