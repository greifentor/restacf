package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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

	@Test
	public void tableNameToDBOClassName_PassNullValue_ReturnsNullValue() {
		assertThat(this.unitUnderTest.tableNameToDBOClassName(null),
				nullValue());
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
	public void tableNameToDBOClassName_PassTableSOWithNameUnderScore_ReturnsACorrectDBOName() {
		// Prepare
		String expected = "TableNameDBO";
		TableSO tableSO = new TableSO().setName("TABLE_NAME");
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

}