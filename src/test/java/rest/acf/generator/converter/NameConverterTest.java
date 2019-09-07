package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * Unit tests for class "NameConverter".
 * 
 * @author Oliver.Lieshoff
 *
 */
@ExtendWith(MockitoExtension.class)
public class NameConverterTest {

	@InjectMocks
	private NameConverter unitUnderTest;

	@DisplayName("tests for class names to attribute names")
	@Nested
	class ClassNameToAttributeNameTests {

		@Test
		public void classNameToAttributeName_PassANullValue_ReturnsANullValue() {
			// Prepare
			String expected = null;
			String passed = null;
			// Run
			String returned = unitUnderTest.classNameToAttrName(passed);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void classNameToAttributeName_PassAnEmptyString_ReturnsAnEmptyString() {
			// Prepare
			String expected = "";
			String passed = "";
			// Run
			String returned = unitUnderTest.classNameToAttrName(passed);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void classNameToAttributeName_PassAClassName_ReturnsAnAttributeNameForThePassedClassName() {
			// Prepare
			String expected = "aClass";
			String passed = "AClass";
			// Run
			String returned = unitUnderTest.classNameToAttrName(passed);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void classNameToAttributeName_PassAClassNameWithUnderscores_ReturnsAnAttributeNameForThePassedClassName() {
			// Prepare
			String expected = "aClass";
			String passed = "A_Class";
			// Run
			String returned = unitUnderTest.classNameToAttrName(passed);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for column names to attribute names")
	@Nested
	class ColumnNameToAttributeNameTests {

		@Test
		public void columnNameToAttributeName_PassColumnSOWithEmptyName_ThrowsException() {
			// Prepare
			ColumnSO columnSO = new ColumnSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.columnNameToAttributeName(columnSO);
			});
		}

		@Test
		public void columnNameToAttributeName_PassANullValue_ReturnsANullValue() {
			assertThat(unitUnderTest.columnNameToAttributeName(null), nullValue());
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			ColumnSO columnSO = new ColumnSO().setName("column");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			ColumnSO columnSO = new ColumnSO().setName("Column");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameCompleteUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameSingleUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			ColumnSO columnSO = new ColumnSO().setName("C");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameSingleLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			ColumnSO columnSO = new ColumnSO().setName("c");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			ColumnSO columnSO = new ColumnSO().setName("Column_Name");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void columnNameToAttributeName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			ColumnSO columnSO = new ColumnSO().setName("column_name");
			// Run
			String returned = unitUnderTest.columnNameToAttributeName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for getter names")
	@Nested
	class GetterNameTests {

		@Test
		public void getGetterName_PassANullValue_ReturnsANullValue() {
			// Prepare
			String expected = null;
			// Run
			String returned = unitUnderTest.getGetterName(null);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getColumn";
			ColumnSO columnSO = new ColumnSO().setName("column").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getColumn";
			ColumnSO columnSO = new ColumnSO().setName("Column").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameCompleteUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getColumn";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameSingleUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getC";
			ColumnSO columnSO = new ColumnSO().setName("C").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameSingleLowerCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getC";
			ColumnSO columnSO = new ColumnSO().setName("c").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getColumnName";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "getColumnName";
			ColumnSO columnSO = new ColumnSO().setName("Column_Name").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectGetterName() {
			// Prepare
			String expected = "getColumnName";
			ColumnSO columnSO = new ColumnSO().setName("column_name").setType(new TypeSO().setSqlType(Types.VARCHAR));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getGetterName_PassAColumnSOWithBooleanType_ReturnsACorrectGetterName() {
			// Prepare
			String expected = "isColumnName";
			ColumnSO columnSO = new ColumnSO().setName("column_name").setType(new TypeSO().setSqlType(Types.BOOLEAN));
			// Run
			String returned = unitUnderTest.getGetterName(columnSO);
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
			String returned = unitUnderTest.getGetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for setter names")
	@Nested
	class SetterNameTests {

		@Test
		public void getSetterName_PassANullValue_ReturnsANullValue() {
			// Prepare
			String expected = null;
			// Run
			String returned = unitUnderTest.getSetterName(null);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumn";
			ColumnSO columnSO = new ColumnSO().setName("column");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumn";
			ColumnSO columnSO = new ColumnSO().setName("Column");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameCompleteUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumn";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameSingleUpperCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setC";
			ColumnSO columnSO = new ColumnSO().setName("C");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameSingleLowerCase_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setC";
			ColumnSO columnSO = new ColumnSO().setName("c");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumnName";
			ColumnSO columnSO = new ColumnSO().setName("COLUMN_NAME");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumnName";
			ColumnSO columnSO = new ColumnSO().setName("Column_Name");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void getSetterName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectMethodName() {
			// Prepare
			String expected = "setColumnName";
			ColumnSO columnSO = new ColumnSO().setName("column_name");
			// Run
			String returned = unitUnderTest.getSetterName(columnSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for schema name to application class names")
	@Nested
	class SchemaNameToApplicationClassNameTests {

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			DatabaseSO databaseSO = new DatabaseSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			});
		}

		@Test
		public void schemeNameToApplicationClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.schemeNameToApplicationClassName(null), nullValue());
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameCamelCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TestTableApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameUpperCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TableApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TableNameApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TableNameApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("table_name");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TableNameApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSOWithNameLowerCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TableApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("table");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassTableSONameSingleUpperCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("T");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void schemeNameToApplicationClassName_PassDatabaseSONameSinglelowerCase_ReturnsACorrectApplicationClassName() {
			// Prepare
			String expected = "TApplication";
			DatabaseSO databaseSO = new DatabaseSO().setName("t");
			// Run
			String returned = unitUnderTest.schemeNameToApplicationClassName(databaseSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for DBO class names")
	@Nested
	class DBOClassNameTests {

		@Test
		public void tableNameToDBOClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToDBOClassName(tableSO);
			});
		}

		@Test
		public void tableNameToDBOClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToDBOClassName(null), nullValue());
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableDBO";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToDBOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for DBO converter class names")
	@Nested
	class DBOConverterClassNameTests {

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			});
		}

		@Test
		public void tableNameToDBOConverterClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToDBOConverterClassName(null), nullValue());
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TestTableDBOConverter";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TableDBOConverter";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TableNameDBOConverter";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TableNameDBOConverter";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TableNameDBOConverter";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TableDBOConverter";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TDBOConverter";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDBOConverterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOConverterName() {
			// Prepare
			String expected = "TDBOConverter";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToDBOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for DTO class names")
	@Nested
	class DTOClassNameTests {

		@Test
		public void tableNameToDTOClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToDTOClassName(tableSO);
			});
		}

		@Test
		public void tableNameToDTOClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToDTOClassName(null), nullValue());
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTO";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToDTOClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for DTO converter class names")
	@Nested
	class DTOConverterClassNameTests {

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			});
		}

		@Test
		public void tableNameToDTOConverterClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToDTOConverterClassName(null), nullValue());
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TestTableDTOConverter";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableDTOConverter";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TableDTOConverter";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TDTOConverter";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToDTOConverterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDTOConverterName() {
			// Prepare
			String expected = "TDTOConverter";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToDTOConverterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for persistence adapter class names")
	@Nested
	class PersistenceAdapterClassNameTests {

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			});
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToPersistenceAdapterClassName(null), nullValue());
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistenceAdapterClassName_PassTableSONameSinglelowerCase_ReturnsACorrectName() {
			// Prepare
			String expected = "TRDBMSPersistenceAdapter";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToPersistenceAdapterClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for persistence port interface names")
	@Nested
	class PersistencePortInterfaceNameTests {

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			});
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToPersistencePortInterfaceName(null), nullValue());
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTablePersistencePort";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TablePersistencePort";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TablePersistencePort";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TPersistencePort";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToPersistencePortInterfaceName_PassTableSONameSinglelowerCase_ReturnsACorrectName() {
			// Prepare
			String expected = "TPersistencePort";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToPersistencePortInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for repository interface names")
	@Nested
	class RepositoryInterfaceNameTests {

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			});
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToRepositoryInterfaceName(null), nullValue());
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableRepository";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRepository";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRepository";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRepository";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRepository";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRepository";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TRepository";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRepositoryInterfaceName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TRepository";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToRepositoryInterfaceName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for REST controller class names")
	@Nested
	class RESTControllerClassNameTests {

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			});
		}

		@Test
		public void tableNameToRESTControllerClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToRESTControllerClassName(null), nullValue());
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableRESTController";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRESTController";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRESTController";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRESTController";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameRESTController";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRESTController";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TRESTController";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToRESTControllerClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TRESTController";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToRESTControllerClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}
	}

	@DisplayName("tests for service interface implementation class names")
	@Nested
	class ServiceImplClassNameTests {

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToServiceImplClassName(tableSO);
			});
		}

		@Test
		public void tableNameToServiceImplClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToServiceImplClassName(null), nullValue());
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameCamelCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TestTableServiceImpl";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameUpperCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TableServiceImpl";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSOWithNameLowerCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TableServiceImpl";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSONameSingleUpperCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TServiceImpl";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceImplClassName_PassTableSONameSinglelowerCase_ReturnsACorrectServiceImplClassName() {
			// Prepare
			String expected = "TServiceImpl";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToServiceImplClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@DisplayName("tests for service interface names")
		@Nested
		class ServiceInterfaceNameTests {

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithEmptyName_ThrowsException() {
				// Prepare
				TableSO tableSO = new TableSO().setName("");
				// Run
				Assertions.assertThrows(IllegalArgumentException.class, () -> {
					unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				});
			}

			@Test
			public void tableNameToServiceInterfaceName_PassNullValue_ReturnsNullValue() {
				assertThat(unitUnderTest.tableNameToServiceInterfaceName(null), nullValue());
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameCamelCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TestTableService";
				TableSO tableSO = new TableSO().setName("TestTable");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameUpperCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TableService";
				TableSO tableSO = new TableSO().setName("TABLE");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TableNameService";
				TableSO tableSO = new TableSO().setName("TABLE_NAME");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TableNameService";
				TableSO tableSO = new TableSO().setName("table_name");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TableNameService";
				TableSO tableSO = new TableSO().setName("Table_Name");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSOWithNameLowerCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TableService";
				TableSO tableSO = new TableSO().setName("table");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSONameSingleUpperCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TService";
				TableSO tableSO = new TableSO().setName("T");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}

			@Test
			public void tableNameToServiceInterfaceName_PassTableSONameSinglelowerCase_ReturnsACorrectServiceInterfaceName() {
				// Prepare
				String expected = "TService";
				TableSO tableSO = new TableSO().setName("t");
				// Run
				String returned = unitUnderTest.tableNameToServiceInterfaceName(tableSO);
				// Check
				assertThat(returned, equalTo(expected));
			}
		}
	}

	@DisplayName("tests for service object class names")
	@Nested
	class ResultPageSOClassNameTests {

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithEmptyName_ThrowsException() {
			// Prepare
			TableSO tableSO = new TableSO().setName("");
			// Run
			Assertions.assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			});
		}

		@Test
		public void tableNameToServiceObjectClassName_PassNullValue_ReturnsNullValue() {
			assertThat(unitUnderTest.tableNameToServiceObjectClassName(null), nullValue());
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableSO";
			TableSO tableSO = new TableSO().setName("TestTable");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableSO";
			TableSO tableSO = new TableSO().setName("TABLE");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameSO";
			TableSO tableSO = new TableSO().setName("TABLE_NAME");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameSO";
			TableSO tableSO = new TableSO().setName("table_name");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameSO";
			TableSO tableSO = new TableSO().setName("Table_Name");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSOWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableSO";
			TableSO tableSO = new TableSO().setName("table");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSONameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TSO";
			TableSO tableSO = new TableSO().setName("T");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

		@Test
		public void tableNameToServiceObjectClassName_PassTableSONameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TSO";
			TableSO tableSO = new TableSO().setName("t");
			// Run
			String returned = unitUnderTest.tableNameToServiceObjectClassName(tableSO);
			// Check
			assertThat(returned, equalTo(expected));
		}

	}

}