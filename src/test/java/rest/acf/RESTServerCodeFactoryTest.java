package rest.acf;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

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

}