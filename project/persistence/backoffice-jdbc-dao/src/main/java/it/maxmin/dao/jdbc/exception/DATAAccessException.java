package it.maxmin.dao.jdbc.exception;

import org.springframework.dao.DataAccessException;

public class DATAAccessException extends DataAccessException {

	private static final long serialVersionUID = -8729318448985461722L;

	public DATAAccessException(String msg) {
		super(msg);
	}

	public DATAAccessException(String msg, Exception ex) {
		super(msg, ex);
	}

}
