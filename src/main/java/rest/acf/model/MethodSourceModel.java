package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

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
public class MethodSourceModel implements AnnotationBearer {

	private String name;
	private String code;
	private String returnType;
	private List<ParameterSourceModel> parameters = new ArrayList<>();
	private List<AnnotationSourceModel> annotations = new ArrayList<>();

}