package de.ollie.archimedes.alexandrian.service;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A container for table service objects.
 *
 * @author ollie
 *
 */
@Data
@Accessors(chain = true)
public class TableSO {

	private String name;
	private List<ColumnSO> columns;
	private TableMetaInfo metaInfo;
	private TableGUIInfo guiInfo;

}