/*
 * PersistenceExceptionClassGenerator.java
 *
 * (c) by Ollie
 *
 * 29.08.2019
 */
package rest.acf.generator.service;

import java.util.Arrays;

import org.apache.log4j.Logger;

import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ConstructorSourceModel;
import rest.acf.model.EnumTypeSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for persistence exceptions.
 *
 * @author ollie
 *
 */
public class PersistenceExceptionClassGenerator {

	private static final Logger LOG = Logger.getLogger(PersistencePortInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new persistence exception class generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public PersistenceExceptionClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a persistence exception class classes for the passed database service object.
	 * 
	 * @param authorName The name which should be inserted as author name.
	 * @returns A persistence exception class for the passed database or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(String authorName) {
		ClassSourceModel csm = this.classSourceModelUtils.createPersistenceExceptionClassSourceModel();
		csm.setExtendsModel(new ExtensionSourceModel().setParentClassName("Exception"));
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createPersistenceExceptionPackageNameSuffix()));
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * An exception for persistence errors.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		csm.setEnums(Arrays.asList(createType()));
		csm.setAttributes(Arrays.asList(
				new AttributeSourceModel().addModifier(ModifierSourceModel.PRIVATE).setName("type").setType("Type")));
		csm.setConstructors(Arrays.asList(createConstructor()));
		csm.setMethods(Arrays.asList(createGetType()));
		return csm;
	}

	private EnumTypeSourceModel createType() {
		EnumTypeSourceModel etsm = new EnumTypeSourceModel();
		etsm.setName("Type");
		etsm.setIdentifiers(Arrays.asList("ReadError", "WriteError"));
		etsm.addModifier(ModifierSourceModel.PUBLIC);
		return etsm;
	}

	private ConstructorSourceModel createConstructor() {
		ConstructorSourceModel csm = new ConstructorSourceModel();
		csm.setParameters(Arrays.asList(new ParameterSourceModel().setName("type").setType("Type"),
				new ParameterSourceModel().setName("message").setType("String"),
				new ParameterSourceModel().setName("cause").setType("Throwable")));
		csm.setCode("\t\tsuper(message, cause);\n" //
				+ "\t\tthis.type = type;\n" //
				+ "\t}\n");
		return csm;
	}

	private MethodSourceModel createGetType() {
		MethodSourceModel msm = new MethodSourceModel().setName("getType");
		msm.addModifier(ModifierSourceModel.PUBLIC);
		msm.setReturnType("Type");
		msm.setCode("\t\treturn this.type;\n" //
				+ "\t}\n");
		return msm;
	}

}