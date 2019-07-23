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
import org.junit.Ignore;
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
		System.setProperty("corentx.util.Str.suppress.html.note", "true");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		this.unitUnderTest = new RESTServerCodeFactory();
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackDBOFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(
				Paths.get("src/test/resources/", "de", "ollie", "library", "persistence", "dbo", "RackDBO.java")));
		String generated = new String(
				Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "persistence", "dbo", "RackDBO.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackRepositoryFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/", "de", "ollie", "library",
				"persistence", "repository", "RackRepository.java")));
		String generated = new String(Files.readAllBytes(
				Paths.get(path, "de", "ollie", "library", "persistence", "repository", "RackRepository.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackDTOFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(
				Paths.get("src/test/resources/", "de", "ollie", "library", "rest", "v1", "dto", "RackDTO.java")));
		String generated = new String(
				Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "rest", "v1", "dto", "RackDTO.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackDTOConverterFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/", "de", "ollie", "library",
				"rest", "v1", "converter", "RackDTOConverter.java")));
		String generated = new String(Files.readAllBytes(
				Paths.get(path, "de", "ollie", "library", "rest", "v1", "converter", "RackDTOConverter.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackSOFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(
				Paths.get("src/test/resources/", "de", "ollie", "library", "service", "so", "RackSO.java")));
		String generated = new String(
				Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "service", "so", "RackSO.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackDBOConverterFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/", "de", "ollie", "library",
				"persistence", "converter", "RackDBOConverter.java")));
		String generated = new String(Files.readAllBytes(
				Paths.get(path, "de", "ollie", "library", "persistence", "converter", "RackDBOConverter.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackPersistenceAdapterFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/", "de", "ollie", "library",
				"persistence", "adapter", "RackRDBMSPersistenceAdapter.java")));
		String generated = new String(Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "persistence",
				"adapter", "RackRDBMSPersistenceAdapter.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Test
	public void generate_PassADataModel_CreatesCorrectRackPersistencePortFile() throws Exception {
		String path = "target/test/output";
		if (new File(path).exists()) {
			Files.walk(Paths.get(path)).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
		}
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dm = (Diagramm) reader.read("src/test/resources/library.xml");
		this.unitUnderTest.setDataModel(dm);
		this.unitUnderTest.generate(path);
		assertThat(new File(path).exists(), equalTo(true));
		String expected = new String(Files.readAllBytes(Paths.get("src/test/resources/", "de", "ollie", "library",
				"service", "persistence", "port", "RackPersistencePort.java")));
		String generated = new String(Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "service",
				"persistence", "port", "RackPersistencePort.java")));
		assertEquals(expected.toString(), generated.toString());
	}

	@Ignore
	@Test
	public void generate_PassADataModel_CreatesCorrectCityDBOFile() throws Exception {
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
		String expected = new String(Files.readAllBytes(
				Paths.get("src/test/resources/", "de", "ollie", "library", "persistence", "dbo", "CityDBO.java")));
		String generated = new String(
				Files.readAllBytes(Paths.get(path, "de", "ollie", "library", "persistence", "dbo", "CityDBO.java")));
		assertEquals(expected.toString(), generated.toString());
	}

}