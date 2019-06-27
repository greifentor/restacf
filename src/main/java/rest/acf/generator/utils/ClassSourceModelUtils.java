package rest.acf.generator.utils;

import java.util.List;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * A utility class for class source models.
 * 
 * @author Oliver.Lieshoff
 *
 */
public class ClassSourceModelUtils {

	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new class source model utility.
	 *
	 * @param nameConverter
	 *            An access to the name converter of the application.
	 * @param typeConverter
	 *            An access to the type converter of the application.
	 */
	public ClassSourceModelUtils(NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Adds an import for the passed package name and class to the passed class
	 * source model.
	 * 
	 * @param csm
	 *            The class source model which the import is to add to.
	 * @param packageName
	 *            The name of the package to import.
	 * @param className
	 *            The name of the class to import.
	 */
	public void addImport(ClassSourceModel csm, String packageName,
			String className) {
		List<ImportSourceModel> imports = csm.getImports();
		imports.add(
				new ImportSourceModel().setClassName(className).setPackageModel(
						new PackageSourceModel().setPackageName(packageName)));
	}

	/**
	 * Adds an annotation to the passed class source model.
	 * 
	 * @param csm
	 *            The class source model which the annotation is to add to.
	 * @param name
	 *            The name of the annotation.
	 * @param propertyInfo
	 *            Information about properties which should be set for the
	 *            annotation. Could be left empty if no properties should be
	 *            passed with the annotation.
	 */
	public void addAnnotation(ClassSourceModel csm, String name,
			Object... propertyInfo) {
		AnnotationSourceModel asm = new AnnotationSourceModel().setName(name);
		if (propertyInfo.length > 0) {
			for (int i = 0, leni = propertyInfo.length; i < leni; i = i + 2) {
				asm.getProperties()
						.add(new PropertySourceModel<>()
								.setName(String.valueOf(propertyInfo[i]))
								.setContent(propertyInfo[i + 1]));
			}
		}
		csm.getAnnotations().add(asm);
	}

	/**
	 * Adds an attribute for the passed column service object to the passed
	 * class source model.
	 * 
	 * @param csm
	 *            The class source model which the attribute is to add to.
	 * @param column
	 *            The column service object which the attribute source model is
	 *            to add for.
	 */
	public void addAttributeForColumn(ClassSourceModel csm, ColumnSO column) {
		String attributeName = this.nameConverter
				.columnNameToAttributeName(column);
		String typeName = this.typeConverter
				.typeSOToTypeString(column.getType(), column.isNullable());
		csm.getAttributes().add(new AttributeSourceModel()
				.setName(attributeName).setType(typeName));
	}

}