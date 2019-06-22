package rest.acf;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import static org.hamcrest.Matchers.sameInstance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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