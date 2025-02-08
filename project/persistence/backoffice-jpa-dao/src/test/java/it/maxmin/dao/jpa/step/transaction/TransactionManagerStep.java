package it.maxmin.dao.jpa.step.transaction;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.dao.jpa.step.StepContext;

public class TransactionManagerStep {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionManagerStep.class);

	private PlatformTransactionManager transactionManager;
	private StepContext stepContext;
	private TransactionStatus status;

	@Autowired
	public TransactionManagerStep(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Given("I start a database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		definition.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		definition.setTimeout(300);
		definition.setName("transaction - " + stepContext.getScenarioName());
		status = transactionManager.getTransaction(definition);
		LOGGER.debug(MessageFormat.format("{0}: starting a database transaction, transaction {1}", stepContext.getScenarioId(),
				status.getTransactionName()));
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		LOGGER.debug(MessageFormat.format("{0}: committing the database transaction {1}", stepContext.getScenarioId(),
				status.getTransactionName()));
		transactionManager.commit(status);
	}

	@Given("I rollback the database transaction")
	public void rollback_database_transaction() {
		LOGGER.debug(MessageFormat.format("{0}: rollbacking the database transaction {1}", stepContext.getScenarioId(),
				status.getTransactionName()));
		transactionManager.rollback(status);
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario);
	}

	@After
	public void clear() {
		//
	}
}
