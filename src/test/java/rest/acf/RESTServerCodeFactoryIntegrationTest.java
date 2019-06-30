package rest.acf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import archimedes.model.DataModel;
import archimedes.scheme.ArchimedesObjectFactory;
import archimedes.scheme.Diagramm;
import archimedes.scheme.xml.ModelXMLReader;

/**
 * An integration test for class "RESTServerCodeFactory".
 * 
 * @author ollie
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RESTServerCodeFactoryIntegrationTest {

	private RESTServerCodeFactory unitUnderTest;

	@Before
	public void setUp() {
		this.unitUnderTest = new RESTServerCodeFactory();
	}

	@Test
	public void generate_PassADataModel_CreatesAllFilesFromTheDataModel() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/TestDataModel.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = Files.readString(
				Paths.get("src/test/resources/", "de", "ollie", "library", "persistence", "dbo", "CityDBO.java"));
		String generated = Files
				.readString(Paths.get(path, "de", "ollie", "library", "persistence", "dbo", "CityDBO.java"));
		assertEquals(expected, generated);
	}

}