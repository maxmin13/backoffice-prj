package it.maxmin.dao.jpa;

public class JpaDaoTestException extends RuntimeException {
	
	private static final long serialVersionUID = -3810044371895076670L;

	public JpaDaoTestException(String msg) {
		super(msg);
	}

	public JpaDaoTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
