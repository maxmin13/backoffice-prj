package it.maxmin.dao.jpa.transaction;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;

public enum TransactionPropagation {

	REQUIRED_PROPAGATION("required", PROPAGATION_REQUIRED),
	SUPPORTS_PROPAGATION("supports", PROPAGATION_SUPPORTS),
	MANDATORY_PROPAGATION("mandatory", PROPAGATION_MANDATORY),
	REQUIRES_NEW_PROPAGATION("requires new", PROPAGATION_REQUIRES_NEW),
	NOT_SUPPORTED_PROPAGATION("not supported", PROPAGATION_NOT_SUPPORTED),
	NEVER_PROPAGATION("never", PROPAGATION_NEVER),
	NESTED_PROPAGATION("nested", PROPAGATION_NESTED);

	private String description;
	private int level;

	private TransactionPropagation(String description, int level) {
		this.description = description;
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public int getLevel() {
		return level;
	}
}
