package rest.acf.generator.converter;

import java.util.Arrays;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import de.ollie.archimedes.alexandrian.service.ColumnSO;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import de.ollie.archimedes.alexandrian.service.TypeSO;

/**
 * A convert for Archimedes DataModels (old style) to SO's.
 *
 * @author ollie
 *
 */
public class DataModelToSOConverter {

	/**
	 * Converts an old style DataModel to an SO model.
	 * 
	 * @param dataModel The model to convert.
	 * @return A SO model with the values of the passed DataModel.
	 */
	public DatabaseSO convert(DataModel dataModel) {
		if (dataModel == null) {
			return null;
		}
		SchemeSO sso = new SchemeSO().setName("public");
		for (TableModel tm : dataModel.getTables()) {
			TableSO tso = new TableSO().setName(tm.getName());
			for (ColumnModel cm : tm.getColumns()) {
				ColumnSO cso = new ColumnSO().setName(cm.getName());
				DomainModel dom = cm.getDomain();
				TypeSO tpso = new TypeSO().setSqlType(dom.getDataType());
				if (dom.getLength() > 0) {
					tpso.setLength(dom.getLength());
				}
				if (dom.getDecimalPlace() > 0) {
					tpso.setPrecision(dom.getDecimalPlace());
				}
				cso.setType(tpso);
				tso.getColumns().add(cso);
			}
			sso.getTables().add(tso);
		}
		DatabaseSO dbso = new DatabaseSO().setName(dataModel.getName()).setSchemes(Arrays.asList(sso));
		return dbso;
	}

}