package net.bounceme.chronos.inteligenciaartificial.exceptions;

import lombok.Getter;

public class AssertException extends IllegalArgumentException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Getter
	private final String code;
	
	public AssertException() {
		super();
		code = "";
	}
	
	public AssertException(String s) {
		super(s);
		code = "";
	}

	public AssertException(String s, String code) {
		super(s);
		this.code = code;
	}
}
