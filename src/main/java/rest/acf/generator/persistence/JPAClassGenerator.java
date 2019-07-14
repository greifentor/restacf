package rest.acf.generator.persistence;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class JPAClassGenerator {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new JPA class generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public JPAClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a JPA mapping class for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A JPA mapping class for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		ClassSourceModel csm = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createJPAModelPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Column");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Entity");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Id");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Table");
		this.classSourceModelUtils.addAnnotation(csm, "Entity");
		this.classSourceModelUtils.addAnnotation(csm, "Table", "name", tableSO.getName());
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A mapping class " + tableSO.getName().toLowerCase() + " objects.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		for (ColumnSO column : tableSO.getColumns()) {
			this.classSourceModelUtils.addAttributeForColumn(csm, column).ifPresent(asm -> {
				if (column.isPkMember()) {
					this.classSourceModelUtils.addAnnotation(asm, "Id");
				}
				this.classSourceModelUtils.addAnnotation(asm, "Column", "name", column.getName());
				ForeignKeySO[] foreignKeys = this.classSourceModelUtils.getForeignkeyByColumn(column);
				if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
					this.classSourceModelUtils.addAnnotation(asm, "JoinColumn", "name", column.getName());
				}
			});
		}
		return csm;
	}

}