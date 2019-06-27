package rest.acf.generator.persistence;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * Unit tests for class "JPAClassGenerator".
 *
 * @author ollie
 */
@RunWith(MockitoJUnitRunner.class)
public class JPAClassGeneratorTest {

	private static final String COLUMN_NAME_0 = "Column0";
	private static final String COLUMN_NAME_1 = "Column1";
	private static final TypeSO COLUMN_TYPE_0 = new TypeSO()
			.setSqlType(Types.INTEGER);
	private static final TypeSO COLUMN_TYPE_1 = new TypeSO()
			.setSqlType(Types.VARCHAR).setLength(100);
	private static final String TABLE_NAME = "TestTable";

	private ClassSourceModelUtils classSourceModelUtils;
	@Spy
	private NameConverter nameConverter;
	@Spy
	private TypeConverter typeConverter;

	private JPAClassGenerator unitUnderTest;

	@Before
	public void setUp() {
		this.classSourceModelUtils = new ClassSourceModelUtils(
				this.nameConverter, this.typeConverter);
		this.unitUnderTest = new JPAClassGenerator(this.classSourceModelUtils,
				this.nameConverter, this.typeConverter);
	}

	@Test
	public void generate_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.generate(null), nullValue());
	}

	@Test
	public void generate_PassASimpleClassWithSimpleFields_ReturnsACorrectClassSourceModel() {
		// Prepare
		ColumnSO column0 = new ColumnSO().setName(COLUMN_NAME_0)
				.setType(COLUMN_TYPE_0);
		ColumnSO column1 = new ColumnSO().setName(COLUMN_NAME_1)
				.setType(COLUMN_TYPE_1);
		List<ColumnSO> columns = Arrays.asList(column0, column1);
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(columns);

		ImportSourceModel importEntityAnnotation = new ImportSourceModel()
				.setClassName("Entity").setPackageModel(new PackageSourceModel()
						.setPackageName("javax.persistence"));
		ImportSourceModel importTableAnnotation = new ImportSourceModel()
				.setClassName("Table").setPackageModel(new PackageSourceModel()
						.setPackageName("javax.persistence"));
		AnnotationSourceModel annotationEntity = new AnnotationSourceModel()
				.setName("Entity");
		AnnotationSourceModel annotationTable = new AnnotationSourceModel()
				.setName("Table")
				.setProperties(Arrays.asList(new PropertySourceModel<String>()
						.setName("name").setContent(TABLE_NAME)));
		AttributeSourceModel attribute0 = new AttributeSourceModel()
				.setName("column0").setType("int");
		AttributeSourceModel attribute1 = new AttributeSourceModel()
				.setName("column1").setType("String");
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0,
				attribute1);
		ClassSourceModel expected = new ClassSourceModel()
				.setAttributes(attributes).setName(TABLE_NAME + "DBO")
				.setImports(Arrays.asList(importEntityAnnotation,
						importTableAnnotation))
				.setAnnotations(
						Arrays.asList(annotationEntity, annotationTable));
		// Run
		ClassSourceModel returned = this.unitUnderTest.generate(table);

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

}