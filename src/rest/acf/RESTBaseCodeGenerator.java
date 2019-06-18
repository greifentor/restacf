/*
 * RESTBaseCodeGenerator.java
 *
 * 17.06.2019
 *
 * (c) by O.Lieshoff
 *
 */
package rest.acf;

import java.util.LinkedList;
import java.util.List;

import archimedes.acf.AbstractCodeGenerator;
import archimedes.acf.SetterConfiguration;
import archimedes.acf.param.DomainParamIds;
import archimedes.acf.param.TableParamIds;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;
import corentx.io.FileUtil;
import corentx.util.Checks;
import corentx.util.SortedVector;
import gengen.IndividualPreferences;

/**
 * A basic class for all REST code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.06.2019 - Added.
 */
abstract public class RESTBaseCodeGenerator extends AbstractCodeGenerator {

	@Override
	public String createAbsoluteFileName(IndividualPreferences individualPreferences, DataModel model,
			String subProjectName, String className, String packageName, boolean specialAPI, TableModel tm) {
		String p = tm.getDataModel().getBasicCodePath() + "/" + this.getPackageName(tm).replace(".", "/") + "/"
				+ this.getClassName(tm) + "." + this.getFileSuffix();
		if (individualPreferences.getBaseCodePath(model.getName()) != null) {
			p = FileUtil.completePath(individualPreferences.getBaseCodePath(model.getName())) + p;
		}
		return p;
	}

	@Override
	public String createChangeListenerLogic(ColumnModel c, String attrName, SetterConfiguration setterConfiguration) {
//		String attrIdClassName = GeneratedPOJOAttributeIdCodeGenerator.className(setterConfiguration.getTable());
//		String eventClassName = GeneratedPOJOChangeListenerEventCodeGenerator.className(setterConfiguration.getTable());
//		String methodName = "fire" + eventClassName;
//		return "        this." + methodName + "(new " + eventClassName + "(" + attrIdClassName + "."
//				+ this.createAttrIdName(c) + ", " + attrName + ", " + eventClassName + ".Type.SET));\n";
		return ":op";
	}

