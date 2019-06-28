package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for a class source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ClassSourceModel implements AnnotationBearer {

	private PackageSourceModel packageModel;
	private String name;
	private List<AnnotationSourceModel> annotations = new ArrayList<>();
	private List<AttributeSourceModel> attributes = new ArrayList<>();
	private List<ImportSourceModel> imports = new ArrayList<>();
	private List<MethodSourceModel> methods = new ArrayList<>();

}