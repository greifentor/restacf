package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.InterfaceSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for CRUD repository interfaces.
 *
 * @author ollie
 *
 */
public class CRUDRepositoryInterfaceGenerator {

	private static final Logger LOG = Logger.getLogger(CRUDRepositoryInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final DatabaseSO databaseSO;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public CRUDRepositoryInterfaceGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter, DatabaseSO databaseSO) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.databaseSO = databaseSO;
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
		this.classSourceModelUtils.getReferencedColumns(tableSO, this.databaseSO) //
				.forEach(columnSO -> {
					this.classSourceModelUtils.addImport(ism, "java.util", "List");
					this.classSourceModelUtils.addImport(ism, "org.springframework.data.jpa.repository", "Query");
					ism.getMethods().add(createFindXByY(columnSO, tableSO, dboClassName));
				});
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

	private MethodSourceModel createFindXByY(ColumnSO columnSO, TableSO tableSO, String dboClassName) {
		String letter = this.nameConverter.getSingularName(tableSO).substring(0, 1).toLowerCase();
		return new MethodSourceModel() //
				.addAnnotations(new AnnotationSourceModel().setName("Query").setValue("SELECT " + letter //
						+ " FROM " + this.nameConverter.getSingularName(tableSO) + " " + letter //
						+ " WHERE " + letter + "."
						+ this.nameConverter.columnNameToAttributeName(getReferencingColum(columnSO, tableSO)) + "."
						+ this.nameConverter.columnNameToAttributeName(columnSO) + "=?1")) //
				.setName("find" + this.nameConverter.getPluralName(tableSO) + "For"
						+ this.nameConverter.getSingularName(columnSO.getTable())) //
				.setReturnType("List<" + dboClassName + ">") //
				.addParameters(new ParameterSourceModel() //
						.setName(this.nameConverter.columnNameToAttributeName(columnSO, true)) //
						.setType(this.typeConverter.typeSOToTypeString(columnSO.getType(), columnSO.isNullable())));
	}

	private ColumnSO getReferencingColum(ColumnSO columnSO, TableSO tableSO) {
		for (ForeignKeySO foreignKey : tableSO.getForeignKeys()) {
			for (ReferenceSO reference : foreignKey.getReferences()) {
				if (reference.getReferencedColumn() == columnSO) {
					return reference.getReferencingColumn();
				}
			}
		}
		return null;
	}

}