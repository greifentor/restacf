/*
 * PersistenceExceptionClassGenerator.java
 *
 * (c) by Ollie
 *
 * 29.08.2019
 */
package rest.acf.generator.rest;

import java.util.Arrays;

import org.apache.log4j.Logger;

import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.GenericParameterSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for result page data transfer objects.
 *
 * @author ollie
 *
 */
public class ResultPageDTOClassGenerator {

	private static final Logger LOG = Logger.getLogger(ResultPageDTOClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public ResultPageDTOClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		ClassSourceModel csm = this.classSourceModelUtils.createResultPageDTOClassSourceModel();
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createResultPageDTOClassPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "ArrayList");
		this.classSourceModelUtils.addImport(csm, "java.util", "List");
		this.classSourceModelUtils.addImport(csm, "lombok", "Data");
		this.classSourceModelUtils.addImport(csm, "lombok.experimental", "Accessors");
		this.classSourceModelUtils.addAnnotation(csm, "Accessors", "chain", true);
		this.classSourceModelUtils.addAnnotation(csm, "Data");
		csm.getGenericParameters().add(new GenericParameterSourceModel().setName("T"));
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * An object to manage paged results.\n" //
				+ " *\n" //
				+ " * @param <T> The type of the managed results.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		csm.setAttributes(Arrays.asList(
				new AttributeSourceModel().addModifier(ModifierSourceModel.PRIVATE).setName("currentPage")
						.setType("int"),
				new AttributeSourceModel().addModifier(ModifierSourceModel.PRIVATE).setName("resultsPerPage")
						.setType("int"),
				new AttributeSourceModel().setInitialValue("new ArrayList<>()").addModifier(ModifierSourceModel.PRIVATE)
						.setName("results").setType("List<T>"),
				new AttributeSourceModel().addModifier(ModifierSourceModel.PRIVATE).setName("totalResults")
						.setType("int")));
		return csm;
	}

}