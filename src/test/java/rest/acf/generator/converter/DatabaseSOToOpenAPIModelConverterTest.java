package rest.acf.generator.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import rest.acf.generator.openapi.OpenAPIModel;

/**
 * Unit tests for class "OpenAPIFileGenerator".
 *
 * @author ollie (17.11.2019)
 */
@ExtendWith(MockitoExtension.class)
public class DatabaseSOToOpenAPIModelConverterTest {

	private static final String DATABASE_NAME = "DatabaseName";
	private static final String VERSION = "1.0";

	@InjectMocks
	private DatabaseSOToOpenAPIModelConverter unitUnderTest;

	@DisplayName("Tests of the methode 'convert(DatabaseSO)'.")
	@Nested
	class TestOfMethod_convert_DatabaseSO {

		@DisplayName("Throws an exception passing a null value as database service object.")
		@Test
		void passANullValueAsDatabaseSO_ThrowsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.convert(null));
		}

		@DisplayName("Returns a correct OpenAPI model for the passed database service object.")
		@Test
		void passANullValueAsDatabaseSO_ReturnsACorrectOpenAPIModelForThePassedDatabaseServiceObject() {
			// Prepare
			DatabaseSO database = new DatabaseSO() //
					.setName(DATABASE_NAME);
			OpenAPIModel expected = new OpenAPIModel() //
					.setTitle(DATABASE_NAME);
			// Run
			OpenAPIModel returned = unitUnderTest.convert(database);
			// Check
			assertThat(returned, equalTo(expected));
		}

	}

}