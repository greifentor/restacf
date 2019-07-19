package rest.acf.generator.rest;

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
 * A generator for DTO converter classes.
 *
 * @author ollie
 *
 */
public class DTOConverterClassGenerator {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new DTO converter class generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public DTOConverterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a DTO converter class for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A DTO converter class for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		ClassSourceModel csm = this.classSourceModelUtils.createDTOConverterClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createDTOConverterPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Component");
		this.classSourceModelUtils.addImport(csm, "de.ollie.library.rest.v1.dto", "RackDTO");
		this.classSourceModelUtils.addImport(csm, "de.ollie.library.service.so", "RackSO");
		this.classSourceModelUtils.addAnnotation(csm, "Component");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A converter for " + tableSO.getName().toLowerCase() + " DTO's.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		String dtoClassName = this.classSourceModelUtils.createDTOClassSourceModel(tableSO).getName();
		String soClassName = this.classSourceModelUtils.createServiceObjectClassSourceModel(tableSO).getName();
		StringBuilder code = new StringBuilder("\t\tif (so == null) {\n");
		code.append("\t\t\treturn null;\n");
		code.append("\t\t}\n");
		code.append("\t\treturn new ").append(dtoClassName).append("()");
		for (ColumnSO column : tableSO.getColumns()) {
			code.append(".").append(this.nameConverter.getSetterName(column)).append("(so.")
					.append(this.nameConverter.getGetterName(column)).append("())");
		}
		code.append(";\n");
		code.append("\t}\n");
		csm.getMethods().add(new MethodSourceModel() //
				.setReturnType(dtoClassName) //
				.setName("convertSOToDTO") //
				.setParameters(Arrays.asList(new ParameterSourceModel().setName("so").setType(soClassName))) //
				.setCode(code.toString()));
		return csm;
	}

}