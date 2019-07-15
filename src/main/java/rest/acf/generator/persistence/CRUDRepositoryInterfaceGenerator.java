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
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.InterfaceSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for CRUD repository interfaces.
 *
 * @author ollie
 *
 */
public class CRUDRepositoryInterfaceGenerator {

	private static final Logger LOG = Logger.getLogger(CRUDRepositoryInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new CRUD repository interface generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public CRUDRepositoryInterfaceGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a CRUD repository interface for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A CRUD repository interface for passed database table or a "null" value if a "null" value is passed.
	 */
	public InterfaceSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		List<ColumnSO> pkMembers = getPrimaryKeyMembers(tableSO);
		if (pkMembers.size() != 1) {
			LOG.error("table '" + tableSO.getName() + "' has not a primary key with one member: " + pkMembers.size());
			return null;
		}
		String dboClassName = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO).getName();
		String jpaModelPackageName = this.classSourceModelUtils.createJPAModelPackageNameSuffix();
		InterfaceSourceModel ism = this.classSourceModelUtils.createCRUDRepitoryInterfaceSourceModel(tableSO);
		ism.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createCRUDRepositoryPackageNameSuffix()));
		this.classSourceModelUtils.addImport(ism, "org.springframework.data.repository", "CrudRepository");
		this.classSourceModelUtils.addImport(ism, "org.springframework.stereotype", "Repository");
		this.classSourceModelUtils.addImport(ism, "${base.package.name}." + jpaModelPackageName, dboClassName);
		this.classSourceModelUtils.addAnnotation(ism, "Repository");
		ism.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A CRUD repository for " + tableSO.getName().toLowerCase() + " access.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		String parentClassName = "CrudRepository<" + dboClassName + ", "
				+ this.typeConverter.typeSOToTypeString(pkMembers.get(0).getType(), true) + ">";
		ism.setExtendsModel(new ExtensionSourceModel().setParentClassName(parentClassName));
		return ism;
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