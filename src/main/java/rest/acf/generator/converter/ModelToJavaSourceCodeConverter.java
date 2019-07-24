package rest.acf.generator.converter;

import java.util.List;
import java.util.Set;

import rest.acf.model.AnnotationSourceModel;
import rest.acf.model.AttributeSourceModel;
import rest.acf.model.ClassSourceModel;
import rest.acf.model.ConstructorSourceModel;
import rest.acf.model.ExtensionSourceModel;
import rest.acf.model.ImportSourceModel;
import rest.acf.model.InterfaceSourceModel;
import rest.acf.model.MethodSourceModel;
import rest.acf.model.ModifierSourceModel;
import rest.acf.model.PackageSourceModel;
import rest.acf.model.ParameterSourceModel;
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
			code += "package " + psm.getPackageName() + ";\n\n";
		}
		String importStart = "";
		if (!csm.getImports().isEmpty()) {
			for (ImportSourceModel ism : csm.getImports()) {
				String packageName = ism.getPackageModel().getPackageName();
				if (!importStart.equals(getFirstPackageNamePart(packageName))) {
					System.out.println(importStart + "-" + getFirstPackageNamePart(packageName));
					if (!importStart.isEmpty()) {
						code += "\n";
					}
					importStart = getFirstPackageNamePart(packageName);
				}
				code += "import " + ism.getPackageModel().getPackageName() + ".";
				if (ism.getClassName() != null) {
					code += ism.getClassName();
				} else {
					code += "*";
				}
				code += ";\n";
			}
			code += "\n";
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
					code += prosm.getName() + " = " + getJavaConstantValue(prosm.getContent());
					first = false;
				}
				code += ")";
			}
			code += "\n";
		}
		code += "public class " + csm.getName() + getImplementsString(csm.getInterfaces()) + " {\n";
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
						code += prosm.getName() + " = " + getJavaConstantValue(prosm.getContent());
						first = false;
					}
					code += ")";
				}
				code += "\n";
			}
			code += "\t" + getModifierString(asm.getModifiers()) + asm.getType() + " " + asm.getName() + ";\n";
		}
		if (csm.getAttributes().size() > 0) {
			code += "\n";
		}
		for (ConstructorSourceModel cosm : csm.getConstructors()) {
			code += "\tpublic " + csm.getName() + "(";
			String paramStr = "";
			for (ParameterSourceModel param : cosm.getParameters()) {
				if (!paramStr.isEmpty()) {
					paramStr += ", ";
				}
				paramStr += param.getType() + " " + param.getName();
			}
			code += paramStr;
			code += ") {\n";
			code += cosm.getCode() + "\n";
		}
		for (MethodSourceModel method : csm.getMethods()) {
			for (AnnotationSourceModel ansm : method.getAnnotations()) {
				code += "\t@" + ansm.getName();
				if (!ansm.getProperties().isEmpty()) {
					code += "(";
					boolean first = true;
					for (PropertySourceModel<?> prosm : ansm.getProperties()) {
						if (!first) {
							code += ", ";
						}
						code += prosm.getName() + " = " + getJavaConstantValue(prosm.getContent());
						first = false;
					}
					code += ")";
				}
				code += "\n";
			}
			code += "\t" + getModifierString(method.getModifiers()) + method.getReturnType() + " " + method.getName()
					+ "(";
			String paramStr = "";
			for (ParameterSourceModel param : method.getParameters()) {
				if (!paramStr.isEmpty()) {
					paramStr += ", ";
				}
				paramStr += param.getType() + " " + param.getName();
			}
			code += paramStr;
			code += ") {\n";
			code += method.getCode() + "\n";
		}
		code += "}";
		return code;
	}

	private String getFirstPackageNamePart(String s) {
		if (s.contains(".")) {
			return s.substring(0, s.indexOf("."));
		}
		return s;
	}

	private String getImplementsString(List<ExtensionSourceModel> interfaces) {
		String s = "";
		for (ExtensionSourceModel esm : interfaces) {
			if (!s.isEmpty()) {
				s += ",";
			} else {
				s = " implements";
			}
			s += " " + esm.getParentClassName();
		}
		return s;
	}

	private String getJavaConstantValue(Object value) {
		if (value instanceof String) {
			return "\"" + String.valueOf(value) + "\"";
		}
		return String.valueOf(value);
	}

	private String getModifierString(Set<ModifierSourceModel> modifiers) {
		String s = "";
		if (modifiers.contains(ModifierSourceModel.PRIVATE)) {
			s += "private ";
		}
		if (modifiers.contains(ModifierSourceModel.PUBLIC)) {
			s += "public ";
		}
		if (modifiers.contains(ModifierSourceModel.STATIC)) {
			s += "static ";
		}
		if (modifiers.contains(ModifierSourceModel.FINAL)) {
			s += "final ";
		}
		return s;
	}

	/**
	 * Converts the passed interface source model into a Java source code.
	 * 
	 * @param insm The interface source model to convert.
	 * @return A string with the Java source of the passed class source model or a "null" value if a "null" value has
	 *         been passed.
	 */
	public String interfaceSourceModelToJavaSourceCode(InterfaceSourceModel insm) {
		if (insm == null) {
			return null;
		}
		String code = "";
		PackageSourceModel psm = insm.getPackageModel();
		if (psm != null) {
			code += "package " + psm.getPackageName() + ";\n\n";
		}
		String importStart = "";
		if (!insm.getImports().isEmpty()) {
			for (ImportSourceModel ism : insm.getImports()) {
				String packageName = ism.getPackageModel().getPackageName();
				if (!importStart.equals(getFirstPackageNamePart(packageName))) {
					System.out.println(importStart + "-" + getFirstPackageNamePart(packageName));
					if (!importStart.isEmpty()) {
						code += "\n";
					}
					importStart = getFirstPackageNamePart(packageName);
				}
				code += "import " + ism.getPackageModel().getPackageName() + ".";
				if (ism.getClassName() != null) {
					code += ism.getClassName();
				} else {
					code += "*";
				}
				code += ";\n";
			}
			code += "\n";
		}
		if (insm.getComment() != null) {
			code += insm.getComment().getComment();
		}
		for (AnnotationSourceModel asm : insm.getAnnotations()) {
			code += "@" + asm.getName();
			if (!asm.getProperties().isEmpty()) {
				code += "(";
				boolean first = true;
				for (PropertySourceModel<?> prosm : asm.getProperties()) {
					if (!first) {
						code += ", ";
					}
					code += prosm.getName() + " = " + getJavaConstantValue(prosm.getContent());
					first = false;
				}
				code += ")";
			}
			code += "\n";
		}
		code += "public interface " + insm.getName() + createExtendsStatement(insm.getExtendsModel()) + " {\n";
		boolean hasMethod = false;
		for (MethodSourceModel method : insm.getMethods()) {
			hasMethod = true;
			code += "\n\t" + method.getReturnType() + " " + method.getName() + "(";
			String paramStr = "";
			for (ParameterSourceModel param : method.getParameters()) {
				if (!paramStr.isEmpty()) {
					paramStr += ", ";
				}
				paramStr += param.getType() + " " + param.getName();
			}
			code += paramStr;
			code += ");\n\n";
		}
		code += "}";
		return code;
	}

	private String createExtendsStatement(ExtensionSourceModel esm) {
		if (esm == null) {
			return "";
		}
		return " extends " + esm.getParentClassName();
	}

}