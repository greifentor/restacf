package rest.acf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.acf.checker.ModelChecker;
import baccara.gui.GUIBundle;

/**
 * Unit tests of class "RESTServerCodeFactory".
 * 
 * @author ollie
 *
 */
@ExtendWith(MockitoExtension.class)
public class RESTServerCodeFactoryTest {

	@InjectMocks
	private RESTServerCodeFactory unitUnderTest;

	@Test
	@Disabled
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