package it.maxmin.model.jpa.dao.exception;

public class JpaModelTestException extends RuntimeException {

	private static final long serialVersionUID = -3810044371895076670L;

	public JpaModelTestException(String msg) {
		super(msg);
	}

	public JpaModelTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
