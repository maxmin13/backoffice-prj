package it.maxmin.dao.jpa.exception;

import org.springframework.dao.DataAccessException;

public class JpaDaoException extends DataAccessException {

	private static final long serialVersionUID = -8729318448985461722L;

	public JpaDaoException(String msg) {
		super(msg);
	}

	public JpaDaoException(String msg, Exception ex) {
		super(msg, ex);
	}

}
