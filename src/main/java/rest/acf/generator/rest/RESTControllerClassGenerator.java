package rest.acf.generator.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ConstructorSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for REST controller classes.
 *
 * @author ollie
 *
 */
public class RESTControllerClassGenerator {

	private static final Logger LOG = Logger.getLogger(RESTControllerClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new REST controller classes generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public RESTControllerClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a REST controller classes for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A REST controller classes for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		List<ColumnSO> pkMembers = getPrimaryKeyMembers(tableSO);
		if (pkMembers.size() != 1) {
			LOG.error("table '" + tableSO.getName() + "' has not a primary key with one member: " + pkMembers.size());
			return null;
		}
		String dtoClassName = this.classSourceModelUtils.createDTOClassSourceModel(tableSO).getName();
		String dtoConverterClassName = this.classSourceModelUtils.createDTOConverterClassSourceModel(tableSO).getName();
		String serviceInterfaceClassName = this.classSourceModelUtils.createServiceInterfaceSourceModel(tableSO)
				.getName();
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		String dtoPackageName = this.classSourceModelUtils.createDTOPackageNameSuffix();
		String dtoConverterPackageName = this.classSourceModelUtils.createDTOConverterPackageNameSuffix();
		String serviceInterfacePackageName = this.classSourceModelUtils.createServicePackageNameSuffix();
		String soPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createRESTControllerClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createRESTControllerClassPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.apache.logging.log4j", "LogManager");
		this.classSourceModelUtils.addImport(csm, "org.apache.logging.log4j", "Logger");
		this.classSourceModelUtils.addImport(csm, "org.springframework.http", "ResponseEntity");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "GetMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "PathVariable");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "RequestMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "RestController");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dtoConverterPackageName,
				dtoConverterClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dtoPackageName, dtoClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + serviceInterfacePackageName,
				serviceInterfaceClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + soPackageName, soClassName);
		this.classSourceModelUtils.addAnnotation(csm, "RestController");
		csm.getAnnotations().add(new AnnotationSourceModel().setName("RequestMapping")
				.setValue("api/v1/" + tableSO.getName().toLowerCase() + "s"));
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * A REST controller for " + tableSO.getName().toLowerCase() + "s.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		this.classSourceModelUtils
				.addAttributeForClassName(csm, "Logger", "LogManager.getLogger(" + csm.getName() + ".class)")
				.ifPresent(a -> a.addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL));
		Optional<AttributeSourceModel> dtoConverterAttrOpt = this.classSourceModelUtils.addAttributeForClassName(csm,
				dtoConverterClassName);
		Optional<AttributeSourceModel> serviceAttrOpt = this.classSourceModelUtils.addAttributeForClassName(csm,
				serviceInterfaceClassName);
		if (dtoConverterAttrOpt.isPresent() && serviceAttrOpt.isPresent()) {
			dtoConverterAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL);
			String dtoConverterAttrName = dtoConverterAttrOpt.get().getName();
			serviceAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL);
			String serviceAttrName = serviceAttrOpt.get().getName();
			ConstructorSourceModel cosm = new ConstructorSourceModel();
			cosm.getParameters()
					.add(new ParameterSourceModel()
							.setName(this.nameConverter.classNameToAttrName(dtoConverterClassName))
							.setType(dtoConverterClassName));
			cosm.getParameters()
					.add(new ParameterSourceModel()
							.setName(this.nameConverter.classNameToAttrName(serviceInterfaceClassName))
							.setType(serviceInterfaceClassName));
			String code = "\t\tsuper();\n" //
					+ "\t\tthis." + dtoConverterAttrName + " = " + dtoConverterAttrName + ";\n" //
					+ "\t\tthis." + serviceAttrName + " = " + serviceAttrName + ";\n" //
					+ "\t}\n";
			cosm.setCode(code);
			csm.getConstructors().add(cosm);
			MethodSourceModel methodFindById = new MethodSourceModel().setName("findById");
			methodFindById.addModifier(ModifierSourceModel.PUBLIC);
			methodFindById.getAnnotations().add(new AnnotationSourceModel().setName("GetMapping").setValue("/{id}"));
			ParameterSourceModel paramId = new ParameterSourceModel().setName("id").setType("long");
			paramId.getAnnotations().add(new AnnotationSourceModel().setName("PathVariable").setValue("id"));
			methodFindById.getParameters().add(paramId);
			methodFindById.setReturnType("ResponseEntity<" + dtoClassName + ">");
			methodFindById.setCode( //
					"\t\tOptional<" + soClassName + "> so = this." + serviceAttrName + ".findById(id);\n" //
							+ "\t\tif (so.isEmpty()) {\n" //
							+ "\t\t\tlogger.debug(\"no " + tableSO.getName().toLowerCase()
							+ " found for id: \" + id);\n" //
							+ "\t\t\treturn ResponseEntity.notFound().build();\n" //
							+ "\t\t}\n"//
							+ "\t\treturn ResponseEntity.ok().body(this." + dtoConverterAttrName
							+ ".convertSOToDTO(so.get()));\n" //
							+ "\t}\n");
			csm.getMethods().add(methodFindById);
		}
		return csm;
	}

	private List<ColumnSO> getPrimaryKeyMembers(TableSO table) {
		List<ColumnSO> pkMembers = new ArrayList<>();
		for (ColumnSO column : table.getColumns()) {
			if (column.isPkMember()) {
				pkMembers.add(column);
			}
		}
		return pkMembers;
	}

}