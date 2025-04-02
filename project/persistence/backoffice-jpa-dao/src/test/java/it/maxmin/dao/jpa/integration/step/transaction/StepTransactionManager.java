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

	private static long identifer = 0;
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
		String id = createID();
		transactionDefinition.setName(id);
		Transaction transaction = Transaction.newInstance().withTransactionDefinition(transactionDefinition);
		scenarioContext.getTrasactions().put(id, transaction);
		logUtil.log("created transaction {0} with propagation {1} and isolation {2}", id,
				REQUIRED_PROPAGATION.getDescription(), REPEATABLE_READ_ISO.getDescription());
		return id;
	}

	public String createTx(TransactionPropagation transactionPropagation, TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		String id = createTx();
		setTransactionPropagation(id, transactionPropagation);
		setTransactionIsolation(id, transactionIsolation);
		logUtil.log("created transaction {0} with propagation {1} and isolation {2}", id,
				transactionPropagation.getDescription(), transactionIsolation.getDescription());
		return id;
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
		logUtil.log("transaction {0} set propagation {1}", id, transactionPropagation.getDescription());
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
		logUtil.log("transaction {0} set isolation {1}", id, transactionIsolation.getDescription());
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
		logUtil.log("transaction {0} started", id);
	}

	public void commitTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		platformTransactionManager.commit(transaction.getTransactionStatus());
		scenarioContext.getTrasactions().remove(id);
		logUtil.log("transaction {0} committed", id);
	}

	public void rollbackTx(String id) {
		assertNotNull(id);
		Transaction transaction = scenarioContext.getTrasactions().get(id);
		if (transaction == null) {
			throw new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction"));
		}
		platformTransactionManager.rollback(transaction.getTransactionStatus());
		scenarioContext.getTrasactions().remove(id);
		logUtil.log("transaction {0} rolled back", id);
	}

	private static synchronized String createID() {
		return String.valueOf(identifer++);
	}
}
