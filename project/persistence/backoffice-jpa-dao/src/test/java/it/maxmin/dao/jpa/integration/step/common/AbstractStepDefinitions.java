package it.maxmin.dao.jpa.integration.step.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.TRANSACTION_STATUS;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.Scenario;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;

public abstract class AbstractStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStepDefinitions.class);

	private MessageService messageService;
	private StepUtil stepUtil;
	private PlatformTransactionManager transactionManager;
	private ScenarioContext scenarioContext;

	@Autowired
	public AbstractStepDefinitions(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService, ScenarioContext scenarioContext) {
		this.messageService = messageService;
		this.stepUtil = stepUtil;
		this.transactionManager = transactionManager;
		this.scenarioContext = scenarioContext;
	}

	public void init(Scenario scenario) {
		scenarioContext.init(scenario);
	}

	public String getScenarioName() {
		return scenarioContext.getScenarioId();
	}

	public void addToScenarioContext(String key, Object object) {
		scenarioContext.addProperty(key, object);
	}

	public Optional<Object> getFromScenarioContext(String key) {
		return scenarioContext.get(key);
	}

	public Collection<Object> getSavedObjects() {
		return scenarioContext.getSavedObjects();
	}

	public StepError getStepError(String description) {
		return stepUtil.getStepError(description)
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
	}

	public TransactionIsolationLevel getTransactionIsolationLevel(String description) {
		return stepUtil.getTransactionIsolationLevel(description).orElseThrow(
				() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void startTx() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setTimeout(300);
		TransactionIsolationLevel transactionIsolationLevel = stepUtil
				.getTransactionIsolationLevel(REPEATABLE_READ_ISO.getDescription()).orElseThrow(
						() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		addToScenarioContext(TRANSACTION_STATUS, txStatus);
	}

	public void commitTx() {
		TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext(TRANSACTION_STATUS).orElseThrow(
				() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
		transactionManager.commit(txStatus);
		log("transaction committed");
	}

	public void rollbackTx() {
		TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext(TRANSACTION_STATUS).orElseThrow(
				() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
		transactionManager.rollback(txStatus);
		log("transaction rolled back");
	}

	public String getMessage(String message, Object... params) {
		return messageService.getMessage(message, params);
	}

	public void log(String message, Object... params) {
		StringBuffer bf = new StringBuffer();
		bf.append("SCE(").append(getScenarioName()).append("): ").append(message);
		LOGGER.debug(MessageFormat.format(bf.toString(), params));
	}

}
