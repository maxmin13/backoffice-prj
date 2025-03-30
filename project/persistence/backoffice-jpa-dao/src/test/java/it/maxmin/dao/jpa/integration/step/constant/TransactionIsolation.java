package it.maxmin.dao.jpa.integration.step.constant;

import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_UNCOMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_SERIALIZABLE;

public enum TransactionIsolation {

	READ_UNCOMMITED_ISO("read uncommitted", ISOLATION_READ_UNCOMMITTED),
	READ_COMMITTED_ISO("read committed", ISOLATION_READ_COMMITTED),
	REPEATABLE_READ_ISO("repeatable read", ISOLATION_REPEATABLE_READ),
	SERIALIZABLE("serializable", ISOLATION_SERIALIZABLE);

	private String description;
	private int isolation;

	private TransactionIsolation(String description, int isolation) {
		this.description = description;
		this.isolation = isolation;
	}

	public String getDescription() {
		return description;
	}

	public int getIsolation() {
		return isolation;
	}
}
