package rest.acf.generator.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.sql.Types;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * Unit test for class "ClassSourceModelUtils".
 *
 * @author Oliver.Lieshoff
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassSourceModelUtilsTest {

	@Spy
	private NameConverter nameConverter;
	@Spy
	private TypeConverter typeConverter;

	@InjectMocks
	private ClassSourceModelUtils unitUnderTest;

	@Test
	public void addImport_ClassSourceModelAndImportData_AddsANewImportToTheClassSourceModel() {
		// Prepare
		String className = "className";
		String packageName = "pack.age.name";
		ClassSourceModel csm = new ClassSourceModel();
		ImportSourceModel expected = new ImportSourceModel()
				.setClassName(className).setPackageModel(
						new PackageSourceModel().setPackageName(packageName));
		// Run
		ImportSourceModel returned = this.unitUnderTest.addImport(csm,
				packageName, className);
		// Check
		assertThat(csm.getImports().size(), equalTo(1));
		ImportSourceModel stored = csm.getImports().get(0);
		assertThat(stored, equalTo(expected));
		assertThat(returned, sameInstance(stored));
	}

	@Test
	public void addAnnotation_AttributeSourceModelAndAnnotationInfosPassed_AddsANewAnnotationToTheAttributeSourceModel() {
		// Prepare
		String name = "annotation";
		String propertyName = "propertyName";
		String propertyValue = "propertyValue";
		AttributeSourceModel asm = new AttributeSourceModel();
		AnnotationSourceModel expected = new AnnotationSourceModel()
				.setName(name)
				.setProperties(Arrays.asList(new PropertySourceModel<>()
						.setName(propertyName).setContent(propertyValue)));
		// Run
		AnnotationSourceModel returned = this.unitUnderTest.addAnnotation(asm,
				name, propertyName, propertyValue);
		// Check
		assertThat(asm.getAnnotations().size(), equalTo(1));
		AnnotationSourceModel stored = asm.getAnnotations().get(0);
		assertThat(stored, equalTo(expected));
		assertThat(returned, sameInstance(stored));
	}

	@Test
	public void addAnnotation_ClassSourceModelAndAnnotationInfosPassed_AddsANewAnnotationToTheClassSourceModel() {
		// Prepare
		String name = "annotation";
		String propertyName = "propertyName";
		String propertyValue = "propertyValue";
		ClassSourceModel csm = new ClassSourceModel();
		AnnotationSourceModel expected = new AnnotationSourceModel()
				.setName(name)
				.setProperties(Arrays.asList(new PropertySourceModel<>()
						.setName(propertyName).setContent(propertyValue)));
		// Run
		AnnotationSourceModel returned = this.unitUnderTest.addAnnotation(csm,
				name, propertyName, propertyValue);
		// Check
		assertThat(csm.getAnnotations().size(), equalTo(1));
		AnnotationSourceModel stored = csm.getAnnotations().get(0);
		assertThat(stored, equalTo(expected));
		assertThat(returned, sameInstance(stored));
	}

	@Test
	public void addAttributeForColumn_ClassSourceModelAndColumnServiceObjectPassed_AddsANewAttributeToTheClassSourceModel() {
		// Prepare
		String attributeName = "attributeName";
		String typeName = "int";
		ColumnSO column = new ColumnSO().setName(attributeName)
				.setType(new TypeSO().setSqlType(Types.INTEGER));
		ClassSourceModel csm = new ClassSourceModel();
		AttributeSourceModel expected = new AttributeSourceModel()
				.setName(attributeName).setType(typeName);
		// Run
		AttributeSourceModel stored = this.unitUnderTest
				.addAttributeForColumn(csm, column);
		// Check
		assertThat(csm.getAttributes().size(), equalTo(1));
		AttributeSourceModel returned = csm.getAttributes().get(0);
		assertThat(stored, equalTo(expected));
		assertThat(returned, sameInstance(stored));
	}

}