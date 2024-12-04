package it.maxmin.service.jdbc.exception;

public class JdbcServiceTestException extends RuntimeException {

	private static final long serialVersionUID = -3810044371895076670L;

	public JdbcServiceTestException(String msg) {
		super(msg);
	}

	public JdbcServiceTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
