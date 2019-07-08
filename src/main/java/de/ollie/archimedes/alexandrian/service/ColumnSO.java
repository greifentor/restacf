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

	/**
	 * Returns the table and the column name separated by a dot.
	 * 
	 * @return The table and the column name separated by a dot.
	 */
	public String getNameWithTable() {
		return (this.table != null ? this.table.getName() + "." : "") + this.name;
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