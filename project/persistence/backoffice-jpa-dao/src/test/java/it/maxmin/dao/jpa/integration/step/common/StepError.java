package it.maxmin.dao.jpa.integration.step.common;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

public enum StepError {

	DATA_INTEGRITY_VIOLATION("data integrity violation", DataIntegrityViolationException.class),
	JPA_SYSTEM_EXEPTION("JPA system exception", JpaSystemException.class);
	
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
