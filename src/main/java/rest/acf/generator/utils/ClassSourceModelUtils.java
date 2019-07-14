package rest.acf.generator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.RESTServerCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AnnotationBearer;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportBearer;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.InterfaceSourceModel;
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
	 * @param ib          An import bearer which the import is to add to.
	 * @param packageName The name of the package to import.
	 * @param className   The name of the class to import.
	 * @return The add import source model.
	 */
	public ImportSourceModel addImport(ImportBearer ib, String packageName, String className) {
		List<ImportSourceModel> imports = ib.getImports();
		ImportSourceModel ism = new ImportSourceModel().setClassName(className)
				.setPackageModel(new PackageSourceModel().setPackageName(packageName));
		imports.add(ism);
		LOG.debug("Added import '" + ism + "' to import bearer: " + ib);
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
	 * @return An optional with the added attribute source model or an empty optional if no attribute source model could
	 *         be added.
	 */
	public Optional<AttributeSourceModel> addAttributeForColumn(ClassSourceModel csm, ColumnSO column) {
		String attributeName = this.nameConverter.columnNameToAttributeName(column);
		AttributeSourceModel asm = null;
		ForeignKeySO[] foreignKeys = getForeignkeyByColumn(column);
		if (foreignKeys.length == 0) {
			String typeName = this.typeConverter.typeSOToTypeString(column.getType(), column.isNullable());
			asm = new AttributeSourceModel().setName(attributeName).setType(typeName);
			csm.getAttributes().add(asm);
		} else if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
			ReferenceSO reference = foreignKeys[0].getReferences().get(0);
			String referencedClassName = this.nameConverter
					.tableNameToDBOClassName(reference.getReferencedColumn().getTable());
			LOG.debug("Attribute '" + csm.getName() + "." + attributeName + "' is a reference to '"
					+ referencedClassName + "'.");
			asm = new AttributeSourceModel().setName(attributeName).setType(referencedClassName);
			csm.getAttributes().add(asm);
		} else {
			LOG.error("Too many references for attribute: " + attributeName);
			return Optional.empty();
		}
		LOG.debug("Added attribute '" + asm + "' to class source model: " + csm);
		return Optional.of(asm);
	}

	/**
	 * Creates a new CRUD repository interface source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the interface source model is to create for.
	 * @return An interface source model for a CRUD repository interface based on the passed table service object.
	 */
	public InterfaceSourceModel createCRUDRepitoryInterfaceSourceModel(TableSO tableSO) {
		return new InterfaceSourceModel().setName(tableSO.getName() + "Repository");
	}

	/**
	 * Returns the package name suffix for the CRUD repository generated interface.
	 * 
	 * @return The package name suffix for the CRUD repository generated interface.
	 */
	public String createCRUDRepositoryPackageNameSuffix() {
		return "persistence.repository";
	}

	/**
	 * Creates a new JPA model class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return A class source model for a JPA model class based on the passed table service object.
	 */
	public ClassSourceModel createJPAModelClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(tableSO.getName() + "DBO");
	}

	/**
	 * Returns the package name suffix for the JPA model generated class.
	 * 
	 * @return The package name suffix for the JPA model generated class.
	 */
	public String createJPAModelPackageNameSuffix() {
		return "persistence.dbo";
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