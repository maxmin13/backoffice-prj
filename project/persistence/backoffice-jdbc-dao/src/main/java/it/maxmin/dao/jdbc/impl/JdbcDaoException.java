package it.maxmin.dao.jdbc.impl;

public class JdbcDaoException extends RuntimeException  {

	private static final long serialVersionUID = -2038321331219503965L;

	public JdbcDaoException(String message, Throwable cause) {
		super(message, cause);
	}
}