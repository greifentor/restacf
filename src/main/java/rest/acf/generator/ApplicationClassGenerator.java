package rest.acf.generator;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.persistence.CRUDRepositoryInterfaceGenerator;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for application classes.
 *
 * @author ollie
 *
 */
public class ApplicationClassGenerator {

	private static final Logger LOG = Logger.getLogger(CRUDRepositoryInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new application classes generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public ApplicationClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a application classes for the passed database service object.
	 * 
	 * @param databaseSO The database service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A application classes for passed database or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(DatabaseSO databaseSO, String authorName) {
		if (databaseSO == null) {
			return null;
		}
		ClassSourceModel csm = this.classSourceModelUtils.createApplicationClassSourceModel(databaseSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName("${base.package.name}"));
		this.classSourceModelUtils.addImport(csm, "org.springframework.boot", "SpringApplication");
		this.classSourceModelUtils.addImport(csm, "org.springframework.boot.autoconfigure", "SpringBootApplication");
		this.classSourceModelUtils.addAnnotation(csm, "SpringBootApplication");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * Application start class.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		MethodSourceModel methodMain = new MethodSourceModel().setName("main");
		methodMain.addModifier(ModifierSourceModel.PUBLIC, ModifierSourceModel.STATIC);
		methodMain.setReturnType("void");
		methodMain.getParameters().add(new ParameterSourceModel().setName("args").setType("String[]"));
		methodMain.setCode( //
				"\t\tSpringApplication.run(" + csm.getName() + ".class, args);\n" //
						+ "\t}\n");
		csm.getMethods().add(methodMain);
		return csm;
	}

}