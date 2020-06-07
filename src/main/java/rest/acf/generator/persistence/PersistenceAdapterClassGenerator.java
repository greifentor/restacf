package rest.acf.generator.persistence;

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
 * A generator for persistence adapter classes.
 *
 * @author ollie
 *
 */
public class PersistenceAdapterClassGenerator implements ClassCodeFactory {

	private static final Logger LOG = Logger.getLogger(PersistenceAdapterClassGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final DatabaseSO databaseSO;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	public PersistenceAdapterClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
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
		String pkGetterName = this.nameConverter.getGetterName(pkMembers.get(0));
		String pkClassName = this.typeConverter.typeSOToTypeString(pkMembers.get(0).getType(),
				pkMembers.get(0).isNullable());
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
		this.classSourceModelUtils.addImport(csm, "java.util", "ArrayList");
		this.classSourceModelUtils.addImport(csm, "java.util", "List");
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
			String elementName = this.nameConverter.classNameToAttrName(tableSO.getName());
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
			csm.getMethods().add(createDelete(dboClassName, pkAttrName, pkClassName, elementName, repositoryAttrName));
			csm.getMethods().add(createFindAll(soClassName, dboClassName, repositoryAttrName, dboConverterAttrName,
					elementName + "s"));
			csm.getMethods().add(createFindById(soClassName, dboClassName, pkAttrName, pkClassName, repositoryAttrName,
					dboConverterAttrName));
			csm.getMethods().add(createSave(soClassName, dboClassName, pkClassName, dboConverterAttrName,
					repositoryAttrName, pkGetterName));
			this.classSourceModelUtils.getReferencedColumns(tableSO, this.databaseSO) //
					.forEach(columnSO -> csm.getMethods().add(createFindXByY(columnSO, tableSO, soClassName,
							dboClassName, repositoryAttrName, dboConverterAttrName)));
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

	private MethodSourceModel createDelete(String dboClassName, String pkAttrName, String pkClassName,
			String elementName, String repositoryAttrName) {
		return new MethodSourceModel().setName("delete") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName(pkAttrName).setType(pkClassName)) //
				.setReturnType("boolean") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode("\t\tboolean result = false;\n" //
						+ "\t\ttry {\n" //
						+ "\t\t\tOptional<" + dboClassName + "> dbo = this." + repositoryAttrName + ".findById(id);\n"
						+ "\t\t\tif (dbo.isPresent()) {\n" + "\t\t\t\tthis." + repositoryAttrName
						+ ".delete(dbo.get());\n" //
						+ "\t\t\t\tresult = true;\n" //
						+ "\t\t\t}\n" //
						+ "\t\t} catch (Exception e) {\n" //
						+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.WriteError, " //
						+ "\"error while deleting " + elementName + " with id: \" + id, e);\n" //
						+ "\t\t}\n" //
						+ "\t\treturn result;\n" //
						+ "\t}\n");
	}

	private MethodSourceModel createFindAll(String soClassName, String dboClassName, String repositoryAttrName,
			String dboConverterAttrName, String pluralElementName) {
		return new MethodSourceModel().setName("findAll") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.setReturnType("List<" + soClassName + ">") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode( //
						"\t\ttry {\n" //
								+ "\t\t\tList<" + soClassName + "> sos = new ArrayList<>();\n" //
								+ "\t\t\tfor (" + dboClassName + " dbo : this." + repositoryAttrName + ".findAll()) {\n" //
								+ "\t\t\t\tsos.add(this." + dboConverterAttrName + ".convertDBOToSO(dbo));\n" //
								+ "\t\t\t}\n"//
								+ "\t\t\treturn sos;\n" //
								+ "\t\t} catch (Exception e) {\n" //
								+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.ReadError, " //
								+ "\"error while finding all " + pluralElementName + ".\", e);\n" //
								+ "\t\t}\n" //
								+ "\t}\n");
	}

	/*
	 * @Override public List<RackSO> findAll() throws PersistenceException { try { List<RackSO> sos = new ArrayList<>();
	 * for (RackDBO dbo : this.rackRepository.findAll()) { sos.add(this.rackDBOConverter.convertDBOToSO(dbo)); } return
	 * sos; } catch (Exception e) { throw new PersistenceException(PersistenceException.Type.ReadError,
	 * "error while finding all racks.", e); } }
	 * 
	 * 
	 */

