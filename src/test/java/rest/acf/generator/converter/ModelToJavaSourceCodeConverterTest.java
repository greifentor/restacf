package rest.acf.generator.converter;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests of the class "ModelToJavaSourceCodeConverter".
 *
 * @author Oliver.Lieshoff
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ModelToJavaSourceCodeConverterTest {

	@InjectMocks
	private ModelToJavaSourceCodeConverter unitUnderTest;

	@Test
	public void classSourceModelToJavaSourceCode_PassANullValue_ReturnsANullValue() {
		assertThat(this.unitUnderTest.classSourceModelToJavaSourceCode(null),
				nullValue());
	}

}