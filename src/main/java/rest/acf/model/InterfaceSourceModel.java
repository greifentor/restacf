package rest.acf.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A model for an interface source.
 * 
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class InterfaceSourceModel implements AnnotationBearer, ImportBearer {

	private PackageSourceModel packageModel = new PackageSourceModel();
	private String name;
	private ExtensionSourceModel extendsModel;
	private ClassCommentSourceModel comment;
	private List<AnnotationSourceModel> annotations = new ArrayList<>();
	private List<ImportSourceModel> imports = new ArrayList<>();

}