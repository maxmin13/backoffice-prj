package it.maxmin.dao.jpa.it.context;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.transaction.Transaction;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class StepTransactionManager {

	private TransactionManager transactionManager;
	private ScenarioTransactionContext scenarioTransactionContext;
	private MessageService messageService;

	@Autowired
	public StepTransactionManager(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, MessageService messageService) {
		this.transactionManager = transactionManager;
		this.scenarioTransactionContext = scenarioTransactionContext;
		this.messageService = messageService;
	}

	public String createTx() {
		Transaction transaction = transactionManager.createTx();
		scenarioTransactionContext.setTransaction(transaction.getId(), transaction);
		return transaction.getId();
	}

	public String createTx(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		Transaction transaction = transactionManager.createTx(transactionPropagation, transactionIsolation);
		scenarioTransactionContext.setTransaction(transaction.getId(), transaction);
		return transaction.getId();
	}

	public void startTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioTransactionContext.getTransaction(id).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		transactionManager.startTx(transaction);
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
	}

	public List<Transaction> getPendingTransaction() {
		return scenarioTransactionContext.getTransactions();
	}

}
