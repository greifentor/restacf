/*
 * VersionTest.java
 *
 * **.**.**** (automatisch generiert)
 *
 * (c) by O.Lieshoff
 *
 */

package rest.acf;

import static org.junit.Assert.*;

import org.junit.*;

/**
 * Tests zur Klasse VersionTest.
 *
 * @author O.Lieshoff
 *
 * @changed *** **.**.**** - Automatisch generiert.
 */

public class VersionTest {

	@Test
	/** Test auf bestehen der statischen Instanz. */
	public void testInstanceNotNull() {
		assertNotNull(Version.INSTANCE);
	}

	@Test
	/** Test der Methode <TT>getVersion()</TT>. */
	public void testGetVersion() {
		assertEquals("1.1.1", Version.INSTANCE.getVersion());
	}

	@Test
	/** Test der Methode <TT>toString()</TT>. */
	public void testToString() {
		assertEquals("1.1.1", Version.INSTANCE.toString());
	}

}
