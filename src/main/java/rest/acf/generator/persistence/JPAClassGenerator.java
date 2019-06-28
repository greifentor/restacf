package rest.acf.generator.persistence;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.PackageSourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class JPAClassGenerator {

	private final ClassSourceModelUtils classSourceModelUtils;
	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new JPA class generator with the passed parameters.
	 *
	 * @param classSourceModelUtils An access to the class source model utils.
	 * @param nameConverter         An access to the name converter of the application.
	 * @param typeConverter         An access to the type converter of the application.
	 */
	public JPAClassGenerator(ClassSourceModelUtils classSourceModelUtils, NameConverter nameConverter,
			TypeConverter typeConverter) {
		super();
		this.classSourceModelUtils = classSourceModelUtils;
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
	}

	/**
	 * Returns the package name suffix for the generated class.
	 * 
	 * @return The package name suffix for the generated class.
	 */
	public String getPackageNameSuffix() {
		return "persistence.dbo";
	}

	/**
	 * Generiert eine JPA mapping class for the passed database table service object.
	 * 
	 * @param tableSO The database table service object which the class is to create for.
	 * @returns A JPA mapping class for passed database table or a "null" value if a "null" value is passed.
	 */
	public ClassSourceModel generate(TableSO tableSO) {
		if (tableSO == null) {
			return null;
		}
		ClassSourceModel csm = createClassWithName(tableSO);
		csm.setPackageModel(new PackageSourceModel().setPackageName("${base.package.name}." + getPackageNameSuffix()));
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Column");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Entity");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Id");
		this.classSourceModelUtils.addImport(csm, "javax.persistence", "Table");
		this.classSourceModelUtils.addAnnotation(csm, "Entity");
		this.classSourceModelUtils.addAnnotation(csm, "Table", "name", tableSO.getName());
		for (ColumnSO column : tableSO.getColumns()) {
			AttributeSourceModel asm = this.classSourceModelUtils.addAttributeForColumn(csm, column);
			if (column.isPkMember()) {
				this.classSourceModelUtils.addAnnotation(asm, "Id");
			}
			this.classSourceModelUtils.addAnnotation(asm, "Column", "name", column.getName());
		}
		return csm;
	}

	private ClassSourceModel createClassWithName(TableSO tableSO) {
		return new ClassSourceModel().setName(tableSO.getName() + "DBO");
	}

}