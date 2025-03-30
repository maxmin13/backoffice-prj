package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.integration.step.constant.TransactionPropagation.REQUIRED_PROPAGATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.LogUtil;
import it.maxmin.dao.jpa.integration.step.constant.TransactionIsolation;
import it.maxmin.dao.jpa.integration.step.constant.TransactionPropagation;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.context.Transaction;

public class StepTransactionManager {

	private static long id = 0;
	private ScenarioContext scenarioContext;
	private PlatformTransactionManager platformTransactionManager;
	private MessageService messageService;
	private LogUtil logUtil;

	@Autowired
	public StepTransactionManager(ScenarioContext scenarioContext,
			PlatformTransactionManager platformTransactionManager, MessageService messageService, LogUtil logUtil) {
		this.scenarioContext = scenarioContext;
		this.platformTransactionManager = platformTransactionManager;
		this.messageService = messageService;
		this.logUtil = logUtil;
	}

	public String createTx() {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setTimeout(300);
		transactionDefinition.setIsolationLevel(REPEATABLE_READ_ISO.getIsolation());
		transactionDefinition.setPropagationBehavior(REQUIRED_PROPAGATION.getPropagation());
		String identifier = createID();
		transactionDefinition.setName(identifier);
		Transaction transaction = Transaction.newInstance().withTransactionDefinition(transactionDefinition);
		scenarioContext.getTrasactions().put(identifier, transaction);
		return identifier;
	}

	public String createTx(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		String identifier = createTx();
		Transaction transaction = scenarioContext.getTrasactions().get(identifier);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		DefaultTransactionDefinition defaultTransactionDefinition = (DefaultTransactionDefinition) transaction
				.getTransactionDefinition();
		defaultTransactionDefinition.setIsolationLevel(transactionPropagation.getPropagation());
		defaultTransactionDefinition.setIsolationLevel(transactionIsolation.getIsolation());
		return identifier;
	}

	public void setTransactionPropagation(String id, TransactionPropagation transactionPropagation) {
		assertNotNull(id);
		assertNotNull(transactionPropagation);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		DefaultTransactionDefinition defaultTransactionDefinition = (DefaultTransactionDefinition) transaction
				.getTransactionDefinition();
		defaultTransactionDefinition.setPropagationBehavior(transactionPropagation.getPropagation());
	}

	public void setTransactionIsolation(String id, TransactionIsolation transactionIsolation) {
		assertNotNull(id);
		assertNotNull(transactionIsolation);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		DefaultTransactionDefinition defaultTransactionDefinition = (DefaultTransactionDefinition) transaction
				.getTransactionDefinition();
		defaultTransactionDefinition.setIsolationLevel(transactionIsolation.getIsolation());
	}

	public void startTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		TransactionStatus transactionStatus = platformTransactionManager
				.getTransaction(transaction.getTransactionDefinition());
		transaction.withTransactionStatus(transactionStatus);
	}

	// TODO get or remove from contest ??????
	public void commitTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		platformTransactionManager.commit(transaction.getTransactionStatus());
		logUtil.log("transaction committed");
	}

	// TODO get or remove from contest ??????
	public void rollbackTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		platformTransactionManager.rollback(transaction.getTransactionStatus());
		logUtil.log("transaction rolled back");
	}

	private static synchronized String createID() {
		return String.valueOf(id++);
	}
}