	private MethodSourceModel createFindById(String soClassName, String dboClassName, String pkColumnName,
			String pkClassName, String repositoryAttrName, String dboConverterAttrName) {
		return new MethodSourceModel().setName("findById") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName(pkColumnName).setType(pkClassName)) //
				.setReturnType("Optional<" + soClassName + ">") //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode( //
						"\t\ttry {\n" //
								+ "\t\t\tOptional<" + dboClassName + "> dbo = this." + repositoryAttrName
								+ ".findById(id);\n" //
								+ "\t\t\tif (dbo.isEmpty()) {\n" //
								+ "\t\t\t\treturn Optional.empty();\n" //
								+ "\t\t\t}\n"//
								+ "\t\t\treturn Optional.of(this." + dboConverterAttrName
								+ ".convertDBOToSO(dbo.get()));\n" //
								+ "\t\t} catch (Exception e) {\n" //
								+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.ReadError, " //
								+ "\"error while finding by id: \" + id, e);\n" //
								+ "\t\t}\n" //
								+ "\t}\n");
	}

	private MethodSourceModel createSave(String soClassName, String dboClassName, String pkClassName,
			String dboConverterAttrName, String repositoryAttrName, String pkGetterName) {
		return new MethodSourceModel().setName("save") //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.addParameters(new ParameterSourceModel().setName("so").setType(soClassName)) //
				.setReturnType(pkClassName) //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode( //
						"\t\ttry {\n" //
								+ "\t\t\t" + dboClassName + " dbo = this." + dboConverterAttrName
								+ ".convertSOToDBO(so);\n" //
								+ "\t\t\treturn this." + repositoryAttrName + ".save(dbo)." + pkGetterName + "();\n" //
								+ "\t\t} catch (Exception e) {\n" //
								+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.WriteError, " //
								+ "\"error while saving: \" + so, e);\n" //
								+ "\t\t}\n" //
								+ "\t}\n");
	}

	private MethodSourceModel createFindXByY(ColumnSO columnSO, TableSO tableSO, String soClassName,
			String dboClassName, String repositoryAttrName, String dboConverterAttrName) {
		String xPluralName = this.nameConverter.getPluralName(tableSO);
		String ySingularName = this.nameConverter.getSingularName(columnSO.getTable());
		String methodName = "find" + xPluralName + "For" + ySingularName;
		String idParameterName = this.nameConverter.columnNameToAttributeName(columnSO, true);
		return new MethodSourceModel() //
				.setName(methodName) //
				.addModifiers(ModifierSourceModel.PUBLIC) //
				.addAnnotations(new AnnotationSourceModel().setName("Override")) //
				.setReturnType("List<" + soClassName + ">") //
				.addParameters(new ParameterSourceModel() //
						.setName(idParameterName) //
						.setType(this.typeConverter.typeSOToTypeString(columnSO.getType(), columnSO.isNullable()))) //
				.addThrownExceptions(new ThrownExceptionSourceModel().setName("PersistenceException")) //
				.setCode("\t\ttry {\n" + //
						"\t\t\tList<" + soClassName + "> sos = new ArrayList<>();\n" + "			for ("
						+ dboClassName + " dbo : this." + repositoryAttrName + "." + methodName + "(" + idParameterName
						+ ")) {\n" //
						+ "\t\t\t\tsos.add(this." + dboConverterAttrName + ".convertDBOToSO(dbo));\n" + "\t\t\t}\n" //
						+ "\t\t\treturn sos;\n" //
						+ "\t\t} catch (Exception e) {\n" //
						+ "\t\t\tthrow new PersistenceException(PersistenceException.Type.ReadError, \"error while "
						+ "finding all " + this.nameConverter.classNameToAttrName(xPluralName) + " for "
						+ this.nameConverter.classNameToAttrName(ySingularName) + ":\" + " + idParameterName + ", e);\n" //
						+ "\t\t}\n" //
						+ "\t}\n") //
		;
	}

}