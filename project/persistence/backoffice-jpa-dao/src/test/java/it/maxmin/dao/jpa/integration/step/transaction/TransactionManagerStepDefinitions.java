package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.BaseStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.StepError;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel;

public class TransactionManagerStepDefinitions extends BaseStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerStepDefinitions.class);

	public TransactionManagerStepDefinitions(PlatformTransactionManager transactionManager,
			MessageService messageService, StepUtil stepUtil) {
		super(transactionManager, stepUtil, messageService);
	}

	@Given("I create a database transaction")
	public void create_database_transaction() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setTimeout(300);
		txDefinition.setName(getStepContext().getScenarioName());

		getStepContext().addProperty("tx-definition", txDefinition);
		LOGGER.debug(
				MessageFormat.format("SCE({0}): creating a database transaction", getStepContext().getScenarioId()));
	}

	@And("the transaction isolation level is the default")
	public void set_repeatable_read_transaction_isolation_level() {

		TransactionIsolationLevel transactionIsolationLevel = getStepUtil()
				.getTransactionIsolationLevel(REPEATABLE_READ_ISO.getDescription())
				.orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getStepContext()
				.get("tx-definition").orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		LOGGER.debug(MessageFormat.format("SCE({0}): setting transaction isolation level to {1}",
				getStepContext().getScenarioId(), transactionIsolationLevel.getDescription()));
	}

	@Given("I set the transaction isolation to {string}")
	public void set_transaction_isolation_level(String level) {

		TransactionIsolationLevel transactionIsolationLevel = getStepUtil().getTransactionIsolationLevel(level)
				.orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getStepContext()
				.get("tx-definition").orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		LOGGER.debug(MessageFormat.format("SCE({0}): setting transaction isolation level to {1}",
				getStepContext().getScenarioId(), transactionIsolationLevel.getDescription()));
	}

	@Given("I start the database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getStepContext()
				.get("tx-definition").orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));
		TransactionStatus txStatus = getTransactionManager().getTransaction(txDefinition);
		getStepContext().addProperty("tx-status", txStatus);
		LOGGER.debug(
				MessageFormat.format("SCE({0}): starting database transaction ...", getStepContext().getScenarioId()));
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		LOGGER.debug(MessageFormat.format("scenario({0}): committing the database transaction ...",
				getStepContext().getScenarioId()));
		try {
			TransactionStatus txStatus = (TransactionStatus) getStepContext().get("tx-status")
					.orElseThrow(() -> new JpaDaoTestException(
							getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().commit(txStatus);
			LOGGER.debug(MessageFormat.format("SCE({0}): transaction committed", getStepContext().getScenarioId()));
		}
		catch (Exception e) {
			LOGGER.debug(MessageFormat.format("SCE({0})", e));
			getStepContext().addProperty("exception", e);
		}
	}

	@Given("I rollback the database transaction")
	public void rollback_database_transaction() {
		LOGGER.debug(MessageFormat.format("SCE({0}): rolling back the database transaction ...",
				getStepContext().getScenarioId()));
		try {
			TransactionStatus txStatus = (TransactionStatus) getStepContext().get("tx-status")
					.orElseThrow(() -> new JpaDaoTestException(
							getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().rollback(txStatus);
			LOGGER.debug(MessageFormat.format("SCE({0}): transaction rolled back", getStepContext().getScenarioId()));
		}
		catch (Exception e) {
			LOGGER.debug(MessageFormat.format("SCE({0})", e));
			getStepContext().addProperty("exception", e);
		}
	}

	@Then("a database {string} error should have been raised")
	public void a_error_should_have_been_thrown(String description) {
		LOGGER.debug(MessageFormat.format("SCE({0}): checking error ...", getStepContext().getScenarioId()));
		Exception ex = (Exception) getStepContext().get("exception").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "exception")));

		StepError stepError = getStepUtil().getStepError(description).orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));

		assertEquals(stepError.getExceptionClass(), ex.getClass());
	}

	@Before
	public void initStep(Scenario scenario) {
		getStepContext().init(scenario);
		LOGGER.debug(MessageFormat.format("SCE({0}): creating step context ...", getStepContext().getScenarioId()));
	}

	@After
	public void cleanStep() {
		LOGGER.debug(MessageFormat.format("SCE({0}): cleaning step context ...", getStepContext().getScenarioId()));
	}

}
