package it.maxmin.dao.jpa.integration.step.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;

import java.text.MessageFormat;
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

public abstract class BaseStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseStepDefinitions.class);

	private MessageService messageService;
	private StepContext stepContext;
	private StepUtil stepUtil;
	private PlatformTransactionManager transactionManager;

	@Autowired
	public BaseStepDefinitions(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService) {
		this.messageService = messageService;
		this.stepUtil = stepUtil;
		this.transactionManager = transactionManager;
		stepContext = new StepContext();
	}

	public void init(Scenario scenario) {
		stepContext.init(scenario);
	}

	public String getScenarioId() {
		return stepContext.getScenarioId();
	}

	public String getScenarioName() {
		return stepContext.getScenarioName();
	}

	public void addToScenarioContext(String key, Object object) {
		stepContext.addProperty(key, object);
	}

	public Optional<Object> getFromScenarioContext(String key) {
		return stepContext.get(key);
	}

	public void removeFromScenarioContext(String key) {
		stepContext.removeProperty(key);
	}

	public void startTx() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setTimeout(300);
		TransactionIsolationLevel transactionIsolationLevel = stepUtil
				.getTransactionIsolationLevel(REPEATABLE_READ_ISO.getDescription()).orElseThrow(
						() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);
		stepContext.addProperty("transaction", txStatus);
	}

	public void commitTx() {
		TransactionStatus txStatus = (TransactionStatus) stepContext.get("transaction")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		transactionManager.commit(txStatus);
	}

	public void rollbackTx() {
		TransactionStatus txStatus = (TransactionStatus) stepContext.get("transaction")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		transactionManager.rollback(txStatus);
	}

	public String getMessage(String message, Object... params) {
		return messageService.getMessage(message, params);
	}

	public StepError getStepError(String description) {
		return stepUtil.getStepError(description)
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
	}

	public TransactionIsolationLevel getTransactionIsolationLevel(String description) {
		return stepUtil.getTransactionIsolationLevel(description).orElseThrow(
				() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
	}

	public void log(String message, Object... params) {
		StringBuffer bf = new StringBuffer();
		bf.append("SCE(").append(getScenarioId()).append("): ").append(message);
		LOGGER.debug(MessageFormat.format(bf.toString(), params));
	}

	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

}
