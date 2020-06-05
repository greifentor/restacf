package rest.acf.generator.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
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
public class RESTControllerClassGenerator implements ClassCodeFactory {

	private static final Logger LOG = Logger.getLogger(RESTControllerClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public RESTControllerClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		String resultPageClassName = this.classSourceModelUtils.createResultPageDTOClassSourceModel().getName();
		String resultPageClassPackageName = this.classSourceModelUtils.createResultPageDTOClassPackageNameSuffix();
		String resultPageSOClassName = this.classSourceModelUtils.createResultPageSOClassSourceModel().getName();
		String resultPageSOClassPackageName = this.classSourceModelUtils.createResultPageSOClassPackageNameSuffix();
		String serviceInterfacePackageName = this.classSourceModelUtils.createServicePackageNameSuffix();
		String soPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createRESTControllerClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createRESTControllerClassPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "ArrayList");
		this.classSourceModelUtils.addImport(csm, "java.util", "List");
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.apache.logging.log4j", "LogManager");
		this.classSourceModelUtils.addImport(csm, "org.apache.logging.log4j", "Logger");
		this.classSourceModelUtils.addImport(csm, "org.springframework.beans.factory.annotation", "Autowired");
		this.classSourceModelUtils.addImport(csm, "org.springframework.http", "HttpStatus");
		this.classSourceModelUtils.addImport(csm, "org.springframework.http", "ResponseEntity");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "DeleteMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "GetMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "PathVariable");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "PostMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "RequestBody");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "RequestMapping");
		this.classSourceModelUtils.addImport(csm, "org.springframework.web.bind.annotation", "RestController");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dtoConverterPackageName,
				dtoConverterClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dtoPackageName, dtoClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + resultPageClassPackageName,
				resultPageClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + serviceInterfacePackageName,
				serviceInterfaceClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + soPackageName, soClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + resultPageSOClassPackageName,
				resultPageSOClassName);
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
			dtoConverterAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE);
			this.classSourceModelUtils.addAnnotation(dtoConverterAttrOpt.get(), "Autowired");
			String dtoConverterAttrName = dtoConverterAttrOpt.get().getName();
			serviceAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE);
			this.classSourceModelUtils.addAnnotation(serviceAttrOpt.get(), "Autowired");
			String serviceAttrName = serviceAttrOpt.get().getName();
			csm.getMethods().add(createDelete(serviceAttrName, tableSO));
			csm.getMethods().add(createFindAll(resultPageClassName, resultPageSOClassName, dtoClassName, soClassName,
					serviceAttrName, dtoConverterAttrName));
			csm.getMethods()
					.add(createFindById(dtoClassName, soClassName, serviceAttrName, tableSO, dtoConverterAttrName));
			csm.getMethods().add(createSave(dtoClassName, soClassName, serviceAttrName, tableSO, dtoConverterAttrName));
			addListReaderMethods(csm, tableSO);
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

	private MethodSourceModel createDelete(String serviceAttrName, TableSO tableSO) {
		ParameterSourceModel paramId = new ParameterSourceModel().setName("id").setType("long");
		paramId.getAnnotations().add(new AnnotationSourceModel().setName("PathVariable").setValue("id"));
		return new MethodSourceModel().setName("delete") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("DeleteMapping").setValue("/{id}")) //
				.addParameters(paramId) //
				.setReturnType("ResponseEntity") //
				.setCode("\t\ttry {\n" //
						+ "\t\t\tlogger.debug(\"deleting " + this.nameConverter.classNameToAttrName(tableSO.getName())
						+ " with id: \" + id);\n" //
						+ "\t\t\tif (!this." + serviceAttrName + ".delete(id)) {\n" //
						+ "\t\t\t\treturn ResponseEntity.status(HttpStatus.NOT_FOUND).build();\n" //
						+ "\t\t\t}\n" //
						+ "\t\t} catch (Exception e) {\n"//
						+ "\t\t\treturn ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();\n" //
						+ "\t\t}\n"//
						+ "\t\treturn ResponseEntity.ok().build();\n" //
						+ "\t}\n");
	}

	private MethodSourceModel createFindAll(String resultPageClassName, String resultPageSOClassName,
			String dtoClassName, String soClassName, String serviceAttrName, String dtoConverterAttrName) {
		String returnClassName = resultPageClassName + "<" + dtoClassName + ">";
		return new MethodSourceModel().setName("findAll") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("GetMapping")) //
				.setReturnType("ResponseEntity<" + returnClassName + ">") //
				.setCode("\t\ttry {\n" //
						+ "\t\t\tList<" + dtoClassName + "> dtos = new ArrayList<>();\n" //
						+ "\t\t\t" + resultPageSOClassName + "<" + soClassName + "> result = this." + serviceAttrName
						+ ".findAll();\n" //
						+ "\t\t\tfor (" + soClassName + " so : result.getResults()) {\n" //
						+ "\t\t\t\tdtos.add(this." + dtoConverterAttrName + ".convertSOToDTO(so));\n" //
						+ "\t\t\t}\n"//
						+ "\t\t\treturn ResponseEntity.ok().body(new " + returnClassName
						+ "().setCurrentPage(result.getCurrentPage()).setResultsPerPage(result.getResultsPerPage()).setResults(dtos).setTotalResults(result.getTotalResults()));\n" //
						+ "\t\t} catch (Exception e) {\n" //
						+ "\t\t\treturn ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();\n" //
						+ "\t\t}\n" //
						+ "\t}\n");
	}

	private MethodSourceModel createFindById(String dtoClassName, String soClassName, String serviceAttrName,
			TableSO tableSO, String dtoConverterAttrName) {
		ParameterSourceModel paramId = new ParameterSourceModel().setName("id").setType("long");
		paramId.getAnnotations().add(new AnnotationSourceModel().setName("PathVariable").setValue("id"));
		return new MethodSourceModel().setName("findById") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("GetMapping").setValue("/{id}")) //
				.addParameters(paramId) //
				.setReturnType("ResponseEntity<" + dtoClassName + ">") //
				.setCode( //
						"\t\ttry {\n" //
								+ "\t\t\tOptional<" + soClassName + "> so = this." + serviceAttrName
								+ ".findById(id);\n" //
								+ "\t\t\tif (so.isEmpty()) {\n" //
								+ "\t\t\t\tlogger.debug(\"no " + tableSO.getName().toLowerCase()
								+ " found for id: \" + id);\n" //
								+ "\t\t\t\treturn ResponseEntity.notFound().build();\n" //
								+ "\t\t\t}\n"//
								+ "\t\t\treturn ResponseEntity.ok().body(this." + dtoConverterAttrName
								+ ".convertSOToDTO(so.get()));\n" //
								+ "\t\t} catch (Exception e) {\n" //
								+ "\t\t\treturn ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();\n" //
								+ "\t\t}\n" // welt
								+ "\t}\n");
	}

	private MethodSourceModel createSave(String dtoClassName, String soClassName, String serviceAttrName,
			TableSO tableSO, String dtoConverterAttrName) {
		ParameterSourceModel paramId = new ParameterSourceModel().setName("dto").setType(dtoClassName);
		paramId.getAnnotations().add(new AnnotationSourceModel().setName("RequestBody"));
		return new MethodSourceModel().setName("save") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("PostMapping")) //
				.addParameters(paramId) //
				.setReturnType("ResponseEntity") //
				.setCode( //
						"\t\t" + soClassName + " so = this." + dtoConverterAttrName + ".convertDTOToSO(dto);\n" //
								+ "\t\ttry {\n" //
								+ "\t\t\tlogger.debug(\"saving " + tableSO.getName().toLowerCase() + ": \" + so);\n" //
								+ "\t\t\tthis." + serviceAttrName + ".save(so);\n" //
								+ "\t\t} catch (Exception e) {\n"//
								+ "\t\t\treturn ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();\n"//
								+ "\t\t}\n"//
								+ "\t\treturn ResponseEntity.ok().build();\n" //
								+ "\t}\n");
	}

	private void addListReaderMethods(ClassSourceModel csm, TableSO table) {

	}

}