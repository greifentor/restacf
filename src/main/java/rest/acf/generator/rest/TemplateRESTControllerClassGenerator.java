package rest.acf.generator.rest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.utils.ClassSourceModelUtils;

/**
 * A template based class generator for a REST controller class.
 *
 * @author ollie (08.10.2019)
 */
public class TemplateRESTControllerClassGenerator {

	private ClassSourceModelUtils utils = new ClassSourceModelUtils(new NameConverter(), new TypeConverter());
	private NameConverter nameConverter = new NameConverter();

	public String generate(TableSO table, DatabaseSO database, String authorName) {
		String code = "";
		List<String> imports = new ArrayList<>();
		imports.add("java.util.ArrayList");
		imports.add("java.util.List");
		imports.add("java.util.Optional");
		imports.add("");
		imports.add("org.apache.logging.log4j.LogManager");
		imports.add("org.apache.logging.log4j.Logger");
		imports.add("org.springframework.beans.factory.annotation.Autowired");
		imports.add("org.springframework.http.HttpStatus");
		imports.add("org.springframework.http.ResponseEntity");
		imports.add("org.springframework.web.bind.annotation.DeleteMapping");
		imports.add("org.springframework.web.bind.annotation.GetMapping");
		imports.add("org.springframework.web.bind.annotation.PathVariable");
		imports.add("org.springframework.web.bind.annotation.PostMapping");
		imports.add("org.springframework.web.bind.annotation.RequestBody");
		imports.add("org.springframework.web.bind.annotation.RequestMapping");
		imports.add("org.springframework.web.bind.annotation.RestController");
		imports.add("");
		imports.add("${base.package.name}.rest.v1.converter.${dtoConverter.class.name}");
		imports.add("${base.package.name}.rest.v1.dto.${dto.class.name}");
		imports.add("${base.package.name}.rest.v1.dto.ResultPageDTO");
		imports.add("${base.package.name}.service.${service.class.name}");
		imports.add("${base.package.name}.service.so.${so.class.name}");
		imports.add("${base.package.name}.service.so.ResultPageSO");
		try {
			code = getCodeFromTemplate(Files
					.readAllLines(Paths.get(System.getProperty("restacf.template.path", "src/main/resources/templates/")
							+ "RESTController.template")));
			code = code.replace("$^{foreign.attribute.code.block}\n",
					getForeignAttributeCodeBlock(table, database, imports));
			code = code.replace("$^{findXXXForYYY.code.block}\n",
					getForeignFindXXXForYYYCodeBlock(table, database, imports));
			code = code.replace("$^{imports}\n", getImportsFromList(imports));
			code = code.replace("${author.name}", authorName);
			code = code.replace("${attribute.name}", this.nameConverter.classNameToAttrName(table.getName()));
			code = code.replace("${class.name}", this.utils.createRESTControllerClassSourceModel(table).getName());
			code = code.replace("${table.name}", this.nameConverter.getClassName(table));
			String dtoConverterClassName = this.utils.createDTOConverterClassSourceModel(table).getName();
			code = code.replace("${dtoConverter.class.name}", dtoConverterClassName);
			code = code.replace("${dtoConverter.attribute.name}",
					this.nameConverter.classNameToAttrName(dtoConverterClassName));
			String serviceClassName = this.utils.createServiceInterfaceSourceModel(table).getName();
			code = code.replace("${service.class.name}", serviceClassName);
			code = code.replace("${service.attribute.name}", this.nameConverter.classNameToAttrName(serviceClassName));
			String soClassName = this.utils.createSOClassSourceModel(table).getName();
			code = code.replace("${so.class.name}", soClassName);
			code = code.replace("${so.attribute.name}", this.nameConverter.classNameToAttrName(soClassName));
			String dtoClassName = this.utils.createDTOClassSourceModel(table).getName();
			code = code.replace("${dto.class.name}", dtoClassName);
			code = code.replace("${dto.attribute.name}", this.nameConverter.classNameToAttrName(dtoClassName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code.substring(0, code.length() - 1);
	}

	private String getCodeFromTemplate(List<String> l) {
		StringBuilder code = new StringBuilder();
		l.forEach(s -> code.append(s).append("\n"));
		return code.toString();
	}

	private String getImportsFromList(List<String> l) {
		StringBuilder code = new StringBuilder();
		l.forEach(s -> {
			if (!s.isEmpty()) {
				code.append("import ").append(s).append(";");
			}
			code.append("\n");
		});
		return code.toString();
	}

	private String getForeignAttributeCodeBlock(TableSO table, DatabaseSO database, List<String> imports) {
		String code = "";
		try {
			List<ReferenceSO> refs = getReferencingTables(table, database);
			if (refs.size() > 0) {
				String template = getCodeFromTemplate(Files.readAllLines(
						Paths.get(System.getProperty("restacf.template.path", "src/main/resources/templates/")
								+ "RESTController-foreign-attributeCodeBlock.template")));
				for (ReferenceSO ref : refs) {
					TableSO refTable = ref.getReferencingColumn().getTable();
					code += template;
					String dtoConverterClassName = this.utils.createDTOConverterClassSourceModel(refTable).getName();
					code = code.replace("${foreign.dtoConverter.class.name}", dtoConverterClassName);
					code = code.replace("${foreign.dtoConverter.attribute.name}",
							this.nameConverter.classNameToAttrName(dtoConverterClassName));
					imports.add("${base.package.name}." + this.utils.createDTOConverterPackageNameSuffix() + "."
							+ dtoConverterClassName);
					String serviceClassName = this.utils.createServiceInterfaceSourceModel(refTable).getName();
					code = code.replace("${foreign.service.class.name}", serviceClassName);
					code = code.replace("${foreign.service.attribute.name}",
							this.nameConverter.classNameToAttrName(serviceClassName));
					imports.add("${base.package.name}." + this.utils.createServicePackageNameSuffix() + "."
							+ serviceClassName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	private String getForeignFindXXXForYYYCodeBlock(TableSO table, DatabaseSO database, List<String> imports) {
		String code = "";
		try {
			List<ReferenceSO> refs = getReferencingTables(table, database);
			if (refs.size() > 0) {
				String template = getCodeFromTemplate(Files.readAllLines(
						Paths.get(System.getProperty("restacf.template.path", "src/main/resources/templates/")
								+ "RESTController-findXXXForYYY.template")));
				for (ReferenceSO ref : refs) {
					TableSO refTable = ref.getReferencingColumn().getTable();
					code += template;
					code = code.replace("${foreign.attribute.name}",
							this.nameConverter.classNameToAttrName(refTable.getName()));
					String dtoClassName = this.utils.createDTOClassSourceModel(refTable).getName();
					code = code.replace("${foreign.dto.class.name}", dtoClassName);
					code = code.replace("${foreign.dto.attribute.name}",
							this.nameConverter.classNameToAttrName(dtoClassName));
					imports.add("${base.package.name}." + this.utils.createDTOPackageNameSuffix() + "." + dtoClassName);
					String soClassName = this.utils.createSOClassSourceModel(refTable).getName();
					code = code.replace("${foreign.so.class.name}", soClassName);
					code = code.replace("${foreign.so.attribute.name}",
							this.nameConverter.classNameToAttrName(soClassName));
					imports.add("${base.package.name}." + this.utils.createSOPackageNameSuffix() + "." + soClassName);
					String dtoConverterClassName = this.utils.createDTOConverterClassSourceModel(refTable).getName();
					code = code.replace("${foreign.dtoConverter.class.name}", dtoConverterClassName);
					code = code.replace("${foreign.dtoConverter.attribute.name}",
							this.nameConverter.classNameToAttrName(dtoConverterClassName));
					String serviceClassName = this.utils.createServiceInterfaceSourceModel(refTable).getName();
					code = code.replace("${foreign.service.class.name}", serviceClassName);
					code = code.replace("${foreign.service.attribute.name}",
							this.nameConverter.classNameToAttrName(serviceClassName));
					code = code.replace("${foreign.table.name}", this.nameConverter.getClassName(refTable));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return code + (code.length() > 0 ? "\n" : "");
	}

	private List<ReferenceSO> getReferencingTables(TableSO table, DatabaseSO database) {
		List<ReferenceSO> refs = new ArrayList<>();
		for (SchemeSO scheme : database.getSchemes()) {
			for (TableSO t : scheme.getTables()) {
				for (ForeignKeySO fk : t.getForeignKeys()) {
					for (ReferenceSO ref : fk.getReferences()) {
						if (ref.getReferencedColumn().getTable() == table) {
							refs.add(ref);
						}
					}
				}
			}
		}
		return refs;
	}

}