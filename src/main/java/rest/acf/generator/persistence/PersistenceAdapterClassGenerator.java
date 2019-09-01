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
import rest.acf.model.ThrownExceptionSourceModel;

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
		String persistenceExceptionClassName = this.classSourceModelUtils.createPersistenceExceptionClassSourceModel()
				.getName();
		String persistenceExceptionPackageName = this.classSourceModelUtils
				.createPersistenceExceptionPackageNameSuffix();
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
		this.classSourceModelUtils.addImport(csm, "${base.package.name}." + persistenceExceptionPackageName,
				persistenceExceptionClassName);
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
			csm.getMethods().add(createFindById(soClassName, dboClassName, repositoryAttrName, dboConverterAttrName));
			csm.getMethods().add(createSave(soClassName, dboClassName, repositoryAttrName, dboConverterAttrName));
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

	private MethodSourceModel createFindById(String soClassName, String dboClassName, String repositoryAttrName,
			String dboConverterAttrName) {
		MethodSourceModel msm = new MethodSourceModel().setName("findById");
		msm.addModifiers(ModifierSourceModel.PUBLIC);
		msm.getAnnotations().add(new AnnotationSourceModel().setName("Override"));
		msm.getParameters().add(new ParameterSourceModel().setName("id").setType("long"));
		msm.setReturnType("Optional<" + soClassName + ">");
		msm.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException"));
		msm.setCode( //
				"\t\ttry {\n" //
						+ "\t\t\tOptional<" + dboClassName + "> dbo = this." + repositoryAttrName + ".findById(id);\n" //
						+ "\t\t\tif (dbo.isEmpty()) {\n" //
						+ "\t\t\t\treturn Optional.empty();\n" //
						+ "\t\t\t}\n"//
						+ "\t\t\treturn Optional.of(this." + dboConverterAttrName + ".convertDBOToSO(dbo.get()));\n" //
						+ "\t\t} catch (Exception e) {\n" //
						+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.ReadError, " //
						+ "\"error while finding by id: \" + id, e);\n" //
						+ "\t\t}\n" //
						+ "\t}\n");
		return msm;
	}

	private MethodSourceModel createSave(String soClassName, String dboClassName, String repositoryAttrName,
			String dboConverterAttrName) {
		MethodSourceModel msm = new MethodSourceModel().setName("save");
		msm.addModifiers(ModifierSourceModel.PUBLIC);
		msm.getAnnotations().add(new AnnotationSourceModel().setName("Override"));
		msm.getParameters().add(new ParameterSourceModel().setName("so").setType(soClassName));
		msm.setReturnType("void");
		msm.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException"));
		msm.setCode( //
				"\t\ttry {\n" //
						+ "\t\t\t" + dboClassName + " dbo = this." + dboConverterAttrName + ".convertSOToDBO(so);\n" //
						+ "\t\t\tthis.rackRepository.save(dbo);\n" //
						+ "\t\t} catch (Exception e) {\n" //
						+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.WriteError, " //
						+ "\"error while saving: \" + so, e);\n" //
						+ "\t\t}\n" //
						+ "\t}\n");
		return msm;
	}

}