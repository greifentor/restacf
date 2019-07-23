package rest.acf.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private Set<ModifierSourceModel> modifiers = new HashSet<>();
	private List<AnnotationSourceModel> annotations = new ArrayList<>();

	public AttributeSourceModel addModifier(ModifierSourceModel... modifiers) {
		for (ModifierSourceModel msm : modifiers) {
			this.modifiers.add(msm);
		}
		return this;
	}
}