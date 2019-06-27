package rest.acf.generator.utils;

import static org.hamcrest.CoreMatchers.equalTo;
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
		// Vorbereitung
		String className = "className";
		String packageName = "pack.age.name";
		ClassSourceModel csm = new ClassSourceModel();
		ImportSourceModel expected = new ImportSourceModel()
				.setClassName(className).setPackageModel(
						new PackageSourceModel().setPackageName(packageName));
		// Ausführung
		this.unitUnderTest.addImport(csm, packageName, className);
		// Prüfung
		assertThat(csm.getImports().size(), equalTo(1));
		ImportSourceModel stored = csm.getImports().get(0);
		assertThat(stored, equalTo(expected));
	}

	@Test
	public void addAnnotation_ClassSourceModelAndAnnotationInfosPassed_AddsANewAnnotationToTheClassSourceModel() {
		// Vorbereitung
		String name = "annotation";
		String propertyName = "propertyName";
		String propertyValue = "propertyValue";
		ClassSourceModel csm = new ClassSourceModel();
		AnnotationSourceModel expected = new AnnotationSourceModel()
				.setName(name)
				.setProperties(Arrays.asList(new PropertySourceModel<>()
						.setName(propertyName).setContent(propertyValue)));
		// Ausführung
		this.unitUnderTest.addAnnotation(csm, name, propertyName,
				propertyValue);
		// Prüfung
		assertThat(csm.getAnnotations().size(), equalTo(1));
		AnnotationSourceModel stored = csm.getAnnotations().get(0);
		assertThat(stored, equalTo(expected));
	}

	@Test
	public void addAttributeForColumn_ClassSourceModelAndColumnServiceObjectPassed_AddsANewAttributeToTheClassSourceModel() {
		// Vorbereitung
		String attributeName = "attributeName";
		String typeName = "int";
		ColumnSO column = new ColumnSO().setName(attributeName)
				.setType(new TypeSO().setSqlType(Types.INTEGER));
		ClassSourceModel csm = new ClassSourceModel();
		AttributeSourceModel expected = new AttributeSourceModel()
				.setName(attributeName).setType(typeName);
		// Ausführung
		this.unitUnderTest.addAttributeForColumn(csm, column);
		// Prüfung
		assertThat(csm.getAttributes().size(), equalTo(1));
		AttributeSourceModel stored = csm.getAttributes().get(0);
		assertThat(stored, equalTo(expected));
	}

}