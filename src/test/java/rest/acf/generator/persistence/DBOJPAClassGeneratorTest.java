package rest.acf.generator.persistence;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * Unit tests for class "DBOJPAClassGenerator".
 *
 * @author ollie
 */
@ExtendWith(MockitoExtension.class)
public class DBOJPAClassGeneratorTest {

	private static final String AUTHOR_NAME = "rest-acf";
	private static final String COLUMN_NAME_0 = "Column0";
	private static final String COLUMN_NAME_1 = "Column1";
	private static final TypeSO COLUMN_TYPE_0 = new TypeSO().setSqlType(Types.INTEGER);
	private static final TypeSO COLUMN_TYPE_1 = new TypeSO().setSqlType(Types.VARCHAR).setLength(100);
	private static final String TABLE_NAME = "TestTable";

	private ClassSourceModelUtils classSourceModelUtils;
	@Spy
	private NameConverter nameConverter;
	@Spy
	private TypeConverter typeConverter;

	private DBOJPAClassGenerator unitUnderTest;

	@BeforeEach
	public void setUp() {
		this.classSourceModelUtils = new ClassSourceModelUtils(this.nameConverter, this.typeConverter);
		this.unitUnderTest = new DBOJPAClassGenerator(this.classSourceModelUtils, this.nameConverter,
				this.typeConverter);
	}

	@Test
	public void generate_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.generate(null, null), nullValue());
	}

	@Test
	public void generate_PassASimpleClassWithSimpleFields_ReturnsACorrectClassSourceModel() {
		// Prepare
		ColumnSO column0 = new ColumnSO().setName(COLUMN_NAME_0).setNullable(false).setType(COLUMN_TYPE_0)
				.setPkMember(true);
		ColumnSO column1 = new ColumnSO().setName(COLUMN_NAME_1).setType(COLUMN_TYPE_1);
		List<ColumnSO> columns = Arrays.asList(column0, column1);
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(columns);
		column0.setTable(table);
		column1.setTable(table);
		ImportSourceModel importColumnAnnotation = new ImportSourceModel().setClassName("Column")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence"));
		ImportSourceModel importEntityAnnotation = new ImportSourceModel().setClassName("Entity")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence"));
		ImportSourceModel importIdAnnotation = new ImportSourceModel().setClassName("Id")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence"));
		ImportSourceModel importTableAnnotation = new ImportSourceModel().setClassName("Table")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence"));
		ImportSourceModel importData = new ImportSourceModel().setClassName("Data")
				.setPackageModel(new PackageSourceModel().setPackageName("lombok"));
		ImportSourceModel importAccessors = new ImportSourceModel().setClassName("Accessors")
				.setPackageModel(new PackageSourceModel().setPackageName("lombok.experimental"));
		AnnotationSourceModel annotationAccessors = new AnnotationSourceModel().setName("Accessors")
				.setProperties(Arrays.asList(new PropertySourceModel<String>().setName("chain").setContent("true")));
		AnnotationSourceModel annotationData = new AnnotationSourceModel().setName("Data");
		AnnotationSourceModel annotationEntity = new AnnotationSourceModel().setName("Entity")
				.setProperties(Arrays.asList(new PropertySourceModel<String>().setName("name").setContent(TABLE_NAME)));
		AnnotationSourceModel annotationTable = new AnnotationSourceModel().setName("Table")
				.setProperties(Arrays.asList(new PropertySourceModel<String>().setName("name").setContent(TABLE_NAME)));
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int")
				.setAnnotations(Arrays.asList(new AnnotationSourceModel().setName("Id"),
						new AnnotationSourceModel().setName("Column").setProperties(Arrays
								.asList(new PropertySourceModel<String>().setName("name").setContent(COLUMN_NAME_0)))));
		attribute0.addModifier(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String")
				.setAnnotations(Arrays.asList(new AnnotationSourceModel().setName("Column").setProperties(
						Arrays.asList(new PropertySourceModel<String>().setName("name").setContent(COLUMN_NAME_1)))));
		attribute1.addModifier(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel expected = new ClassSourceModel().setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A ORM mapping and database access class for " + TABLE_NAME.toLowerCase() + "s.\n" //
				+ " *\n" //
				+ " * @author " + AUTHOR_NAME + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"))
				.setPackageModel(new PackageSourceModel().setPackageName("${base.package.name}.persistence.dbo"))
				.setAttributes(attributes).setName(TABLE_NAME + "DBO")
				.setImports(Arrays.asList(importColumnAnnotation, importEntityAnnotation, importIdAnnotation,
						importTableAnnotation, importData, importAccessors))
				.setAnnotations(Arrays.asList(annotationAccessors, annotationData, annotationEntity, annotationTable));
		// Run
		ClassSourceModel returned = this.unitUnderTest.generate(table, "rest-acf");

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

}