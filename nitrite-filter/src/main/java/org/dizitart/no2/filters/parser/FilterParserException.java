package org.dizitart.no2.filters.parser;

public class FilterParserException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8067345284399903977L;

	public FilterParserException() {
		super();
	}

	public FilterParserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FilterParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilterParserException(String message) {
		super(message);
	}

	public FilterParserException(Throwable cause) {
		super(cause);
	}

}
