package it.maxmin.dao.jpa.it.constant;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import jakarta.persistence.QueryTimeoutException;

public enum StepError {

	SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION("SQL integrity constraint violation", SQLIntegrityConstraintViolationException.class),
	DATA_INTEGRITY_VIOLATION_EXCEPTION("data integrity violation", DataIntegrityViolationException.class),
	QUERY_TIMEOUT_EXCEPTION("query timeout", QueryTimeoutException.class),
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
