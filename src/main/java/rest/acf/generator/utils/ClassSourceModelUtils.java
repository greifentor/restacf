package rest.acf.generator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
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
	 * @param csm                      The class source model which the attribute is to add to.
	 * @param column                   The column service object which the attribute source model is to add for.
	 * @param referenceClassNameFinder A name finder for the referenced class.
	 * @return An optional with the added attribute source model or an empty optional if no attribute source model could
	 *         be added.
	 */
	public Optional<AttributeSourceModel> addAttributeForColumn(ClassSourceModel csm, ColumnSO column,
			NameFinder<TableSO> referenceClassNameFinder) {
		String attributeName = this.nameConverter.columnNameToAttributeName(column);
		AttributeSourceModel asm = null;
		ForeignKeySO[] foreignKeys = getForeignkeyByColumn(column);
		if (foreignKeys.length == 0) {
			String typeName = this.typeConverter.typeSOToTypeString(column.getType(), column.isNullable());
			asm = new AttributeSourceModel().setName(attributeName).setType(typeName);
			csm.getAttributes().add(asm);
			if (typeName.equals("LocalDate")) {
				addImport(csm, "java.time", "LocalDate");
			} else if (typeName.equals("LocalTime")) {
				addImport(csm, "java.time", "LocalTime");
			} else if (typeName.equals("LocalDateTime")) {
				addImport(csm, "java.time", "LocalDateTime");
			}
		} else if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
			ReferenceSO reference = foreignKeys[0].getReferences().get(0);
			String referencedClassName = (referenceClassNameFinder != null
					? referenceClassNameFinder.getName(reference.getReferencedColumn().getTable())
					: reference.getReferencedColumn().getTable().getName());
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
	 * Adds an attribute for the passed class name to the passed class source model.
	 * 
	 * @param csm       The class source model which the attribute is to add to.
	 * @param className The class name which an attribute is to ad for.
	 * @return An optional with the added attribute source model or an empty optional if no attribute source model could
	 *         be added.
	 */
	public Optional<AttributeSourceModel> addAttributeForClassName(ClassSourceModel csm, String className) {
		AttributeSourceModel asm = new AttributeSourceModel().setName(this.nameConverter.classNameToAttrName(className))
				.setType(className);
		csm.getAttributes().add(asm);
		return Optional.of(asm);
	}

	/**
	 * Adds an attribute for the passed class name to the passed class source model.
	 * 
	 * @param csm          The class source model which the attribute is to add to.
	 * @param className    The class name which an attribute is to ad for.
	 * @param initialValue An initial value for the attribute.
	 * @return An optional with the added attribute source model or an empty optional if no attribute source model could
	 *         be added.
	 */
	public Optional<AttributeSourceModel> addAttributeForClassName(ClassSourceModel csm, String className,
			String initialValue) {
		AttributeSourceModel asm = new AttributeSourceModel().setName(this.nameConverter.classNameToAttrName(className))
				.setType(className).setInitialValue(initialValue);
		csm.getAttributes().add(asm);
		return Optional.of(asm);
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
		return addAttributeForColumn(csm, column, null);
	}

	/**
	 * Creates a new application class source model based on the passed database service object.
	 *
	 * @param databaseSO The database service object which the class source model is to create for.
	 * @return An class source model for an application class based on the passed database service object.
	 */
	public ClassSourceModel createApplicationClassSourceModel(DatabaseSO databaseSO) {
		return new ClassSourceModel().setName(this.nameConverter.schemeNameToApplicationClassName(databaseSO));
	}

	/**
	 * Creates a new CRUD repository interface source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the interface source model is to create for.
	 * @return An interface source model for a CRUD repository interface based on the passed table service object.
	 */
	public InterfaceSourceModel createCRUDRepitoryInterfaceSourceModel(TableSO tableSO) {
		return new InterfaceSourceModel().setName(this.nameConverter.tableNameToRepositoryInterfaceName(tableSO));
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
	 * Creates a new DBO converter class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a DBO converter class based on the passed table service object.
	 */
	public ClassSourceModel createDBOConverterClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToDBOConverterClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the DBO converter generated class.
	 * 
	 * @return The package name suffix for the DBO converter generated class.
	 */
	public String createDBOConverterPackageNameSuffix() {
		return "persistence.converter";
	}

	/**
	 * Creates a new DTO class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a DTO class based on the passed table service object.
	 */
	public ClassSourceModel createDTOClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToDTOClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the DBO generated class.
	 * 
	 * @return The package name suffix for the DBO generated class.
	 */
	public String createDTOPackageNameSuffix() {
		return "rest.v1.dto";
	}

	/**
	 * Creates a new DTO converter class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a DTO converter class based on the passed table service object.
	 */
	public ClassSourceModel createDTOConverterClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToDTOConverterClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the DTO converter generated class.
	 * 
	 * @return The package name suffix for the DTO converter generated class.
	 */
	public String createDTOConverterPackageNameSuffix() {
		return "rest.v1.converter";
	}

	/**
	 * Creates a new JPA model class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return A class source model for a JPA model class based on the passed table service object.
	 */
	public ClassSourceModel createJPAModelClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToDBOClassName(tableSO));
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
	 * Creates a new persistence adapter class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a persistence adapter class based on the passed table service object.
	 */
	public ClassSourceModel createPersistenceAdapterClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToPersistenceAdapterClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the persistence adapter generated class.
	 * 
	 * @return The package name suffix for the persistence adapter generated class.
	 */
	public String createPersistenceAdapterPackageNameSuffix() {
		return "persistence.adapter";
	}

	/**
	 * Returns the package name suffix for the persistence exception generated class.
	 * 
	 * @return The package name suffix for the persistence exception generated class.
	 */
	public String createPersistenceExceptionPackageNameSuffix() {
		return "service.persistence.exception";
	}

	/**
	 * Creates a new persistence exception class source model.
	 *
	 * @return A new persistence exception class source model.
	 */
	public ClassSourceModel createPersistenceExceptionClassSourceModel() {
		return new ClassSourceModel().setName("PersistenceException");
	}

	/**
	 * Creates a new persistence port class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a persistence port class based on the passed table service object.
	 */
	public InterfaceSourceModel createPersistencePortInterfaceSourceModel(TableSO tableSO) {
		return new InterfaceSourceModel().setName(this.nameConverter.tableNameToPersistencePortInterfaceName(tableSO));
	}

	/**
	 * Returns the package name suffix for the persistence port generated class.
	 * 
	 * @return The package name suffix for the persistence port generated class.
	 */
	public String createPersistencePortPackageNameSuffix() {
		return "service.persistence.port";
	}

	/**
	 * Creates a new REST controller class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return A class source model for a REST controller class based on the passed table service object.
	 */
	public ClassSourceModel createRESTControllerClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToRESTControllerClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the REST controller generated class.
	 * 
	 * @return The package name suffix for the REST controller generated class.
	 */
	public String createRESTControllerClassPackageNameSuffix() {
		return "rest.v1.controller";
	}

	/**
	 * Creates a new service class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a service class based on the passed table service object.
	 */
	public InterfaceSourceModel createServiceInterfaceSourceModel(TableSO tableSO) {
		return new InterfaceSourceModel().setName(this.nameConverter.tableNameToServiceInterfaceName(tableSO));
	}

	/**
	 * Returns the package name suffix for the service generated class.
	 * 
	 * @return The package name suffix for the service generated class.
	 */
	public String createServicePackageNameSuffix() {
		return "service";
	}

	/**
	 * Creates a new service impl class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a service impl class based on the passed table service object.
	 */
	public ClassSourceModel createServiceImplClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToServiceImplClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the service impl generated class.
	 * 
	 * @return The package name suffix for the service impl generated class.
	 */
	public String createServiceImplPackageNameSuffix() {
		return "service.impl";
	}

	/**
	 * Creates a new service object class source model based on the passed table service object.
	 *
	 * @param tableSO The table service object which the class source model is to create for.
	 * @return An interface source model for a service object class based on the passed table service object.
	 */
	public ClassSourceModel createSOClassSourceModel(TableSO tableSO) {
		return new ClassSourceModel().setName(this.nameConverter.tableNameToServiceObjectClassName(tableSO));
	}

	/**
	 * Returns the package name suffix for the persistence port generated class.
	 * 
	 * @return The package name suffix for the persistence port generated class.
	 */
	public String createSOPackageNameSuffix() {
		return "service.so";
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