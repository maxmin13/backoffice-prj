package it.maxmin.service.jdbc.exception;

public class JdbcServiceException extends RuntimeException  {

	private static final long serialVersionUID = -2038321331219503965L;

	public JdbcServiceException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JdbcServiceException(String message) {
		super(message);
	}
}