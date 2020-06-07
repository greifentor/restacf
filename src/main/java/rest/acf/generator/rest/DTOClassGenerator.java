package rest.acf.generator.rest;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationValue;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for data transfer objects.
 *
 * @author ollie
 *
 */
public class DTOClassGenerator implements ClassCodeFactory {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public DTOClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		ClassSourceModel csm = this.classSourceModelUtils.createDTOClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel()
				.setPackageName("${base.package.name}." + this.classSourceModelUtils.createDTOPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "lombok", "Data");
		this.classSourceModelUtils.addImport(csm, "lombok.experimental", "Accessors");
		this.classSourceModelUtils.addAnnotation(csm, "Accessors", "chain",
				new AnnotationValue().setQuoted(false).setValue("true"));
		this.classSourceModelUtils.addAnnotation(csm, "Data");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A data transfer object class for " + tableSO.getName().toLowerCase() + "s.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		for (ColumnSO column : tableSO.getColumns()) {
			this.classSourceModelUtils
					.addAttributeForColumn(csm, column, t -> this.nameConverter.tableNameToDTOClassName(t))
					.ifPresent(asm -> {
						asm.getModifiers().add(ModifierSourceModel.PRIVATE);
					});
		}
		return csm;
	}

}