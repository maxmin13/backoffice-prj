package it.maxmin.dao.jpa.step.common;

import org.springframework.dao.DataIntegrityViolationException;

public enum StepError {

	DATA_INTEGRITY_VIOLATION("data integrity violation", DataIntegrityViolationException.class);
	
	private String decription;
	private Class<?> clazz;
	
	private StepError(String description, Class<?> clazz) {
		this.decription = description;
		this.clazz = clazz;
	}
	
	public String getDescription() {
		return decription;
	}
	
	public Class<?> getExceptionClass() {
		return clazz;
	}
}
