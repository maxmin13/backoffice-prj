package it.maxmin.dao.jpa.it.transaction;

import java.util.Optional;

import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class StepTransactionHelper {

	public Optional<TransactionIsolation> getTransactionIsolation(String description) {
		TransactionIsolation transactionIsolation = null;
		for (TransactionIsolation isolation : TransactionIsolation.values()) {
			if (isolation.getDescription().equals(description)) {
				transactionIsolation = isolation;
				break;
			}
		}
		return Optional.ofNullable(transactionIsolation);
	}

	public Optional<TransactionPropagation> getTransactionPropagation(String description) {
		TransactionPropagation transactionPropagation = null;
		for (TransactionPropagation propagation : TransactionPropagation.values()) {
			if (propagation.getDescription().equals(description)) {
				transactionPropagation = propagation;
				break;
			}
		}
		return Optional.ofNullable(transactionPropagation);
	}

}
