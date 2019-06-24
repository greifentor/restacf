package rest.acf.model;

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
public class ClassSourceModel {

	private PackageSourceModel packageModel;
	private String name;
	private List<AttributeSourceModel> attributes;
	private List<ImportSourceModel> imports;
	private List<MethodSourceModel> methods;

}