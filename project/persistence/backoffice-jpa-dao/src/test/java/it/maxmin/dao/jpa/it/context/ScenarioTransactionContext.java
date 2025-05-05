package it.maxmin.dao.jpa.it.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.maxmin.dao.jpa.transaction.Transaction;

/**
 * {@link ScenarioTransactionContext} has the scope of a scenario. Cucumber
 * creates a new instance of each step class for each scenario.
 */
@Component
@Scope(value = "cucumber-glue", proxyMode = TARGET_CLASS)
public class ScenarioTransactionContext {

	private Map<String, Transaction> transactions;

	public ScenarioTransactionContext() {
		transactions = new HashMap<>();
	}

	public void addTransaction(String name, Transaction transaction) {
		assertNotNull(name);
		assertNotNull(transaction);
		transactions.put(name, transaction);
	}

	public Optional<Transaction> getTransaction(String name) {
		return Optional.ofNullable(transactions.get(name));
	}

	public void removeTransaction(String name) {
		assertNotNull(name);
		transactions.remove(name);
	}

	public List<Transaction> getTransactions() {
		return transactions.values().stream().toList();
	}

}