	/**
	 * Sorts the passed columns by their editor positions.
	 *
	 * @param cols The columns to sort.
	 *
	 * @changed OLI 23.07.2016 - Added.
	 */
	public ColumnModel[] assortColumnsByEditorPosition(ColumnModel[] cols) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : cols) {
			boolean added = false;
			for (int i = 0, leni = l.size(); i < leni; i++) {
				if (l.get(i).getEditorPosition() > c.getEditorPosition()) {
					l.add(i, c);
					added = true;
					break;
				}
			}
			if (!added) {
				l.add(c);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns the columns of the passed table including the inherited columns.
	 *
	 * @param tm The table whose columns (and inherited ones) should be returned.
	 * @return The columns of the passed table including the inherited columns.
	 *
	 * @changed OLI 07.07.2016 - Added.
	 */
	public ColumnModel[] getColumnsIncludingInherited(TableModel tm) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		this.findColumnsIncludingInherited(l, tm);
		return l.toArray(new ColumnModel[0]);
	}

	private void findColumnsIncludingInherited(List<ColumnModel> l, TableModel tm) {
		for (ColumnModel c : tm.getColumns()) {
			if ((this.currentPanelModel == null)
					|| (this.currentPanelModel.getPanelNumber() == c.getPanel().getPanelNumber())) {
				l.add(c);
			}
		}
//		if (tm.isOptionSet(TableParamIds.INHERITS)) {
//			String tn = tm.getOptionByName(TableParamIds.INHERITS).getParameter();
//			this.findColumnsIncludingInherited(l, tm.getDataModel().getTableByName(tn));
//		}
	}

	/**
	 * @changed OLI 06.07.2016 - Added.
	 */
	@Override
	public String getEnumClassName(DomainModel dm) {
		String enumClassName = this.parameterUtil.getParameterStartsWith(DomainParamIds.ENUM, dm);
		if (enumClassName == null) {
			return null;
		}
		return enumClassName.substring(enumClassName.indexOf(":") + 1).trim();
	}

	/**
	 * @changed OLI 06.07.2016 - Added.
	 */
	@Override
	public String getJavaType(ColumnModel column, boolean referencesToKeyClass, boolean timestampsWrapped) {
		if (column.getDomain().getDataType() == 93) {
			this.imports.add("java.sql");
		} else if ((!(referencesToKeyClass)) && (column.getReferencedTable() != null)) {
			if (getAlternateReferenceTableClassName(column.getReferencedTable()) != null) {
				return getAlternateReferenceTableClassName(column.getReferencedTable());
			}
//			return POJOCodeGenerator.className(column.getReferencedTable());
			return ";op";
		}
		return this.typeUtil.getJavaType(column, referencesToKeyClass, timestampsWrapped, isEntityGenerator(),
				this.parameterUtil, this.generatorUtil, this.imports);
	}

	/**
	 * Returns the name for a list attribute of the passed table.
	 *
	 * @param t The table which the list attribute name is to create for.
	 * @return The list attribute name for the table.
	 *
	 * @changed OLI 17.05.2016 - Added.
	 */
	public String getListAttributeName(TableModel t) {
		return this.getAttributeName(t) + "s";
	}

	/**
	 * Checks if the passed table has list inclusions.
	 *
	 * @param table The table to check.
	 * @return An array of the tables whose data records are included in a list by the passed table or an empty array if
	 *         no list inclusions are detected.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 17.05.2016 - Added.
	 */
	public TableModel[] getListInclusions(TableModel table) {
		Checks.ensure(table != null, "table cannot be null.");
		List<TableModel> l = new SortedVector<TableModel>();
		for (OptionModel o : table.getOptions()) {
			if (TableParamIds.INCLUDE_LIST.equals(o.getName())) {
				TableModel t = table.getDataModel().getTableByName(o.getParameter());
				if (t != null) {
					l.add(t);
				}
			}
		}
		return l.toArray(new TableModel[0]);
	}

	/**
	 * @changed OLI 04.05.2016 - Added.
	 */
	@Override
	public String getOrgApacheCommonsLangBuilderPackageName() {
		return "org.apache.commons.lang3.builder";
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public String getPackageName(TableModel tm) {
		String bpn = tm.getDataModel().getBasePackageName()
				+ ((tm.getCodeFolder() != null) && (tm.getCodeFolder().length() > 0) ? "/" + tm.getCodeFolder() : "")
				+ "/" + this.getSubPackage();
		return bpn.replace("\\", "/").replace("/", ".");
	}

	/**
	 * Returns the plural form of the passed string. If the string contains more than one word, only the last one will
	 * be set to its plural form.
	 *
	 * @param s The string which is to set to its plural form.
	 * @return The plural form of the passed string.
	 * @throws IllegalArgumentException Passing an empty or a null string.
	 *
	 * @changed OLI 22.07.2016 - Added.
	 */
	public String getPlural(String s) {
		Checks.ensure(s != null, "string cannot be null.");
		Checks.ensure(!s.isEmpty(), "string cannot be empty.");
		if (s.endsWith("y")) {
			s = s.substring(0, s.length() - 1);
			s += "ies";
		} else if (s.endsWith("Y")) {
			s = s.substring(0, s.length() - 1);
			s += "IES";
		} else if (s.endsWith("s")) {
			s += "es";
		} else if (s.endsWith("S")) {
			s += "ES";
		} else {
			String l = s.substring(s.length() - 1, s.length());
			if (l.equals(l.toUpperCase())) {
				s += "S";
			} else {
				s += "s";
			}
		}
		return s;
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public String getSrcSubPath() {
		return null;
	}

	/**
	 * Returns the sub package (e. g. "archgen" or "business") which the code is generated to.
	 *
	 * @return The sub package (e. g. "archgen" or "business") which the code is generated to.
	 */
	public String getSubPackage() {
		return "archgen";
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public String getSubProjectFolder(DataModel dm) {
		return null;
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public String getSubProjectSuffix(DataModel model, TableModel tm) {
		return null;
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public boolean isEnum(DomainModel domain) {
		return this.parameterUtil.getParameterStartsWith(DomainParamIds.ENUM, domain) != null;
	}

	/**
	 * @changed OLI 05.04.2016 - Added.
	 */
	@Override
	public boolean isOneTimeFactory() {
		return false;
	}

}