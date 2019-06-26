package rest.acf.generator.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * A generator for JPA mapping objects.
 *
 * @author ollie
 *
 */
public class JPAClassGenerator {

	private final NameConverter nameConverter;
	private final TypeConverter typeConverter;

	/**
	 * Create a new JPA class generator with the passed parameters.
	 * 
	 * @param nameConverter An access to the name converter of the application.
	 * @param typeConverter An access to the type converter of the application.
	 */
	public JPAClassGenerator(NameConverter nameConverter, TypeConverter typeConverter) {
		super();
		this.nameConverter = nameConverter;
		this.typeConverter = typeConverter;
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
		ClassSourceModel csm = new ClassSourceModel().setName(tableSO.getName() + "DBO");
		List<ImportSourceModel> imports = new ArrayList<>();
		imports.add(new ImportSourceModel().setClassName("Entity")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence")));
		imports.add(new ImportSourceModel().setClassName("Table")
				.setPackageModel(new PackageSourceModel().setPackageName("javax.persistence")));
		csm.setImports(imports);
		List<AnnotationSourceModel> annotations = new ArrayList<>();
		annotations.add(new AnnotationSourceModel().setName("Entity"));
		annotations.add(new AnnotationSourceModel().setName("Table").setProperties(
				Arrays.asList(new PropertySourceModel<String>().setName("name").setContent(tableSO.getName()))));
		csm.setAnnotations(annotations);
		List<AttributeSourceModel> attributes = new ArrayList<>();
		for (ColumnSO column : tableSO.getColumns()) {
			String attributeName = this.nameConverter.columnNameToAttributeName(column);
			String typeName = this.typeConverter.typeSOToTypeString(column.getType(), column.isNullable());
			attributes.add(new AttributeSourceModel().setName(attributeName).setType(typeName));
		}
		csm.setAttributes(attributes);
		return csm;
	}

}