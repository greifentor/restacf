/*
 * EnumTypeSourceModel.java
 *
 * (c) by Ollie
 *
 * 29.08.2019
 */
package rest.acf.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A source model for enum types.
 *
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class EnumTypeSourceModel {

	private String name;
	private List<String> identifiers;
	private Set<ModifierSourceModel> modifiers = new HashSet<>();

	public EnumTypeSourceModel addModifier(ModifierSourceModel... modifiers) {
		for (ModifierSourceModel msm : modifiers) {
			this.modifiers.add(msm);
		}
		return this;
	}

}