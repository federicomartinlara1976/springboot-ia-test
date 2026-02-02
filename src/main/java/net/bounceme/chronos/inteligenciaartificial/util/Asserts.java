package net.bounceme.chronos.inteligenciaartificial.util;

import lombok.experimental.UtilityClass;
import net.bounceme.chronos.inteligenciaartificial.exceptions.AssertException;

@UtilityClass
public class Asserts {
	
	public void assertNotNull(Object o) {
		if (o == null) {
			throw new AssertException();
		}
	}
	
	public void assertNotNull(Object o, String message) {
		if (o == null) {
			throw new AssertException(message);
		}
	}
	
	public void assertNotNull(Object o, String message, String code) {
		if (o == null) {
			throw new AssertException(message, code);
		}
	}
	
	public void assertTrue(boolean expression) {
		if (!expression) {
			throw new AssertException();
		}
	}

	public void assertTrue(boolean expression, String message) {
		if (!expression) {
			throw new AssertException(message);
		}
	}
	
	public void assertTrue(boolean expression, String message, String code) {
		if (!expression) {
			throw new AssertException(message, code);
		}
	}
	
	public void assertFalse(boolean expression) {
		if (expression) {
			throw new AssertException();
		}
	}
	
	public void assertFalse(boolean expression, String message) {
		if (expression) {
			throw new AssertException(message);
		}
	}
	
	public void assertFalse(boolean expression, String message, String code) {
		if (expression) {
			throw new AssertException(message, code);
		}
	}
}

