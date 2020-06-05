package rest.acf.generator.persistence;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class DBOJPAClassGenerator implements ClassCodeFactory {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public DBOJPAClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	@Override
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
		this.classSourceModelUtils.addImport(csm, "lombok", "Data");
		this.classSourceModelUtils.addImport(csm, "lombok.experimental", "Accessors");
		this.classSourceModelUtils.addAnnotation(csm, "Accessors", "chain", true);
		this.classSourceModelUtils.addAnnotation(csm, "Data");
		this.classSourceModelUtils.addAnnotation(csm, "Entity", "name", csm.getName().replace("DBO", ""));
		this.classSourceModelUtils.addAnnotation(csm, "Table", "name", tableSO.getName());
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A ORM mapping and database access class for " + tableSO.getName().toLowerCase() + "s.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		for (ColumnSO column : tableSO.getColumns()) {
			this.classSourceModelUtils
					.addAttributeForColumn(csm, column, t -> this.nameConverter.tableNameToDBOClassName(t))
					.ifPresent(asm -> {
						if (column.isPkMember()) {
							this.classSourceModelUtils.addAnnotation(asm, "Id");
						}
						ForeignKeySO[] foreignKeys = this.classSourceModelUtils.getForeignkeyByColumn(column);
						if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
							this.classSourceModelUtils.addAnnotation(asm, "ManyToOne");
							this.classSourceModelUtils.addAnnotation(asm, "JoinColumn", "name", column.getName(),
									"referencedColumnName",
									foreignKeys[0].getReferences().get(0).getReferencedColumn().getName());
						} else {
							this.classSourceModelUtils.addAnnotation(asm, "Column", "name", column.getName());
						}
						asm.getModifiers().add(ModifierSourceModel.PRIVATE);
					});
		}
		for (ForeignKeySO fk : tableSO.getForeignKeys()) {
			if (fk.getReferences().size() == 1) {
				this.classSourceModelUtils.addImport(csm, "javax.persistence", "JoinColumn");
				this.classSourceModelUtils.addImport(csm, "javax.persistence", "ManyToOne");
			}
		}
		return csm;
	}

}