package rest.acf.generator.converter;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Types;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * Unit tests of class "DataModelToSOConverter".
 * 
 * @author ollie
 *
 */
@RunWith(MockitoJUnitRunner.class)
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
		ColumnSO column0 = new ColumnSO().setName(COLUMN0_NAME).setType(new TypeSO().setSqlType(TYPE_BIGINT));
		ColumnSO column1 = new ColumnSO().setName(COLUMN1_NAME)
				.setType(new TypeSO().setSqlType(TYPE_VARCHAR).setLength(TYPE_VARCHAR_LENGTH));
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(Arrays.asList(column0, column1));
		SchemeSO scheme = new SchemeSO().setName("public").setTables(Arrays.asList(table));
		DatabaseSO expected = new DatabaseSO().setName(MODEL_NAME).setSchemes(Arrays.asList(scheme));
		DataModel model = mock(DataModel.class);
		TableModel tm = mock(TableModel.class);
		ColumnModel cm0 = mock(ColumnModel.class);
		ColumnModel cm1 = mock(ColumnModel.class);
		DomainModel domBigInt = mock(DomainModel.class);
		DomainModel domVarchar = mock(DomainModel.class);
		when(domBigInt.getDataType()).thenReturn(TYPE_BIGINT);
		when(domVarchar.getDataType()).thenReturn(TYPE_VARCHAR);
		when(domVarchar.getLength()).thenReturn(TYPE_VARCHAR_LENGTH);
		when(cm0.getName()).thenReturn(COLUMN0_NAME);
		when(cm0.getDomain()).thenReturn(domBigInt);
		when(cm1.getName()).thenReturn(COLUMN1_NAME);
		when(cm1.getDomain()).thenReturn(domVarchar);
		when(tm.getColumns()).thenReturn(new ColumnModel[] { cm0, cm1 });
		when(tm.getName()).thenReturn(TABLE_NAME);
		when(model.getName()).thenReturn(MODEL_NAME);
		when(model.getTables()).thenReturn(new TableModel[] { tm });
		// Run
		DatabaseSO returned = this.unitUnderTest.convert(model);
		// Check
		assertThat(returned, equalTo(expected));
	}

}