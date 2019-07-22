package rest.acf.generator.persistence;

import java.util.Arrays;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for DBO converter classes.
 *
 * @author ollie
 *
 */
public class DBOConverterClassGenerator {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new DBO converter class generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the
	 *                              application.
	 * @param typeConverter         An access to the type converter of the
	 *                              application.
	 */
	public DBOConverterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a DBO converter class for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to
	 *                   create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A DBO converter class for passed database table or a "null" value if
	 *          a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		ClassSourceModel csm = this.classSourceModelUtils.createDBOConverterClassSourceModel(tableSO);
		String dboClassName = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO).getName();
		String soClassName = this.classSourceModelUtils.createServiceObjectClassSourceModel(tableSO).getName();
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createDBOConverterPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Component");
		this.classSourceModelUtils.addImport(csm,
				"${base.package.name}." + this.classSourceModelUtils.createJPAModelPackageNameSuffix(), dboClassName);
		this.classSourceModelUtils.addImport(csm,
				"${base.package.name}." + this.classSourceModelUtils.createServiceObjectPackageNameSuffix(),
				soClassName);
		this.classSourceModelUtils.addAnnotation(csm, "Component");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A converter for " + tableSO.getName().toLowerCase() + " DBO's.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		StringBuilder code = new StringBuilder("\t\tif (dbo == null) {\n");
		code.append("\t\t\treturn null;\n");
		code.append("\t\t}\n");
		code.append("\t\treturn new ").append(soClassName).append("()");
		for (ColumnSO column : tableSO.getColumns()) {
			code.append(".").append(this.nameConverter.getSetterName(column)).append("(dbo.")
					.append(this.nameConverter.getGetterName(column)).append("())");
		}
		code.append(";\n");
		code.append("\t}\n");
		csm.getMethods().add(new MethodSourceModel() //
				.setReturnType(soClassName) //
				.setName("convertDBOToSO") //
				.setParameters(Arrays.asList(new ParameterSourceModel().setName("dbo").setType(dboClassName))) //
				.setCode(code.toString()));
		return csm;
	}

}