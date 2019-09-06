package rest.acf.generator.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Types;
import java.util.Arrays;

import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * Unit tests of class "DataModelToSOConverter".
 * 
 * @author ollie
 *
 */
@ExtendWith(MockitoExtension.class)
public class DataModelToSOConverterTest {

	private static final String COLUMN0_NAME = "Column0";
	private static final String COLUMN1_NAME = "Column1";
	private static final String MODEL_NAME = "ModelName";
	private static final String TABLE_NAME = "TableName";
	private static final int TYPE_BIGINT = Types.BIGINT;
	private static final int TYPE_VARCHAR = Types.VARCHAR;
	private static final int TYPE_VARCHAR_LENGTH = 50;

	@InjectMocks
	private DataModelToSOConverter unitUnderTest;

	@Test
	public void convert_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.convert(null), nullValue());
	}

	@Test
	public void convert_PassADataModel_ReturnsADatabaseSOWithTheDataOfThePassedModel() {
		// Prepare
		ColumnSO column0 = new ColumnSO().setName(COLUMN0_NAME).setType(new TypeSO().setSqlType(TYPE_BIGINT))
				.setNullable(false).setPkMember(true).setUnique(true);
		ColumnSO column1 = new ColumnSO().setName(COLUMN1_NAME)
				.setType(new TypeSO().setSqlType(TYPE_VARCHAR).setLength(TYPE_VARCHAR_LENGTH)).setNullable(true);
		ColumnSO column2 = new ColumnSO().setName("Reference").setType(new TypeSO().setSqlType(TYPE_BIGINT))
				.setNullable(true);
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(Arrays.asList(column0, column1));
		TableSO tableReferencing = new TableSO().setName(TABLE_NAME + "Referencing").setColumns(Arrays.asList(column2));
		column0.setTable(table);
		column1.setTable(table);
		column2.setTable(tableReferencing);
		ReferenceSO reference = new ReferenceSO().setReferencedColumn(column0).setReferencingColumn(column2);
		ForeignKeySO foreignKey = new ForeignKeySO().setReferences(Arrays.asList(reference));
		tableReferencing.setForeignKeys(Arrays.asList(foreignKey));
		SchemeSO scheme = new SchemeSO().setName("public").setTables(Arrays.asList(table, tableReferencing));
		DatabaseSO expected = new DatabaseSO().setName(MODEL_NAME).setSchemes(Arrays.asList(scheme));
		DataModel model = mock(DataModel.class);
		TableModel tm = mock(TableModel.class);
		TableModel tmReferencing = mock(TableModel.class);
		ColumnModel cm0 = mock(ColumnModel.class);
		ColumnModel cm1 = mock(ColumnModel.class);
		ColumnModel cm2 = mock(ColumnModel.class);
		DomainModel domBigInt = mock(DomainModel.class);
		DomainModel domVarchar = mock(DomainModel.class);
		when(domBigInt.getDataType()).thenReturn(TYPE_BIGINT);
		when(domVarchar.getDataType()).thenReturn(TYPE_VARCHAR);
		when(domVarchar.getLength()).thenReturn(TYPE_VARCHAR_LENGTH);
		when(cm0.getName()).thenReturn(COLUMN0_NAME);
		when(cm0.getDomain()).thenReturn(domBigInt);
		when(cm0.isNotNull()).thenReturn(true);
		when(cm0.isPrimaryKey()).thenReturn(true);
		when(cm0.isUnique()).thenReturn(true);
		when(cm1.getName()).thenReturn(COLUMN1_NAME);
		when(cm1.getDomain()).thenReturn(domVarchar);
		when(cm1.isNotNull()).thenReturn(false);
		when(tm.getColumns()).thenReturn(new ColumnModel[] { cm0, cm1 });
		when(tm.getName()).thenReturn(TABLE_NAME);
		when(tmReferencing.getColumns()).thenReturn(new ColumnModel[] { cm2 });
		when(tmReferencing.getName()).thenReturn(TABLE_NAME + "Referencing");
		when(cm2.getName()).thenReturn("Reference");
		when(cm2.getDomain()).thenReturn(domBigInt);
		when(cm2.getReferencedColumn()).thenReturn(cm0);
		when(cm2.getReferencedTable()).thenReturn(tm);
		when(model.getName()).thenReturn(MODEL_NAME);
		when(model.getTables()).thenReturn(new TableModel[] { tm, tmReferencing });
		// Run
		DatabaseSO returned = this.unitUnderTest.convert(model);
		// Check
		assertEquals(expected.toString(), returned.toString());
	}

}