package rest.acf.generator.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassCommentSourceModel;
import rest.acf.model.InterfaceSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;

/**
 * A generator for persistence port interfaces.
 *
 * @author ollie
 *
 */
public class ServiceInterfaceGenerator {

	private static final Logger LOG = Logger.getLogger(ServiceInterfaceGenerator.class);

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new service interface generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public ServiceInterfaceGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Generates a service interface for the passed database table service object.
	 * 
	 * @param tableSO    The database table service object which the class is to create for.
	 * @param authorName The name which should be inserted as author name.
	 * @returns A service interface for passed database table or a "null" value if a "null" value is passed.
	 */
	public InterfaceSourceModel generate(TableSO tableSO, String authorName) {
		if (tableSO == null) {
			return null;
		}
		List<ColumnSO> pkMembers = getPrimaryKeyMembers(tableSO);
		if (pkMembers.size() != 1) {
			LOG.error("table '" + tableSO.getName() + "' has not a primary key with one member: " + pkMembers.size());
			return null;
		}
		String soClassName = this.classSourceModelUtils.createSOClassSourceModel(tableSO).getName();
		String soClassPackageName = this.classSourceModelUtils.createSOPackageNameSuffix();
		InterfaceSourceModel ism = this.classSourceModelUtils.createServiceInterfaceSourceModel(tableSO);
		ism.setPackageModel(new PackageSourceModel()
				.setPackageName("${base.package.name}." + this.classSourceModelUtils.createServicePackageNameSuffix()));
		this.classSourceModelUtils.addImport(ism, "java.util", "Optional");
		this.classSourceModelUtils.addImport(ism, "${base.package.name}." + soClassPackageName, soClassName);
		ism.setComment(new ClassCommentSourceModel().setComment("/**\n" //
				+ " * An interface for a " + tableSO.getName().toLowerCase() + " service.\n" //
				+ " *\n" //
				+ " * @author " + authorName + "\n" //
				+ " *\n" //
				+ " * GENERATED CODE!!! DO NOT CHANGE!!!\n" //
				+ " */\n"));
		MethodSourceModel msm = new MethodSourceModel().setName("findById")
				.setReturnType("Optional<" + soClassName + ">");
		msm.getParameters().add(new ParameterSourceModel().setName("id").setType("long"));
		ism.getMethods().add(msm);
		return ism;
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