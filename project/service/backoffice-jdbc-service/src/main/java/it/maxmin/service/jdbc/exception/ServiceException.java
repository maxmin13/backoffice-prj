package it.maxmin.service.jdbc.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = -8729318448985461722L;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(String msg, Exception ex) {
		super(msg, ex);
	}

}
