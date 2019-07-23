package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Types;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;

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

	@Test
	public void classNameToAttributeName_PassANullValue_ReturnsANullValue() {
		// Prepare
		String expected = null;
		String passed = null;
		// Run
		String returned = this.unitUnderTest.classNameToAttrName(passed);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classNameToAttributeName_PassAnEmptyString_ReturnsAnEmptyString() {
		// Prepare
		String expected = "";
		String passed = "";
		// Run
		String returned = this.unitUnderTest.classNameToAttrName(passed);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classNameToAttributeName_PassAClassName_ReturnsAnAttributeNameForThePassedClassName() {
		// Prepare
		String expected = "aClass";
		String passed = "AClass";
		// Run
		String returned = this.unitUnderTest.classNameToAttrName(passed);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classNameToAttributeName_PassAClassNameWithUnderscores_ReturnsAnAttributeNameForThePassedClassName() {
		// Prepare
		String expected = "aClass";
		String passed = "A_Class";
		// Run
		String returned = this.unitUnderTest.classNameToAttrName(passed);
		// Check
		assertThat(returned, equalTo(expected));
	}

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

	@Test
	public void getGetterName_PassANullValue_ReturnsANullValue() {
		// Prepare
		String expected = null;
		// Run
		String returned = this.unitUnderTest.getGetterName(null);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getColumn";
		ColumnSO columnSO = new ColumnSO().setName("column").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getColumn";
		ColumnSO columnSO = new ColumnSO().setName("Column").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameCompleteUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getColumn";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameSingleUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getC";
		ColumnSO columnSO = new ColumnSO().setName("C").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameSingleLowerCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getC";
		ColumnSO columnSO = new ColumnSO().setName("c").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getColumnName";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "getColumnName";
		ColumnSO columnSO = new ColumnSO().setName("Column_Name").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectGetterName() {
		// Prepare
		String expected = "getColumnName";
		ColumnSO columnSO = new ColumnSO().setName("column_name").setType(new TypeSO().setSqlType(Types.VARCHAR));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnSOWithBooleanType_ReturnsACorrectGetterName() {
		// Prepare
		String expected = "isColumnName";
		ColumnSO columnSO = new ColumnSO().setName("column_name").setType(new TypeSO().setSqlType(Types.BOOLEAN));
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getGetterName_PassAColumnSOWithNullableBooleanType_ReturnsACorrectGetterName() {
		// Prepare
		String expected = "getColumnName";
		ColumnSO columnSO = new ColumnSO().setName("column_name").setType(new TypeSO().setSqlType(Types.BOOLEAN))
				.setNullable(true);
		// Run
		String returned = this.unitUnderTest.getGetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassANullValue_ReturnsANullValue() {
		// Prepare
		String expected = null;
		// Run
		String returned = this.unitUnderTest.getSetterName(null);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumn";
		ColumnSO columnSO = new ColumnSO().setName("column");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumn";
		ColumnSO columnSO = new ColumnSO().setName("Column");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameCompleteUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumn";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameSingleUpperCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setC";
		ColumnSO columnSO = new ColumnSO().setName("C");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameSingleLowerCase_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setC";
		ColumnSO columnSO = new ColumnSO().setName("c");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumnName";
		ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumnName";
		ColumnSO columnSO = new ColumnSO().setName("Column_Name");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getSetterName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectMethodName() {
		// Prepare
		String expected = "setColumnName";
		ColumnSO columnSO = new ColumnSO().setName("column_name");
		// Run
		String returned = this.unitUnderTest.getSetterName(columnSO);
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

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToDBOConverterClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
	}

	@Test
	public void tableNameToDBOConverterClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToDBOConverterClassName(null), nullValue());
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TestTableDBOConverter";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TableDBOConverter";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TableNameDBOConverter";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TableNameDBOConverter";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TableNameDBOConverter";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TableDBOConverter";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TDBOConverter";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDBOConverterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOConverterName() {
		// Prepare
		String expected = "TDBOConverter";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToDBOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToDTOClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToDTOClassName(tableSO);
	}

	@Test
	public void tableNameToDTOClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToDTOClassName(null), nullValue());
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TestTableDTO";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TableDTO";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TableNameDTO";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TableNameDTO";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TableNameDTO";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TableDTO";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TDTO";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDTOName() {
		// Prepare
		String expected = "TDTO";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToDTOConverterClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
	}

	@Test
	public void tableNameToDTOConverterClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToDTOConverterClassName(null), nullValue());
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TestTableDTOConverter";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TableDTOConverter";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TableNameDTOConverter";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TableNameDTOConverter";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TableNameDTOConverter";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TableDTOConverter";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TDTOConverter";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToDTOConverterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDTOConverterName() {
		// Prepare
		String expected = "TDTOConverter";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToDTOConverterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToPersistenceAdapterClassName(null), nullValue());
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TestTableRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistenceAdapterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectName() {
		// Prepare
		String expected = "TRDBMSPersistenceAdapter";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToPersistencePortInterfaceName(null), nullValue());
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TestTablePersistencePort";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TablePersistencePort";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNamePersistencePort";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNamePersistencePort";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNamePersistencePort";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TablePersistencePort";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TPersistencePort";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToPersistencePortInterfaceName_PassTableSONameSinglelowerCase_ReturnsACorrectName() {
		// Prepare
		String expected = "TPersistencePort";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToRepositoryInterfaceName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToRepositoryInterfaceName(null), nullValue());
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TestTableRepository";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableRepository";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRepository";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRepository";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameRepository";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableRepository";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TRepository";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToRepositoryInterfaceName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TRepository";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test(expected = IllegalArgumentException.class)
	public void tableNameToServiceObjectClassName_PassTableSOWithEmptyName_ThrowsException() {
		// Prepare
		TableSO tableSO = new TableSO().setName("");
		// Run
		this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
	}

	@Test
	public void tableNameToServiceObjectClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToServiceObjectClassName(null), nullValue());
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TestTableSO";
		TableSO tableSO = new TableSO().setName("TestTable");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableSO";
		TableSO tableSO = new TableSO().setName("TABLE");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameSO";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameSO";
		TableSO tableSO = new TableSO().setName("table_name");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameSO";
		TableSO tableSO = new TableSO().setName("Table_Name");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableSO";
		TableSO tableSO = new TableSO().setName("table");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TSO";
		TableSO tableSO = new TableSO().setName("T");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void tableNameToServiceObjectClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TSO";
		TableSO tableSO = new TableSO().setName("t");
		// Run
		String returned = this.unitUnderTest.tableNameToServiceObjectClassName(tableSO);
		// Check
		assertThat(returned, equalTo(expected));
	}

}