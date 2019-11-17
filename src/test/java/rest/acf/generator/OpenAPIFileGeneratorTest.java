package rest.acf.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.archimedes.alexandrian.service.DatabaseSO;

/**
 * Unit tests for class "OpenAPIFileGenerator".
 *
 * @author ollie (17.11.2019)
 */
@ExtendWith(MockitoExtension.class)
public class OpenAPIFileGeneratorTest {

	private static final String AUTHOR_NAME = "AuthorName";
	private static final String DATABASE_NAME = "DatabaseName";

	@InjectMocks
	private OpenAPIFileGenerator unitUnderTest;

	@DisplayName("Tests of the methode 'generate(DatabaseSO, String)'.")
	@Nested
	class TestOfMethod_generate_DatabaseSO_String {

		@DisplayName("Throws an exception passing a null value as database service object.")
		@Test
		void passANullValueAsDatabaseSO_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.generate(null, AUTHOR_NAME));
		}

		@DisplayName("Returns a YAML file content in string format for the passed database service object.")
		@Test
		void passADatabaseSO_ReturnsAStringWithTheYAMLFileContentForThePassedDatabase() {
			// Prepare
			String expected = "title: " + DATABASE_NAME + "\n";
			DatabaseSO database = new DatabaseSO() //
					.setName(DATABASE_NAME);
			// Run
			String returned = unitUnderTest.generate(database, AUTHOR_NAME);
			// Check
			assertThat(returned, equalTo(expected));
		}

	}

}