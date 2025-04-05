package it.maxmin.dao.jpa.it.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.common.LogUtil;
import it.maxmin.dao.jpa.it.step.context.ScenarioContext;
import it.maxmin.dao.jpa.transaction.Transaction;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class StepTransactionManager {

	private TransactionManager transactionManager;
	private ScenarioContext scenarioContext;
	private MessageService messageService;
	private LogUtil logUtil;

	@Autowired
	public StepTransactionManager(TransactionManager transactionManager, ScenarioContext scenarioContext,
			MessageService messageService, LogUtil logUtil) {
		this.transactionManager = transactionManager;
		this.scenarioContext = scenarioContext;
		this.messageService = messageService;
		this.logUtil = logUtil;
	}

	public String createTx() {
		Transaction transaction = transactionManager.createTx();
		scenarioContext.getTrasactions().put(transaction.getId(), transaction);
		logUtil.log("created transaction {0} with propagation {1} and isolation {2}", transaction.getId(),
				transaction.getPropagationBehaviour(), transaction.getIsolationLevel());
		return transaction.getId();
	}

	public String createTx(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		Transaction transaction = transactionManager.createTx(transactionPropagation, transactionIsolation);
		scenarioContext.getTrasactions().put(transaction.getId(), transaction);
		logUtil.log("created transaction {0} with propagation {1} and isolation {2}", transaction.getId(),
				transaction.getPropagationBehaviour(), transaction.getIsolationLevel());
		return transaction.getId();
	}
	
	public void startTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		transactionManager.startTx(transaction);
		logUtil.log("transaction {0} started", id);
	}

	public void commitTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		transactionManager.commitTx(transaction);
		scenarioContext.getTrasactions().remove(id);
		logUtil.log("transaction {0} committed", id);
	}

	public void rollbackTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		transactionManager.rollbackTx(transaction);
		scenarioContext.getTrasactions().remove(id);
		logUtil.log("transaction {0} rolled back", id);
	}

	public List<Transaction> getPendingTransaction() {
		return scenarioContext.getTrasactions().values().stream().toList();
	}

}
