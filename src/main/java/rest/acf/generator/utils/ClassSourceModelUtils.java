package rest.acf.generator.utils;

import java.util.ArrayList;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.ReferenceSO;
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
		this.nameConverter = nameConverter;
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
		AttributeSourceModel asm = null;
		// if (getForeignkeyByColumn(column).length == 0) {
		String typeName = this.typeConverter.typeSOToTypeString(column.getType(), column.isNullable());
		asm = new AttributeSourceModel().setName(attributeName).setType(typeName);
		csm.getAttributes().add(asm);
		// }
		return asm;
	}

	/**
	 * Returns the foreign key service objects which contain the passed column service object as referencing column.
	 * 
	 * @param column The column which the related foreign keys are to get for.
	 * @return The foreign key service object which the passed column acts a referencing column for. An empty array will
	 *         returned in case of no related foreign keys found..
	 */
	public ForeignKeySO[] getForeignkeyByColumn(ColumnSO column) {
		if (column == null) {
			return null;
		}
		List<ForeignKeySO> fks = new ArrayList<>();
		for (ForeignKeySO fk : column.getTable().getForeignKeys()) {
			for (ReferenceSO reference : fk.getReferences()) {
				if (reference.getReferencingColumn().equals(column)) {
					fks.add(fk);
				}
			}
		}
		return fks.toArray(new ForeignKeySO[fks.size()]);
	}

}