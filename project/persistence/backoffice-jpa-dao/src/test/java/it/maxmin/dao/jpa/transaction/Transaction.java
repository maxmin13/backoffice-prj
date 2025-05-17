package it.maxmin.dao.jpa.transaction;

import static it.maxmin.dao.jpa.transaction.TransactionIsolation.READ_COMMITTED_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionIsolation.READ_UNCOMMITED_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionIsolation.SERIALIZABLE;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.MANDATORY_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.NESTED_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.NEVER_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.NOT_SUPPORTED_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRED_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRES_NEW_PROPAGATION;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.SUPPORTS_PROPAGATION;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_COMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_READ_UNCOMMITTED;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_SERIALIZABLE;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_MANDATORY;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NEVER;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NOT_SUPPORTED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_SUPPORTS;

import java.text.MessageFormat;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class Transaction {

	private DefaultTransactionDefinition transactionDefinition;
	private TransactionStatus transactionStatus;

	public static Transaction newInstance() {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		return new Transaction(transactionDefinition);
	}

	public String getId() {
		return transactionDefinition.getName();
	}

	public TransactionPropagation getPropagationBehaviour() {
		int propagation = transactionDefinition.getPropagationBehavior();
		return switch (propagation) {
			case PROPAGATION_REQUIRED -> {
				yield REQUIRED_PROPAGATION;
			}
			case PROPAGATION_SUPPORTS -> {
				yield SUPPORTS_PROPAGATION;
			}
			case PROPAGATION_MANDATORY -> {
				yield MANDATORY_PROPAGATION;
			}
			case PROPAGATION_REQUIRES_NEW -> {
				yield REQUIRES_NEW_PROPAGATION;
			}
			case PROPAGATION_NOT_SUPPORTED -> {
				yield NOT_SUPPORTED_PROPAGATION;
			}
			case PROPAGATION_NEVER -> {
				yield NEVER_PROPAGATION;
			}
			case PROPAGATION_NESTED -> {
				yield NESTED_PROPAGATION;
			}
			default -> {
				throw new IllegalArgumentException(MessageFormat.format("Unexpected value {0}", propagation));
			}
		};
	}

	public TransactionIsolation getIsolationLevel() {
		int isolation = transactionDefinition.getIsolationLevel();
		return switch (isolation) {
			case ISOLATION_READ_UNCOMMITTED -> {
				yield READ_UNCOMMITED_ISO;
			}
			case ISOLATION_READ_COMMITTED -> {
				yield READ_COMMITTED_ISO;
			}
			case ISOLATION_REPEATABLE_READ -> {
				yield REPEATABLE_READ_ISO;
			}
			case ISOLATION_SERIALIZABLE -> {
				yield SERIALIZABLE;
			}
			default -> {
				throw new IllegalArgumentException(MessageFormat.format("Unexpected value {0}", isolation));
			}
		};
	}

	Transaction withId(String name) {
		transactionDefinition.setName(name);
		return this;
	}

	// Set the timeout to apply, as number of seconds.
	Transaction withTimeout(Integer timeout) {
		transactionDefinition.setTimeout(timeout);
		return this;
	}

	Transaction withPropagationBehaviour(TransactionPropagation transactionPropagation) {
		transactionDefinition.setPropagationBehavior(transactionPropagation.getLevel());
		return this;
	}

	Transaction withIsolationLevel(TransactionIsolation transactionIsolation) {
		transactionDefinition.setIsolationLevel(transactionIsolation.getLevel());
		return this;
	}

	TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	Transaction withTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
		return this;
	}

	private Transaction() {
	}

	private Transaction(DefaultTransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

}
