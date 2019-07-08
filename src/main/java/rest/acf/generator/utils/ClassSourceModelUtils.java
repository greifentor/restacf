package rest.acf.generator.utils;

import java.util.List;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import rest.acf.RESTServerCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AnnotationBearer;
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

	private static final Logger LOG = Logger.getLogger(RESTServerCodeFactory.class);

	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new class source model utility.
	 *
	 * @param nameConverter An access to the name converter of the application.
	 * @param typeConverter An access to the type converter of the application.
	 */
	public ClassSourceModelUtils(NameConverter nameConverter, TypeConverter typeConverter) {
		super();
		LOG.debug("Created ClassSourceModelUtils");
		LOG.debug("    nameConverter: " + nameConverter);
		this.nameConverter = nameConverter;
		LOG.debug("    typeConverter: " + typeConverter);
		this.typeConverter = typeConverter;
	}

	/**
	 * Adds an import for the passed package name and class to the passed class source model.
	 * 
	 * @param csm         The class source model which the import is to add to.
	 * @param packageName The name of the package to import.
	 * @param className   The name of the class to import.
	 * @return The add import source model.
	 */
	public ImportSourceModel addImport(ClassSourceModel csm, String packageName, String className) {
		List<ImportSourceModel> imports = csm.getImports();
		ImportSourceModel ism = new ImportSourceModel().setClassName(className)
				.setPackageModel(new PackageSourceModel().setPackageName(packageName));
		imports.add(ism);
		LOG.debug("Added import '" + ism + "' to class source model: " + csm);
		return ism;
	}

	/**
	 * Adds an annotation to the passed annotation bearer.
	 * 
	 * @param ab           The annotation bearer which the annotation is to add to.
	 * @param name         The name of the annotation.
	 * @param propertyInfo Information about properties which should be set for the annotation. Could be left empty if
	 *                     no properties should be passed with the annotation.
	 * @return The annotation source model which has been added.
	 */
	public AnnotationSourceModel addAnnotation(AnnotationBearer ab, String name, Object... propertyInfo) {
		AnnotationSourceModel asm = new AnnotationSourceModel().setName(name);
		if (propertyInfo.length > 0) {
			for (int i = 0, leni = propertyInfo.length; i < leni; i = i + 2) {
				asm.getProperties().add(new PropertySourceModel<>().setName(String.valueOf(propertyInfo[i]))
						.setContent(propertyInfo[i + 1]));
			}
		}
		ab.getAnnotations().add(asm);
		LOG.debug("Added annotation '" + asm + "' to annotation bearer: " + ab);
		return asm;
	}

	/**
	 * Adds an attribute for the passed column service object to the passed class source model.
	 * 
	 * @param csm    The class source model which the attribute is to add to.
	 * @param column The column service object which the attribute source model is to add for.
	 * @return The added attribute source model.
	 */
	public AttributeSourceModel addAttributeForColumn(ClassSourceModel csm, ColumnSO column) {
		String attributeName = this.nameConverter.columnNameToAttributeName(column);
		String typeName = this.typeConverter.typeSOToTypeString(column.getType(), column.isNullable());
		AttributeSourceModel asm = new AttributeSourceModel().setName(attributeName).setType(typeName);
		csm.getAttributes().add(asm);
		LOG.debug("Added attribute '" + asm + "' to class source model: " + csm);
		return asm;
	}

}