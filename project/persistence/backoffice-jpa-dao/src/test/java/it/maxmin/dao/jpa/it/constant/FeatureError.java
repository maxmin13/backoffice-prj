package it.maxmin.dao.jpa.it.constant;

import java.sql.SQLIntegrityConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;

import jakarta.persistence.QueryTimeoutException;

public enum FeatureError {

	SQL_INTEGRITY_CONSTRAINT_VIOLATION_EXCEPTION("SQL integrity constraint violation",
			SQLIntegrityConstraintViolationException.class),
	DATA_INTEGRITY_VIOLATION_EXCEPTION("data integrity violation", DataIntegrityViolationException.class),
	QUERY_TIMEOUT_EXCEPTION("query timeout", QueryTimeoutException.class),
	JPA_SYSTEM_EXEPTION("JPA system", JpaSystemException.class);

	private String decription;
	private Class<? extends Exception> clazz;

	private FeatureError(String description, Class<? extends Exception> clazz) {
		this.decription = description;
		this.clazz = clazz;
	}

	public String getDescription() {
		return decription;
	}

	public Class<? extends Exception> getExceptionClass() {
		return clazz;
	}
}
