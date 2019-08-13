package rest.acf.generator.service;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for service objects.
 *
 * @author ollie
 *
 */
public class SOClassGenerator implements ClassCodeFactory {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public SOClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		ClassSourceModel csm = this.classSourceModelUtils.createSOClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel()
				.setPackageName("${base.package.name}." + this.classSourceModelUtils.createSOPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "lombok", "Data");
		this.classSourceModelUtils.addImport(csm, "lombok.experimental", "Accessors");
		this.classSourceModelUtils.addAnnotation(csm, "Accessors", "chain", true);
		this.classSourceModelUtils.addAnnotation(csm, "Data");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A service object class for " + tableSO.getName().toLowerCase() + "s.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		for (ColumnSO column : tableSO.getColumns()) {
			this.classSourceModelUtils
					.addAttributeForColumn(csm, column, t -> this.nameConverter.tableNameToServiceObjectClassName(t))
					.ifPresent(asm -> {
						asm.getModifiers().add(ModifierSourceModel.PRIVATE);
					});
		}
		return csm;
	}

}