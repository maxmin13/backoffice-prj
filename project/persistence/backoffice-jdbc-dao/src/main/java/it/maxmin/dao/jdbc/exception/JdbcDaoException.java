package it.maxmin.dao.jdbc.exception;

public class JdbcDaoException extends RuntimeException  {

	private static final long serialVersionUID = -2038321331219503965L;

	public JdbcDaoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public JdbcDaoException(String msg) {
		super(msg);
	}
}