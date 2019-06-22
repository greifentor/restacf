package rest.acf;

import java.util.ArrayList;
import java.util.List;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.model.CodeFactory;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;

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
	public boolean generate(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GUIBundle getGUIBundle() {
		return this.guiBundle;
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		// TODO ModelCheckers should defined here!
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