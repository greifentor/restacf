package de.ollie.archimedes.alexandrian.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A container for column service objects.
 *
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class ColumnSO {

	@NonNull
	private String name;
	private TypeSO type;
	private boolean nullable;
	private boolean pkMember;
	@NonNull
	private TableSO table;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ColumnSO)) {
			return false;
		}
		return toString().equals(o.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return "ColumnSO(name=" + (name != null ? name : null) + ", type=" + (type != null ? type : null)
				+ ", nullable=" + nullable + ", pkMember=" + pkMember + ", table="
				+ (table != null ? table.getName() : null) + ")";
	}

}