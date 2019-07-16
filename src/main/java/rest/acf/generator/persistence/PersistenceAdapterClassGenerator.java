package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for persistence adapter classes.
 *
 * @author ollie
 *
 */
public class PersistenceAdapterClassGenerator {

	private static final Logger LOG = Logger.getLogger(CRUDRepositoryInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new persistence adapter classes generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public PersistenceAdapterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a persistence adapter classes for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A persistence adapter classes for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		List<ColumnSO> pkMembers = getPrimaryKeyMembers(tableSO);
		if (pkMembers.size() != 1) {
			LOG.error("table '" + tableSO.getName() + "' has not a primary key with one member: " + pkMembers.size());
			return null;
		}
		String dboClassName = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO).getName();
		String dboConverterClassName = this.classSourceModelUtils.createDBOConverterClassSourceModel(tableSO).getName();
		String persistenceClassName = this.classSourceModelUtils.createPersistencePortInterfaceSourceModel(tableSO)
				.getName();
		String repositoryClassName = this.classSourceModelUtils.createPersistenceAdapterClassSourceModel(tableSO)
				.getName();
		String dboPackageName = this.classSourceModelUtils.createJPAModelPackageNameSuffix();
		String dboConverterPackageName = this.classSourceModelUtils.createDBOConverterPackageNameSuffix();
		String persistencePortPackageName = this.classSourceModelUtils.createPersistencePortPackageNameSuffix();
		String repositoryPackageName = this.classSourceModelUtils.createPersistenceAdapterPackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createPersistenceAdapterClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createPersistenceAdapterPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Service");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dboConverterPackageName,
				dboConverterClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dboPackageName, dboClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + repositoryPackageName, repositoryClassName);
		this.classSourceModelUtils.addAnnotation(csm, "Service");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A CRUD repository for " + tableSO.getName().toLowerCase() + " access.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		String parentClassName = "CrudRepository<" + dboClassName + ", "
				+ this.typeConverter.typeSOToTypeString(pkMembers.get(0).getType(), true) + ">";
		csm.setExtendsModel(new ExtensionSourceModel().setParentClassName(parentClassName));
		return csm;
	}

	private List<ColumnSO> getPrimaryKeyMembers(TableSO table) {
		List<ColumnSO> pkMembers = new ArrayList<>();
		for (ColumnSO column : table.getColumns()) {
			if (column.isPkMember()) {
				pkMembers.add(column);
			}
		}
		return pkMembers;
	}

}