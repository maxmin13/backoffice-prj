package it.maxmin.dao.jpa.it.common.hook;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.transaction.TransactionManager;

public class CleanPendingTransactionsHook {

	private TransactionManager transactionManager;
	private ScenarioTransactionContext scenarioTransactionContext;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public CleanPendingTransactionsHook(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, LogScenarioUtil logScenarioUtil) {
		this.transactionManager = transactionManager;
		this.scenarioTransactionContext = scenarioTransactionContext;
		this.logScenarioUtil = logScenarioUtil;
	}

	@After(order = 1000)
	public void clean(Scenario scenario) {
		scenarioTransactionContext.getTransactions().forEach(transaction -> {
			logScenarioUtil.error("Found pending transaction {0}", transaction.getId());
			transactionManager.rollbackTx(transaction);
		});
	}

}
