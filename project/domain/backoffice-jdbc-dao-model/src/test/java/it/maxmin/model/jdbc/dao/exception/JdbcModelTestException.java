package it.maxmin.model.jdbc.dao.exception;

public class JdbcModelTestException extends RuntimeException {

	private static final long serialVersionUID = -3810044371895076670L;

	public JdbcModelTestException(String msg) {
		super(msg);
	}

	public JdbcModelTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
