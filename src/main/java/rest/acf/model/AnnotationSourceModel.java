package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A source model for annotations.
 *
 * @author Oliver.Lieshoff
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class AnnotationSourceModel {

	private String name;
	private String value;
	private List<PropertySourceModel<?>> properties = new ArrayList<>();

}