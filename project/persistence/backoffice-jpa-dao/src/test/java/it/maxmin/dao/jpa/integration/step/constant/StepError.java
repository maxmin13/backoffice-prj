package it.maxmin.dao.jpa.integration.step.constant;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

public enum StepError {

	CONSTRAINT_VALIDATION_EXCEPTION("constraint validation violation", SQLIntegrityConstraintViolationException.class),
	SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION("SQL integrity constraint violation", SQLIntegrityConstraintViolationException.class),
	DATA_INTEGRITY_VIOLATION("data integrity violation", DataIntegrityViolationException.class),
	JPA_SYSTEM_EXEPTION("JPA system", JpaSystemException.class);
	
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
