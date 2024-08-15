package it.maxmin.dao.hibernate;

public class DaoTestException extends RuntimeException {
	
	private static final long serialVersionUID = -3810044371895076670L;

	public DaoTestException(String msg) {
		super(msg);
	}

	public DaoTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
