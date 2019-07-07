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

}