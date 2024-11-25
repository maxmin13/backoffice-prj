package it.maxmin.model.jdbc.dao;

public class ModelTestException extends RuntimeException {

	private static final long serialVersionUID = -3810044371895076670L;

	public ModelTestException(String msg) {
		super(msg);
	}

	public ModelTestException(String msg, Exception ex) {
		super(msg, ex);
	}
}
