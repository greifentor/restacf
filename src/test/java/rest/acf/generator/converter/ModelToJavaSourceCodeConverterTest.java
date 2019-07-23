package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * Unit tests of the class "ModelToJavaSourceCodeConverter".
 *
 * @author Oliver.Lieshoff
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelToJavaSourceCodeConverterTest {

	private static final String PACKAGE_NAME = "de.tst.pack.age";
	private static final String TABLE_NAME = "TestTable";

	@InjectMocks
	private ModelToJavaSourceCodeConverter unitUnderTest;

	@Test
	public void classSourceModelToJavaSourceCode_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.classSourceModelToJavaSourceCode(null), nullValue());
	}

	@Test
	public void classSourceModelToJavaSourceCode_PassASimpleClassSourceModel_ReturnsACorrectSourceCode() {
		// Prepare
		String expected = "package " + PACKAGE_NAME + ";\n\n" //
				+ "public class " + TABLE_NAME + " {\n" //
				+ "\n" //
				+ "\tprivate int column0;\n" //
				+ "\tprivate String column1;\n" //
				+ "\n" //
				+ "}";
		PackageSourceModel packageModel = new PackageSourceModel().setPackageName(PACKAGE_NAME);
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int");
		attribute0.getModifiers().add(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String");
		attribute1.getModifiers().add(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel classSourceModel = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME)
				.setPackageModel(packageModel);
		// Run
		String returned = this.unitUnderTest.classSourceModelToJavaSourceCode(classSourceModel);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classSourceModelToJavaSourceCode_PassASimpleClassSourceModelWithAnAnnotation_ReturnsACorrectSourceCode() {
		// Prepare
		String expected = "@Entity\n" //
				+ "public class " + TABLE_NAME + " {\n" //
				+ "\n" //
				+ "\tprivate int column0;\n" //
				+ "\tprivate String column1;\n" //
				+ "\n" //
				+ "}";
		AnnotationSourceModel annotation = new AnnotationSourceModel().setName("Entity");
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int");
		attribute0.getModifiers().add(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String");
		attribute1.getModifiers().add(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel classSourceModel = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME)
				.setAnnotations(Arrays.asList(annotation));
		// Run
		String returned = this.unitUnderTest.classSourceModelToJavaSourceCode(classSourceModel);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classSourceModelToJavaSourceCode_PassASimpleClassSourceModelWithAParameterizedAnnotation_ReturnsACorrectSourceCode() {
		// Prepare
		String expected = "@Annotation(name = \"aName\", value = 4711)\n" //
				+ "public class " + TABLE_NAME + " {\n" //
				+ "\n" //
				+ "\tprivate int column0;\n" //
				+ "\tprivate String column1;\n" //
				+ "\n" //
				+ "}";
		AnnotationSourceModel annotation = new AnnotationSourceModel().setName("Annotation")
				.setProperties(Arrays.asList(new PropertySourceModel<String>().setName("name").setContent("aName"),
						new PropertySourceModel<Integer>().setName("value").setContent(4711)));
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int");
		attribute0.getModifiers().add(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String");
		attribute1.getModifiers().add(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel classSourceModel = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME)
				.setAnnotations(Arrays.asList(annotation));
		// Run
		String returned = this.unitUnderTest.classSourceModelToJavaSourceCode(classSourceModel);
		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	@Test
	public void classSourceModelToJavaSourceCode_PassASimpleClassSourceModelWithAnImports_ReturnsACorrectSourceCode() {
		// Prepare
		String expected = "import imported.pack.age.Class;\n\n" //
				+ "import another.pack.age.*;\n\n" //
				+ "public class " + TABLE_NAME + " {\n" //
				+ "\n" //
				+ "\tprivate int column0;\n" //
				+ "\tprivate String column1;\n" //
				+ "\n" //
				+ "}";
		ImportSourceModel importSingleClass = new ImportSourceModel()
				.setPackageModel(new PackageSourceModel().setPackageName("imported.pack.age")).setClassName("Class");
		ImportSourceModel importWholePackage = new ImportSourceModel()
				.setPackageModel(new PackageSourceModel().setPackageName("another.pack.age"));
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int");
		attribute0.getModifiers().add(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String");
		attribute1.getModifiers().add(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel classSourceModel = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME)
				.setImports(Arrays.asList(importSingleClass, importWholePackage));
		// Run
		String returned = this.unitUnderTest.classSourceModelToJavaSourceCode(classSourceModel);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void classSourceModelToJavaSourceCode_PassASimpleClassSourceModelWithAttributeAnnotations_ReturnsACorrectSourceCode() {
		// Prepare
		String expected = "import imported.pack.age.Class;\n\n" //
				+ "import another.pack.age.*;\n\n" //
				+ "public class " + TABLE_NAME + " {\n" //
				+ "\n" //
				+ "\t@Id\n" //
				+ "\t@Column(name = \"Column0\")\n" //
				+ "\tprivate int column0;\n" //
				+ "\tprivate String column1;\n" //
				+ "\n" //
				+ "}";
		ImportSourceModel importSingleClass = new ImportSourceModel()
				.setPackageModel(new PackageSourceModel().setPackageName("imported.pack.age")).setClassName("Class");
		ImportSourceModel importWholePackage = new ImportSourceModel()
				.setPackageModel(new PackageSourceModel().setPackageName("another.pack.age"));
		AttributeSourceModel attribute0 = new AttributeSourceModel().setName("column0").setType("int");
		attribute0.setAnnotations(Arrays.asList(new AnnotationSourceModel().setName("Id"),
				new AnnotationSourceModel().setName("Column").setProperties(
						Arrays.asList(new PropertySourceModel<String>().setName("name").setContent("Column0")))));
		attribute0.getModifiers().add(ModifierSourceModel.PRIVATE);
		AttributeSourceModel attribute1 = new AttributeSourceModel().setName("column1").setType("String");
		attribute1.getModifiers().add(ModifierSourceModel.PRIVATE);
		List<AttributeSourceModel> attributes = Arrays.asList(attribute0, attribute1);
		ClassSourceModel classSourceModel = new ClassSourceModel().setAttributes(attributes).setName(TABLE_NAME)
				.setImports(Arrays.asList(importSingleClass, importWholePackage));
		// Run
		String returned = this.unitUnderTest.classSourceModelToJavaSourceCode(classSourceModel);
		// Check
		assertEquals(expected, returned);
	}

}