package it.maxmin.dao.jpa.it.transaction;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.cucumber.datatable.DataTable;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

@Component
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

	public FeatureTransaction buildTransaction(DataTable transaction) {
		assertNotNull(transaction);
		List<List<String>> data = transaction.asLists();
		String txName = data.get(0).get(0);
		String txIsolation = data.get(0).get(1);
		String txPropagation = data.get(0).get(2);
		return FeatureTransaction.newInstance().withName(txName).withIsolation(txIsolation)
				.withPropagation(txPropagation);
	}
}
