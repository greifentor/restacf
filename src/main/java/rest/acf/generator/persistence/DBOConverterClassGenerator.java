package rest.acf.generator.persistence;

import java.util.Arrays;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for DBO converter classes.
 *
 * @author ollie
 *
 */
public class DBOConverterClassGenerator implements ClassCodeFactory {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public DBOConverterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		ClassSourceModel csm = this.classSourceModelUtils.createDBOConverterClassSourceModel(tableSO);
		String dboClassName = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO).getName();
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createDBOConverterPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Component");
		this.classSourceModelUtils.addImport(csm,
				"${base.package.name}." + this.classSourceModelUtils.createJPAModelPackageNameSuffix(), dboClassName);
		this.classSourceModelUtils.addImport(csm,
				"${base.package.name}." + this.classSourceModelUtils.createSOPackageNameSuffix(), soClassName);
		this.classSourceModelUtils.addAnnotation(csm, "Component");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A converter for " + tableSO.getName().toLowerCase() + " DBO's.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		csm.getMethods().add(new MethodSourceModel() //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.setReturnType(soClassName) //
				.setName("convertDBOToSO") //
				.setParameters(Arrays.asList(new ParameterSourceModel().setName("dbo").setType(dboClassName))) //
				.setCode(createConvertDBOToSO(tableSO.getColumns(), soClassName, csm)));
		csm.getMethods().add(new MethodSourceModel() //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.setReturnType(dboClassName) //
				.setName("convertSOToDBO") //
				.setParameters(Arrays.asList(new ParameterSourceModel().setName("so").setType(soClassName))) //
				.setCode(createConvertSOToDBO(tableSO.getColumns(), dboClassName, csm)));
		return csm;
	}

	private String createConvertDBOToSO(List<ColumnSO> columns, String soClassName, ClassSourceModel csm) {
		StringBuilder code = new StringBuilder("\t\tif (dbo == null) {\n");
		code.append("\t\t\treturn null;\n");
		code.append("\t\t}\n");
		code.append("\t\treturn new ").append(soClassName).append("()");
		for (ColumnSO column : columns) {
			ForeignKeySO[] foreignKeys = this.classSourceModelUtils.getForeignkeyByColumn(column);
			if (foreignKeys.length == 0) {
				code.append(".").append(this.nameConverter.getSetterName(column)).append("(dbo.")
						.append(this.nameConverter.getGetterName(column)).append("())");
			} else if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
				this.classSourceModelUtils.addImport(csm, "org.springframework.beans.factory.annotation", "Autowired");
				ReferenceSO reference = foreignKeys[0].getReferences().get(0);
				TableSO referencedTable = reference.getReferencedColumn().getTable();
				String dboConverterClassName = this.nameConverter.tableNameToDBOConverterClassName(referencedTable);
				String dboConverterAttrName = this.nameConverter.classNameToAttrName(dboConverterClassName);
				this.classSourceModelUtils.addAttributeForClassName(csm, dboConverterClassName)
						.ifPresent(asm -> classSourceModelUtils.addAnnotation(asm, "Autowired"));
				code.append(".").append(this.nameConverter.getSetterName(column)).append("(this.")
						.append(dboConverterAttrName).append(".convertDBOToSO(dbo.")
						.append(this.nameConverter.getGetterName(column)).append("()))");
			}
		}
		code.append(";\n");
		code.append("\t}\n");
		return code.toString();
	}

	private String createConvertSOToDBO(List<ColumnSO> columns, String dboClassName, ClassSourceModel csm) {
		StringBuilder code = new StringBuilder("\t\tif (so == null) {\n");
		code.append("\t\t\treturn null;\n");
		code.append("\t\t}\n");
		code.append("\t\treturn new ").append(dboClassName).append("()");
		for (ColumnSO column : columns) {
			ForeignKeySO[] foreignKeys = this.classSourceModelUtils.getForeignkeyByColumn(column);
			if (foreignKeys.length == 0) {
				code.append(".").append(this.nameConverter.getSetterName(column)).append("(so.")
						.append(this.nameConverter.getGetterName(column)).append("())");
			} else if ((foreignKeys.length == 1) && (foreignKeys[0].getReferences().size() == 1)) {
				this.classSourceModelUtils.addImport(csm, "org.springframework.beans.factory.annotation", "Autowired");
				ReferenceSO reference = foreignKeys[0].getReferences().get(0);
				TableSO referencedTable = reference.getReferencedColumn().getTable();
				String dboConverterClassName = this.nameConverter.tableNameToDBOConverterClassName(referencedTable);
				String dboConverterAttrName = this.nameConverter.classNameToAttrName(dboConverterClassName);
				this.classSourceModelUtils.addAttributeForClassName(csm, dboConverterClassName)
						.ifPresent(asm -> classSourceModelUtils.addAnnotation(asm, "Autowired"));
				code.append(".").append(this.nameConverter.getSetterName(column)).append("(this.")
						.append(dboConverterAttrName).append(".convertSOToDBO(so.")
						.append(this.nameConverter.getGetterName(column)).append("()))");
			}
		}
		code.append(";\n");
		code.append("\t}\n");
		return code.toString();
	}

}