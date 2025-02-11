package it.maxmin.dao.jpa.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.transaction.TransactionDefinition.ISOLATION_REPEATABLE_READ;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.StepContext;

public class TransactionManagerStep {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerStep.class);

	private MessageService messageService;
	private PlatformTransactionManager transactionManager;
	private StepContext stepContext;
	private static final Map<String, Class<? extends Exception>> ERRORS = new HashMap<>();

	@Autowired
	public TransactionManagerStep(PlatformTransactionManager transactionManager, MessageService messageService) {
		this.transactionManager = transactionManager;
		this.messageService = messageService;
		ERRORS.put("data integrity violation", DataIntegrityViolationException.class);
	}

	@Given("I start a database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(ISOLATION_REPEATABLE_READ);
		definition.setTimeout(300);
		definition.setName(stepContext.getScenarioName());
		TransactionStatus txStatus = transactionManager.getTransaction(definition);
		stepContext.addProperty("tx-status", txStatus);
		LOGGER.debug(MessageFormat.format("{0}: starting a database transaction ...", stepContext.getScenarioId()));
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		LOGGER.debug(MessageFormat.format("{0}: committing the database transaction ...", stepContext.getScenarioId()));
		try {
			stepContext.getProperty("tx-status").orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			TransactionStatus txStatus = (TransactionStatus) stepContext.getProperty("tx-status").get();
			transactionManager.commit(txStatus);
		}
		catch (Exception e) {
			LOGGER.error("error", e);
			stepContext.addProperty("error", e);
		}
	}

	@Given("I rollback the database transaction")
	public void rollback_database_transaction() {
		LOGGER.debug(
				MessageFormat.format("{0}: rollbacking the database transaction ...", stepContext.getScenarioId()));
		try {
			stepContext.getProperty("tx-status").orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			TransactionStatus txStatus = (TransactionStatus) stepContext.getProperty("tx-status").get();
			transactionManager.rollback(txStatus);
		}
		catch (Exception e) {
			LOGGER.error("error", e);
			stepContext.addProperty("error", e);
		}
	}

	@Then("a {string} error should have been raised")
	public void a_error_should_have_been_thrown(String error) {
	    Class<? extends Exception> expectedError = ERRORS.get(error);
	    Exception ex = (Exception) stepContext.getProperty("error").orElseThrow(
	    		() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "error")));
	    
	    assertEquals(expectedError, ex.getClass());
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario);
	}

}
