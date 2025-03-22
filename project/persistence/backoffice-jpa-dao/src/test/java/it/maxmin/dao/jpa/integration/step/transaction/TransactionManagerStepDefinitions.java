package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.TRANSACTION;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.TRANSACTION_STATUS;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.AbstractStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel;

public class TransactionManagerStepDefinitions extends AbstractStepDefinitions {

	public TransactionManagerStepDefinitions(PlatformTransactionManager transactionManager,
			MessageService messageService, StepUtil stepUtil, ScenarioContext scenarioContext) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
	}

	@Given("I create a database transaction")
	public void create_database_transaction() {
		log("creating a database transaction");
		DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
		txDefinition.setTimeout(300);
		txDefinition.setName(getScenarioName());
		addToScenarioContext(TRANSACTION, txDefinition);
	}

	@And("the transaction isolation level is the default")
	public void set_repeatable_read_transaction_isolation_level() {

		TransactionIsolationLevel transactionIsolationLevel = getTransactionIsolationLevel(
				REPEATABLE_READ_ISO.getDescription());
		log("setting transaction isolation level to {0}", transactionIsolationLevel.getDescription());

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		addToScenarioContext(TRANSACTION, txDefinition);
	}

	@Given("I set the transaction isolation to {string}")
	public void set_transaction_isolation_level(String level) {

		TransactionIsolationLevel transactionIsolationLevel = getTransactionIsolationLevel(level);
		log("setting transaction isolation level to {0}", transactionIsolationLevel.getDescription());

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
		addToScenarioContext(TRANSACTION, txDefinition);
	}

	@Given("I start the database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) getFromScenarioContext(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));
		TransactionStatus txStatus = getTransactionManager().getTransaction(txDefinition);
		addToScenarioContext(TRANSACTION_STATUS, txStatus);
		log("database transaction started");
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		log("committing the database transaction");
		try {
			TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext(TRANSACTION_STATUS).orElseThrow(
					() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().commit(txStatus);
			log("transaction committed");
		}
		catch (Exception e) {
			log("{0}", e);
			addToScenarioContext(EXCEPTION, e);
		}
	}

	@Given("I roll back the database transaction")
	public void rollback_database_transaction() {
		log("rolling back the database transaction");
		try {
			TransactionStatus txStatus = (TransactionStatus) getFromScenarioContext(TRANSACTION_STATUS).orElseThrow(
					() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			getTransactionManager().rollback(txStatus);
			log("transaction rolled back");
		}
		catch (Exception e) {
			log("{0}", e);
			addToScenarioContext(EXCEPTION, e);
		}
	}

}
