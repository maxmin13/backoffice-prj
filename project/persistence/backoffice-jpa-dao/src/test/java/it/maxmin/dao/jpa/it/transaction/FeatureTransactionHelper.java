package it.maxmin.dao.jpa.it.transaction;

import java.util.Optional;

import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class FeatureTransactionHelper {

	public Optional<TransactionIsolation> getTransactionIsolation(String featureDescription) {
		TransactionIsolation transactionIsolation = null;
		for (TransactionIsolation isolation : TransactionIsolation.values()) {
			if (isolation.getDescription().equals(featureDescription)) {
				transactionIsolation = isolation;
				break;
			}
		}
		return Optional.ofNullable(transactionIsolation);
	}

	public Optional<TransactionPropagation> getTransactionPropagation(String featureDescription) {
		TransactionPropagation transactionPropagation = null;
		for (TransactionPropagation propagation : TransactionPropagation.values()) {
			if (propagation.getDescription().equals(featureDescription)) {
				transactionPropagation = propagation;
				break;
			}
		}
		return Optional.ofNullable(transactionPropagation);
	}

}
