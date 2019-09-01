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
	private Set<ThrownExceptionSourceModel> thrownExceptions = new HashSet<>();

	public MethodSourceModel addAnnotations(AnnotationSourceModel... annotations) {
		for (AnnotationSourceModel asm : annotations) {
			this.annotations.add(asm);
		}
		return this;
	}

	public MethodSourceModel addModifiers(ModifierSourceModel... modifiers) {
		for (ModifierSourceModel msm : modifiers) {
			this.modifiers.add(msm);
		}
		return this;
	}

	public MethodSourceModel addParameters(ParameterSourceModel... parameters) {
		for (ParameterSourceModel psm : parameters) {
			this.parameters.add(psm);
		}
		return this;
	}

	public MethodSourceModel addThrownExceptions(ThrownExceptionSourceModel... thrownExceptions) {
		for (ThrownExceptionSourceModel tesm : thrownExceptions) {
			this.thrownExceptions.add(tesm);
		}
		return this;
	}

}