package rest.acf.generator.persistence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;
import rest.acf.generator.persistence.JPAClassGenerator;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;

/**
 * Unit tests for class "JPAClassGenerator".
 *
 * @author ollie
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAClassGeneratorTest {

	private static final String COLUMN_NAME_0 = "Column0";
	private static final String COLUMN_NAME_1 = "Column1";
	private static final TypeSO COLUMN_TYPE_0 = new TypeSO().setSqlType(Types.INTEGER);
	private static final TypeSO COLUMN_TYPE_1 = new TypeSO().setSqlType(Types.VARCHAR).setPrecision(100);
	private static final String TABLE_NAME = "TestTable";

	@InjectMocks
	private JPAClassGenerator unitUnderTest;

	@Test
	public void generate_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.generate(null), nullValue());
	}

	@Test
	public void generate_PassASimpleClassWithSimpleFields_ReturnsACorrectClassSourceModel() {
		// Prepare
		ColumnSO column0 = new ColumnSO().setName(COLUMN_NAME_0).setType(COLUMN_TYPE_0);
		ColumnSO column1 = new ColumnSO().setName(COLUMN_NAME_1).setType(COLUMN_TYPE_1);
		List<ColumnSO> columns = Arrays.asList(column0, column1);
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(columns);

		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0");
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName(COLUMN_NAME_1);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel expected = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME + "Dbo");
		// Run
		ClassSourceModel returned = this.unitUnderTest.generate(table);

		// Check
		assertThat(returned, equalTo(expected));
	}

}