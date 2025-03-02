package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

	public TransactionManagerStepDefinitions(PlatformTransactionManager transactionManager,
			MessageService messageService, StepUtil stepUtil) {
		super(transactionManager, stepUtil, messageService);
	}

	@Given("I create a database transaction")
	public void create_database_transaction() {
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setTimeout(300);
		txDefinition.setName(getScenarioName());

		addToScenarioContext("tx-definition", txDefinition);
		log("creating a database transaction ...");
	}

	@And("the transaction isolation level is the default")
	public void set_repeatable_read_transaction_isolation_level() {

		TransactionIsolationLevel transactionIsolationLevel = getTransactionIsolationLevel(
				REPEATABLE_READ_ISO.getDescription());

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(
				"tx-definition")
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		log("setting transaction isolation level to {0}", transactionIsolationLevel.getDescription());
	}

	@Given("I set the transaction isolation to {string}")
	public void set_transaction_isolation_level(String level) {

		TransactionIsolationLevel transactionIsolationLevel = getTransactionIsolationLevel(level);

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(
				"tx-definition")
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		log("setting transaction isolation level to {0}", transactionIsolationLevel.getDescription());
	}

	@Given("I start the database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(
				"tx-definition")
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));
		TransactionStatus txStatus = getTransactionManager().getTransaction(txDefinition);
		addToScenarioContext("tx-status", txStatus);
		log("starting database transaction ...");
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		log("committing the database transaction ...");
		try {
			TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext("tx-status").orElseThrow(
					() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().commit(txStatus);
			log("transaction committed");
		}
		catch (Exception e) {
			log("{0}", e);
			addToScenarioContext("exception", e);
		}
	}

	@Given("I rollback the database transaction")
	public void rollback_database_transaction() {
		log("rolling back the database transaction ...");
		try {
			TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext("tx-status").orElseThrow(
					() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().rollback(txStatus);
			log("transaction rolled back");
		}
		catch (Exception e) {
			log("{0}", e);
			addToScenarioContext("exception", e);
		}
	}

	@Then("a transaction {string} error should have been raised")
	public void a_error_should_have_been_thrown(String description) {
		log("checking error ...");
		Exception ex = (Exception) getFromScenarioContext("exception")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "exception")));

		StepError stepError = getStepError(description);

		assertEquals(stepError.getExceptionClass(), ex.getClass());
	}

	@Before
	public void initStep(Scenario scenario) {
		init(scenario);
	}

	@After
	public void cleanStep() {
		//
	}

}
