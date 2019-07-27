package rest.acf.generator.service;

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
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for service impl classes.
 *
 * @author ollie
 *
 */
public class ServiceImplClassGenerator {

	private static final Logger LOG = Logger.getLogger(ServiceImplClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new service impl classes generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public ServiceImplClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a service impl class for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A service impl class for passed database table or a "null" value if a "null" value is passed.
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
		String persistencePortClassName = this.classSourceModelUtils.createPersistencePortInterfaceSourceModel(tableSO)
				.getName();
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		String serviceClassName = this.classSourceModelUtils.createServiceInterfaceSourceModel(tableSO).getName();
		String persistencePortPackageName = this.classSourceModelUtils.createPersistencePortPackageNameSuffix();
		String soPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		String servicePackageName = this.classSourceModelUtils.createServicePackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createServiceImplClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createServiceImplPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Service");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + servicePackageName, serviceClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + persistencePortPackageName,
				persistencePortClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + soPackageName, soClassName);
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
			MethodSourceModel methodFindById = new MethodSourceModel().setName("findById");
			methodFindById.addModifier(ModifierSourceModel.PUBLIC);
			methodFindById.getAnnotations().add(new AnnotationSourceModel().setName("Override"));
			methodFindById.getParameters().add(new ParameterSourceModel().setName("id").setType("long"));
			methodFindById.setReturnType("Optional<" + soClassName + ">");
			methodFindById.setCode( //
					"\t\treturn this." + persistencePortAttrName + ".findById(id);\n" //
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