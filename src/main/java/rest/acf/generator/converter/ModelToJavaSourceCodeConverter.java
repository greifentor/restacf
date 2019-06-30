package rest.acf.generator.converter;

import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.PropertySourceModel;

/**
 * A converter to convert source models to Java source code.
 * 
 * @author Oliver.Lieshoff
 *
 */
public class ModelToJavaSourceCodeConverter {

	/**
	 * Converts the passed class source model into a Java source code.
	 * 
	 * @param csm The class source model to convert.
	 * @return A string with the Java source of the passed class source model or a "null" value if a "null" value has
	 *         been passed.
	 */
	public String classSourceModelToJavaSourceCode(ClassSourceModel csm) {
		if (csm == null) {
			return null;
		}
		String code = "";
		PackageSourceModel psm = csm.getPackageModel();
		if (psm != null) {
			code += "package " + psm.getPackageName() + ";\n\n\n";
		}
		if (!csm.getImports().isEmpty()) {
			for (ImportSourceModel ism : csm.getImports()) {
				code += "import " + ism.getPackageModel().getPackageName() + ".";
				if (ism.getClassName() != null) {
					code += ism.getClassName();
				} else {
					code += "*";
				}
				code += ";\n";
			}
			code += "\n\n";
		}
		if (csm.getComment() != null) {
			code += csm.getComment().getComment();
		}
		for (AnnotationSourceModel asm : csm.getAnnotations()) {
			code += "@" + asm.getName();
			if (!asm.getProperties().isEmpty()) {
				code += "(";
				boolean first = true;
				for (PropertySourceModel<?> prosm : asm.getProperties()) {
					if (!first) {
						code += ", ";
					}
					code += prosm.getName() + "=" + getJavaConstantValue(prosm.getContent());
					first = false;
				}
				code += ")";
			}
			code += "\n";
		}
		code += "public " + csm.getName() + " {\n";
		code += "\n";
		for (AttributeSourceModel asm : csm.getAttributes()) {
			for (AnnotationSourceModel ansm : asm.getAnnotations()) {
				code += "\t@" + ansm.getName();
				if (!ansm.getProperties().isEmpty()) {
					code += "(";
					boolean first = true;
					for (PropertySourceModel<?> prosm : ansm.getProperties()) {
						if (!first) {
							code += ", ";
						}
						code += prosm.getName() + "=" + getJavaConstantValue(prosm.getContent());
						first = false;
					}
					code += ")";
				}
				code += "\n";
			}
			code += "\tprivate " + asm.getType() + " " + asm.getName() + ";\n";
		}
		code += "\n";
		code += "}";
		return code;
	}

	private String getJavaConstantValue(Object value) {
		if (value instanceof String) {
			return "\"" + String.valueOf(value) + "\"";
		}
		return String.valueOf(value);
	}

}