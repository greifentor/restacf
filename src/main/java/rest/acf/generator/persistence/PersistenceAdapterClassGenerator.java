package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
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

/**
 * A generator for persistence adapter classes.
 *
 * @author ollie
 *
 */
public class PersistenceAdapterClassGenerator implements ClassCodeFactory {

	private static final Logger LOG = Logger.getLogger(CRUDRepositoryInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public PersistenceAdapterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		String dboClassName = this.classSourceModelUtils.createJPAModelClassSourceModel(tableSO).getName();
		String dboConverterClassName = this.classSourceModelUtils.createDBOConverterClassSourceModel(tableSO).getName();
		String persistenceClassName = this.classSourceModelUtils.createPersistencePortInterfaceSourceModel(tableSO)
				.getName();
		String repositoryClassName = this.classSourceModelUtils.createCRUDRepitoryInterfaceSourceModel(tableSO)
				.getName();
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		String dboPackageName = this.classSourceModelUtils.createJPAModelPackageNameSuffix();
		String dboConverterPackageName = this.classSourceModelUtils.createDBOConverterPackageNameSuffix();
		String persistencePortPackageName = this.classSourceModelUtils.createPersistencePortPackageNameSuffix();
		String repositoryPackageName = this.classSourceModelUtils.createCRUDRepositoryPackageNameSuffix();
		String soPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		ClassSourceModel csm = this.classSourceModelUtils.createPersistenceAdapterClassSourceModel(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName(
				"${base.package.name}." + this.classSourceModelUtils.createPersistenceAdapterPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "java.util", "Optional");
		this.classSourceModelUtils.addImport(csm, "org.springframework.stereotype", "Service");
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dboConverterPackageName,
				dboConverterClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + dboPackageName, dboClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + repositoryPackageName, repositoryClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + persistencePortPackageName,
				persistenceClassName);
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + soPackageName, soClassName);
		this.classSourceModelUtils.addAnnotation(csm, "Service");
		csm.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * An implementation of the " + tableSO.getName().toLowerCase()
				+ " persistence port interface for RDBMS.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		csm.getInterfaces().add(new ExtensionSourceModel().setParentClassName(persistenceClassName));
		Optional<AttributeSourceModel> dboConverterAttrOpt = this.classSourceModelUtils.addAttributeForClassName(csm,
				dboConverterClassName);
		Optional<AttributeSourceModel> repositoryAttrOpt = this.classSourceModelUtils.addAttributeForClassName(csm,
				repositoryClassName);
		if (dboConverterAttrOpt.isPresent() && repositoryAttrOpt.isPresent()) {
			dboConverterAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL);
			String dboConverterAttrName = dboConverterAttrOpt.get().getName();
			repositoryAttrOpt.get().addModifier(ModifierSourceModel.PRIVATE, ModifierSourceModel.FINAL);
			String repositoryAttrName = repositoryAttrOpt.get().getName();
			ConstructorSourceModel cosm = new ConstructorSourceModel();
			cosm.getParameters()
					.add(new ParameterSourceModel()
							.setName(this.nameConverter.classNameToAttrName(dboConverterClassName))
							.setType(dboConverterClassName));
			cosm.getParameters().add(new ParameterSourceModel()
					.setName(this.nameConverter.classNameToAttrName(repositoryClassName)).setType(repositoryClassName));
			String code = "\t\tsuper();\n" //
					+ "\t\tthis." + dboConverterAttrName + " = " + dboConverterAttrName + ";\n" //
					+ "\t\tthis." + repositoryAttrName + " = " + repositoryAttrName + ";\n" //
					+ "\t}\n";
			cosm.setCode(code);
			csm.getConstructors().add(cosm);
			MethodSourceModel methodFindById = new MethodSourceModel().setName("findById");
			methodFindById.addModifier(ModifierSourceModel.PUBLIC);
			methodFindById.getAnnotations().add(new AnnotationSourceModel().setName("Override"));
			methodFindById.getParameters().add(new ParameterSourceModel().setName("id").setType("long"));
			methodFindById.setReturnType("Optional<" + soClassName + ">");
			methodFindById.setCode( //
					"\t\tOptional<" + dboClassName + "> dbo = this." + repositoryAttrName + ".findById(id);\n" //
							+ "\t\tif (dbo.isEmpty()) {\n" //
							+ "\t\t\treturn Optional.empty();\n" //
							+ "\t\t}\n"//
							+ "\t\treturn Optional.of(this." + dboConverterAttrName + ".convertDBOToSO(dbo.get()));\n" //
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