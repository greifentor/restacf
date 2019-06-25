package de.ollie.archimedes.alexandrian.service;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A container for table service objects.
 *
 * @author ollie
 *
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class TableSO {

	@NonNull
	private String name;
	private List<ColumnSO> columns;
	private TableMetaInfo metaInfo;
	private TableGUIInfo guiInfo;

}