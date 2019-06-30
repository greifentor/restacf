package rest.acf;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import de.ollie.archimedes.alexandrian.service.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.SchemeSO;
import de.ollie.archimedes.alexandrian.service.TableSO;
import rest.acf.generator.converter.DataModelToSOConverter;
import rest.acf.generator.converter.ModelToJavaSourceCodeConverter;
import rest.acf.generator.converter.NameConverter;
import rest.acf.generator.converter.TypeConverter;
import rest.acf.generator.persistence.JPAClassGenerator;
import rest.acf.generator.utils.ClassSourceModelUtils;
import rest.acf.model.ClassSourceModel;

/**
 * A basic class for all REST code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.06.2019 - Added.
 */
public class RESTServerCodeFactory implements CodeFactory {

	private DataModel dataModel = null;
	private GUIBundle guiBundle = null;
	private List<CodeFactoryListener> listeners = new ArrayList<>();

	@Override
	public void addCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public boolean generate(String path) {
		new File(path).mkdirs();
		JPAClassGenerator generator = new JPAClassGenerator(
				new ClassSourceModelUtils(new NameConverter(), new TypeConverter()), new NameConverter(),
				new TypeConverter());
		DatabaseSO databaseSO = new DataModelToSOConverter().convert(this.dataModel);
		for (SchemeSO scheme : databaseSO.getSchemes()) {
			for (TableSO table : scheme.getTables()) {
				ClassSourceModel csm = generator.generate(table);
				String p = path + "/" + csm.getPackageModel().getPackageName()
						.replace("${base.package.name}", "de.ollie.library").replace(".", "/");
				new File(p).mkdirs();
				String code = new ModelToJavaSourceCodeConverter().classSourceModelToJavaSourceCode(csm);
				try {
					Files.writeString(Paths.get(p + "/" + table.getName() + "DBO.java"), code,
							StandardOpenOption.CREATE_NEW);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public GUIBundle getGUIBundle() {
		return this.guiBundle;
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[0];
	}

	@Override
	public String getName() {
		return "REST Server Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		// TODO Define the resource bundle names here.
		return new String[0];
	}

	@Override
	public String getVersion() {
		return "0.0.1";
	}

	@Override
	public void removeCodeFactoryListener(CodeFactoryListener listener) {
		this.listeners.remove(listener);
	}

	@Override
	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@Override
	public void setGUIBundle(GUIBundle guiBundle) {
		this.guiBundle = guiBundle;
	}

	@Override
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		// ???
	}

}