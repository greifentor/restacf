package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;

/**
 * Unit tests for class "NameConverter".
 * 
 * @author Oliver.Lieshoff
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class NameConverterTest {

	@InjectMocks
	private NameConverter unitUnderTest;

	@Test(expected = IllegalArgumentException.class)
	public void columnNameToAttributeName_PassColumnSOWithEmptyName_ThrowsException() {
		// Prepare
		ColumnSO columnSO = new ColumnSO().setName("");
		// Run
		this.unitUnderTest.columnNameToAttributeName(columnSO);
	}

	@Test
	public void columnNameToAttributeName_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.columnNameToAttributeName(null), nullValue());
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "column";
		ColumnSO columnSO = new ColumnSO().setName("column");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "column";
		ColumnSO columnSO = new ColumnSO().setName("Column");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameCompleteUpperCase_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "column";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameSingleUpperCase_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "c";
		ColumnSO columnSO = new ColumnSO().setName("C");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameSingleLowerCase_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "c";
		ColumnSO columnSO = new ColumnSO().setName("c");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "columnName";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "columnName";
		ColumnSO columnSO = new ColumnSO().setName("Column_Name");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void columnNameToAttributeName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectAttributeName() {
		// Prepare
		String expected = "columnName";
		ColumnSO columnSO = new ColumnSO().setName("column_name");
		// Run
		String returned = this.unitUnderTest.columnNameToAttributeName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToDBOClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToDBOClassName(tableSO);
	}

	@Test
	public void tableNameToDBOClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToDBOClassName(null), nullValue());
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TestTableDBO";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableDBO";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameDBO";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameDBO";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameDBO";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableDBO";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TDBO";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TDBO";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

}