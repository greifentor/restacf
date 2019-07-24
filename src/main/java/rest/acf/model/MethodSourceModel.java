package rest.acf.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<ModifierSourceModel> modifiers = new HashSet<>();

	public MethodSourceModel addModifier(ModifierSourceModel... modifiers) {
		for (ModifierSourceModel msm : modifiers) {
			this.modifiers.add(msm);
		}
		return this;
	}

}