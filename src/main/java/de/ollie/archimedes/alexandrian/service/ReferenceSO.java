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
public class ReferenceSO {

	@NonNull
	private TableSO referencingTable;
	@NonNull
	private ColumnSO referencingColumn;
	@NonNull
	private TableSO referencedTable;
	@NonNull
	private ColumnSO referencedColumn;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ReferenceSO)) {
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
		return "ReferenceSO(referencedTable=" + (referencedTable != null ? referencedTable.getName() : "null")
				+ ", referencedColumn=" + (referencedColumn != null ? referencedColumn.getName() : "null")
				+ ", referencingTable=" + (referencingTable != null ? referencingTable.getName() : "null")
				+ ", referencingColumn=" + (referencingColumn != null ? referencingColumn.getName() : "null") + ")";
	}

}