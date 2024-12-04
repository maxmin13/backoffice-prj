package it.maxmin.dao.jdbc.exception;

public class JdbcDaoTestException extends RuntimeException {
	
	private static final long serialVersionUID = -3810044371895076670L;

	public JdbcDaoTestException(String msg) {
		super(msg);
	}

	public JdbcDaoTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
