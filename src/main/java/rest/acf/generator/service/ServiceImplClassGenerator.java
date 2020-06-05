package rest.acf.generator.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.ClassCodeFactory;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ConstructorSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;
import rest.acf.model.ThrownExceptionSourceModel;

/**
 * A generator for service impl classes.
 *
 * @author ollie
 *
 */
public class ServiceImplClassGenerator implements ClassCodeFactory {

	private static final Logger LOG = Logger.getLogger(ServiceImplClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final DatabaseSO databaseSO;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public ServiceImplClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter, DatabaseSO databaseSO) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.databaseSO = databaseSO;
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
		String pkAttrName = this.nameConverter.columnNameToAttributeName(pkMembers.get(0));
		String pkClassName = this.typeConverter.typeSOToTypeString(pkMembers.get(0).getType(),
				pkMembers.get(0).isNullable());
		String persistenceExceptionClassName = this.classSourceModelUtils.createPersistenceExceptionClassSourceModel()
				.getName();
		String persistencePortClassName = this.classSourceModelUtils.createPersistencePortInterfaceSourceModel(tableSO)
				.getName();
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		String serviceClassName = this.classSourceModelUtils.createServiceInterfaceSourceModel(tableSO).getName();
		String persistencePortPackageName = this.classSourceModelUtils.createPersistencePortPackageNameSuffix();
		String persistenceExceptionPackageName = this.classSourceModelUtils
				.createPersistenceExceptionPackageNameSuffix();
		String resultPageClassName = this.classSourceModelUtils.createResultPageSOClassSourceModel().getName();
		String resultPageClassPackageName = this.classSourceModelUtils.createResultPageSOClassPackageNameSuffix();
		String soPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		String servicePackageName = this.classSourceModelUtils.createServicePackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createServiceImplClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createServiceImplPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "List");
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Service");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + servicePackageName, serviceClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + persistenceExceptionPackageName,
				persistenceExceptionClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + persistencePortPackageName,
				persistencePortClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + soPackageName, soClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + resultPageClassPackageName,
				resultPageClassName);
		this.classSourceModelUtils.addAnnotation(csm, "Service");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * An implementation of the " + tableSO.getName().toLowerCase() + " service interface.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		csm.getInterfaces().add(new ExtensionSourceModel().setParentClassName(serviceClassName));
		Optional<AttributeSourceModel> persistencePortAttrOpt = this.classSourceModelUtils.addAttributeForClassName(csm,
				persistencePortClassName);
		if (persistencePortAttrOpt.isPresent()) {
			persistencePortAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL);
			String persistencePortAttrName = persistencePortAttrOpt.get().getName();
			ConstructorSourceModel cosm = new ConstructorSourceModel();
			cosm.getParameters()
					.add(new ParameterSourceModel()
							.setName(this.nameConverter.classNameToAttrName(persistencePortClassName))
							.setType(persistencePortClassName));
			String code = "\t\tsuper();\n" //
					+ "\t\tthis." + persistencePortAttrName + " = " + persistencePortAttrName + ";\n" //
					+ "\t}\n";
			cosm.setCode(code);
			csm.getConstructors().add(cosm);
			csm.getMethods()
					.add(createDelete(pkClassName, pkAttrName, persistencePortAttrName, persistenceExceptionClassName));
			csm.getMethods().add(createFindAll(resultPageClassName, soClassName, persistencePortAttrName,
					persistenceExceptionClassName));
			csm.getMethods().add(createFindById(soClassName, persistencePortAttrName, persistenceExceptionClassName));
			csm.getMethods().add(createSave(soClassName, this.nameConverter.classNameToAttrName(tableSO.getName()),
					persistencePortAttrName, persistenceExceptionClassName));
			this.classSourceModelUtils.getReferencedColumns(tableSO, this.databaseSO) //
					.forEach(columnSO -> csm.getMethods()
							.add(createFindXByY(columnSO, tableSO, soClassName, persistencePortAttrName)));
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

	private MethodSourceModel createDelete(String pkClassName, String pkAttrName, String persistencePortAttrName,
			String persistenceExceptionClassName) {
		return new MethodSourceModel().setName("delete") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName(pkAttrName).setType(pkClassName)) //
				.setReturnType("boolean") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName(persistenceExceptionClassName)) //
				.setCode( //
						"\t\treturn this." + persistencePortAttrName + ".delete(" + pkAttrName + ");\n" //
								+ "\t}\n");
	}

	private MethodSourceModel createFindAll(String resultPageClassName, String soClassName,
			String persistencePortAttrName, String persistenceExceptionClassName) {
		String returnClassName = resultPageClassName + "<" + soClassName + ">";
		return new MethodSourceModel().setName("findAll") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.setReturnType(returnClassName) //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName(persistenceExceptionClassName)) //
				.setCode("\t\tList<" + soClassName + "> l = this." + persistencePortAttrName + ".findAll();\n" //
						+ "\t\treturn new " + returnClassName
						+ "().setCurrentPage(0).setResultsPerPage(l.size()).setResults(l).setTotalResults(l.size());\n"
						+ "\t}\n");
		/*
		 * List<RackSO> l = this.rackPersistencePort.findAll(); return new
		 * ResultPageSO<RackSO>().setResults(l).setTotalResults(l.size());
		 * 
		 */
	}

	private MethodSourceModel createFindById(String soClassName, String persistencePortAttrName,
			String persistenceExceptionClassName) {
		return new MethodSourceModel().setName("findById") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName("id").setType("long")) //
				.setReturnType("Optional<" + soClassName + ">") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName(persistenceExceptionClassName)) //
				.setCode( //
						"\t\treturn this." + persistencePortAttrName + ".findById(id);\n" //
								+ "\t}\n");
	}

	private MethodSourceModel createSave(String soClassName, String soAttrName, String persistencePortAttrName,
			String persistenceExceptionClassName) {
		return new MethodSourceModel().setName("save") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName(soAttrName).setType(soClassName)) //
				.setReturnType("void") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName(persistenceExceptionClassName)) //
				.setCode( //
						"\t\tthis." + persistencePortAttrName + ".save(" + soAttrName + ");\n" //
								+ "\t}\n");
	}

	private MethodSourceModel createFindXByY(ColumnSO columnSO, TableSO tableSO, String soClassName,
			String persistencePortAttrName) {
		String methodName = "find" + this.nameConverter.getPluralName(tableSO) + "For"
				+ this.nameConverter.getSingularName(columnSO.getTable());
		String idParameterName = this.nameConverter.columnNameToAttributeName(columnSO, true);
		return new MethodSourceModel() //
				.setName(methodName) //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.setReturnType("ResultPageSO<" + soClassName + ">") //
				.addParameters(new ParameterSourceModel() //
						.setName(idParameterName) //
						.setType(this.typeConverter.typeSOToTypeString(columnSO.getType(), columnSO.isNullable()))) //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode("\t\tList<" + soClassName + "> l = this." + persistencePortAttrName + "." + methodName + "("
						+ idParameterName + ");\n" //
						+ "\t\treturn new ResultPageSO<" + soClassName
						+ ">().setCurrentPage(0).setResultsPerPage(l.size()).setResults(l).setTotalResults(l.size());\n" //
						+ "\t}\n") //
		;
	}

}