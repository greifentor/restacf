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
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.InterfaceSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * Unit tests for class "CRUDRepositoryInterfaceGenerator".
 *
 * @author ollie
 */
@ExtendWith(MockitoExtension.class)
public class CRUDRepositoryInterfaceGeneratorTest {

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

	private CRUDRepositoryInterfaceGenerator unitUnderTest;

	@BeforeEach
	public void setUp() {
		this.classSourceModelUtils = new ClassSourceModelUtils(this.nameConverter, this.typeConverter);
		this.unitUnderTest = new CRUDRepositoryInterfaceGenerator(this.classSourceModelUtils, this.nameConverter,
				this.typeConverter, new DatabaseSO());
	}

	@Test
	public void generate_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.generate(null, null), nullValue());
	}

	@Test
	public void generate_PassASimpleClassWithSimpleFields_ReturnsACorrectClassSourceModel() {
		// Prepare
		ColumnSO column0 = new ColumnSO().setName(COLUMN_NAME_0).setType(COLUMN_TYPE_0).setPkMember(true);
		ColumnSO column1 = new ColumnSO().setName(COLUMN_NAME_1).setType(COLUMN_TYPE_1);
		List<ColumnSO> columns = Arrays.asList(column0, column1);
		TableSO table = new TableSO().setName(TABLE_NAME).setColumns(columns);
		column0.setTable(table);
		column1.setTable(table);
		ImportSourceModel importCRUDRepository = new ImportSourceModel().setClassName("CrudRepository")
				.setPackageModel(new PackageSourceModel().setPackageName("org.springframework.data.repository"));
		ImportSourceModel importEntityAnnotation = new ImportSourceModel().setClassName("Repository")
				.setPackageModel(new PackageSourceModel().setPackageName("org.springframework.stereotype"));
		ImportSourceModel importRackDBOAnnotation = new ImportSourceModel().setClassName(TABLE_NAME + "DBO")
				.setPackageModel(new PackageSourceModel().setPackageName("${base.package.name}.persistence.dbo"));
		AnnotationSourceModel annotationRepository = new AnnotationSourceModel().setName("Repository");
		InterfaceSourceModel expected = new InterfaceSourceModel()
				.setComment(new ClassCommentSourceModel().setComment("/**\n" //
						+ " * A CRUD repository for " + TABLE_NAME.toLowerCase() + " access.\n" //
						+ " *\n" //
						+ " * @author " + AUTHOR_NAME + "\n" //
						+ " *\n" //
						+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
						+ " */\n"))
				.setPackageModel(new PackageSourceModel().setPackageName("${base.package.name}.persistence.repository"))
				.setName(TABLE_NAME + "Repository")
				.setImports(Arrays.asList(importCRUDRepository, importEntityAnnotation, importRackDBOAnnotation))
				.setAnnotations(Arrays.asList(annotationRepository));
		String parentClassName = "CrudRepository<" + TABLE_NAME + "DBO, Integer>";
		expected.setExtendsModel(new ExtensionSourceModel().setParentClassName(parentClassName));
		// Run
		InterfaceSourceModel returned = this.unitUnderTest.generate(table, "rest-acf");

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

}