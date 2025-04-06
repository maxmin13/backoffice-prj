package it.maxmin.dao.jpa.it.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.step.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.transaction.Transaction;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class StepTransactionManager {

	private TransactionManager transactionManager;
	private ScenarioTransactionContext scenarioTransactionContext;
	private MessageService messageService;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public StepTransactionManager(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, MessageService messageService, LogScenarioUtil logScenarioUtil) {
		this.transactionManager = transactionManager;
		this.scenarioTransactionContext = scenarioTransactionContext;
		this.messageService = messageService;
		this.logScenarioUtil = logScenarioUtil;
	}

	public String createTx() {
		Transaction transaction = transactionManager.createTx();
		scenarioTransactionContext.setTransaction(transaction.getId(), transaction);
		logScenarioUtil.log("created transaction {0} with propagation {1} and isolation {2}", transaction.getId(),
				transaction.getPropagationBehaviour(), transaction.getIsolationLevel());
		return transaction.getId();
	}

	public String createTx(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		Transaction transaction = transactionManager.createTx(transactionPropagation, transactionIsolation);
		scenarioTransactionContext.setTransaction(transaction.getId(), transaction);
		logScenarioUtil.log("created transaction {0} with propagation {1} and isolation {2}", transaction.getId(),
				transaction.getPropagationBehaviour(), transaction.getIsolationLevel());
		return transaction.getId();
	}

	public void startTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioTransactionContext.getTransaction(id).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		transactionManager.startTx(transaction);
		logScenarioUtil.log("transaction {0} started", id);
	}

	public void commitTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioTransactionContext.getTransaction(id).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			transactionManager.commitTx(transaction);
		}
		catch (JpaDaoTestException e) {
			scenarioTransactionContext.removeTransaction(id);
			throw e;
		}
		scenarioTransactionContext.removeTransaction(id);
		logScenarioUtil.log("transaction {0} committed", id);
	}

	public void rollbackTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioTransactionContext.getTransaction(id).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			transactionManager.rollbackTx(transaction);
		}
		catch (JpaDaoTestException e) {
			scenarioTransactionContext.removeTransaction(id);
			throw e;
		}
		scenarioTransactionContext.removeTransaction(id);
		logScenarioUtil.log("transaction {0} rolled-back", id);
	}

	public List<Transaction> getPendingTransaction() {
		return scenarioTransactionContext.getTransactions();
	}

}
