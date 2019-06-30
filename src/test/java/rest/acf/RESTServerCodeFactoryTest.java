package rest.acf;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;

/**
 * Unit tests of class "RESTServerCodeFactory".
 * 
 * @author ollie
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RESTServerCodeFactoryTest {

	@InjectMocks
	private RESTServerCodeFactory unitUnderTest;

	@Test
	public void addCodeFactoryListener_PassAListener_AddsTheListenerToTheObject() {
		// Prepare
		CodeFactoryListener listener = mock(CodeFactoryListener.class);
		// Run
		this.unitUnderTest.addCodeFactoryListener(listener);
		// Check
		@SuppressWarnings("unchecked")
		List<CodeFactoryListener> listeners = (List<CodeFactoryListener>) Whitebox.getInternalState(this.unitUnderTest,
				"listeners");
		assertThat(listeners.get(0), sameInstance(listener));
	}

	@Test
	@Ignore
	public void generate_PassANullValue_ReturnsFalse() {
		// Prepare
		boolean expected = false;
		// Run
		boolean returned = this.unitUnderTest.generate(null);
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getModelChecker_ReturnsAnEmptyArray() {
		// Prepare
		ModelChecker[] expected = new ModelChecker[0];
		// Run
		ModelChecker[] returned = this.unitUnderTest.getModelCheckers();
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getName_ReturnsTheCorrectName() {
		// Prepare
		String expected = "REST Server Code Factory";
		// Run
		String returned = this.unitUnderTest.getName();
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getResourceBundleNames_ReturnsTheCorrectResourceBundleName() {
		// Prepare
		String[] expected = new String[] { "archimedes" };
		// Run
		String[] returned = this.unitUnderTest.getResourceBundleNames();
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void getVersion_ReturnsTheCorrectVersion() {
		// Prepare
		String expected = "0.0.1";
		// Run
		String returned = this.unitUnderTest.getVersion();
		// Check
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void removeCodeFactoryListener_PassAListener_RemovesThePassedListener() {
		// Prepare
		CodeFactoryListener listener = mock(CodeFactoryListener.class);
		this.unitUnderTest.addCodeFactoryListener(listener);
		// Run
		this.unitUnderTest.removeCodeFactoryListener(listener);
		// Check
		@SuppressWarnings("unchecked")
		List<CodeFactoryListener> listeners = (List<CodeFactoryListener>) Whitebox.getInternalState(this.unitUnderTest,
				"listeners");
		assertThat(listeners.size(), equalTo(0));
	}

	@Test
	public void setDataModel_PassADataModel_SetsTheDataModelCorrectly() {
		// Prepare
		DataModel expected = mock(DataModel.class);
		// Run
		this.unitUnderTest.setDataModel(expected);
		// Check
		DataModel returned = (DataModel) Whitebox.getInternalState(this.unitUnderTest, "dataModel");
		assertThat(returned, equalTo(expected));
	}

	@Test
	public void setGUIBundle_PassAGUIBundle_SetsTheGUIBundleCorrectly() {
		// Prepare
		GUIBundle expected = mock(GUIBundle.class);
		// Run
		this.unitUnderTest.setGUIBundle(expected);
		// Check
		GUIBundle returned = this.unitUnderTest.getGUIBundle();
		assertThat(returned, sameInstance(expected));
	}

}