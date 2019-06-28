package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

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
public class AttributeSourceModel implements AnnotationBearer {

	private String name;
	private String type;
	private List<AnnotationSourceModel> annotations = new ArrayList<>();

}