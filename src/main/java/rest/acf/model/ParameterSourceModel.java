package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for a parameter source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ParameterSourceModel {

	private String name;
	private String type;
	private List<AnnotationSourceModel> annotations = new ArrayList<>();

}