package de.ollie.utils;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

/**
 * Unit tests for class "Check".
 *
 * @author ollie
 *
 */
public class CheckTest {

	private static final String MESSAGE = "message";

	@Test(expected = UnsupportedOperationException.class)
	public void constructor_ThrowsAnUnsupportedOperationException() {
		new Check();
	}

	@Test
	public void ensure_TrueConditionPassed_DoesNtThrowAnException() {
		try {
			Check.ensure(true, new RuntimeException());
		} catch (RuntimeException e) {
			fail("no exception should thrown.");
		}
	}

	@Test
	public void ensure_FalseConditionPassed_ThrowsThePassedException() {
		RuntimeException e = new RuntimeException();
		try {
			Check.ensure(false, e);
			fail("should throw a runtime exception.");
		} catch (RuntimeException e0) {
			assertSame("thrown exception should be same as the passed one", e, e0);
		}
	}

	@Test
	public void ensure_TrueConditionAndAStringPassed_DoesNtThrowAnException() {
		try {
			Check.ensure(true, MESSAGE);
		} catch (RuntimeException e) {
			fail("no exception should thrown.");
		}
	}

	@Test
	public void ensure_FalseConditionAndAStringPassed_ThrowsAnIllegalArgumentExceptionWithThePassedMessage() {
		try {
			Check.ensure(false, MESSAGE);
			fail("should throw a runtime exception.");
		} catch (RuntimeException e) {
			assertTrue("exception is not an IllegalArgumentException but a " + e.getClass().getSimpleName(),
					e instanceof IllegalArgumentException);
			assertThat(e.getMessage(), equalTo(MESSAGE));
		}
	}

}