package it.maxmin.plain.dao.exception;

public class DataAccessException extends org.springframework.dao.DataAccessException {

	private static final long serialVersionUID = -8729318448985461722L;

	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(String msg, Exception ex) {
		super(msg, ex);
	}

}
