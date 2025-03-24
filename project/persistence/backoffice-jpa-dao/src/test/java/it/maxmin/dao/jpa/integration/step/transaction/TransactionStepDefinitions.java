package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.TRANSACTION;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.TRANSACTION_STATUS;
import static it.maxmin.dao.jpa.integration.step.constant.TransactionIsolationLevel.REPEATABLE_READ_ISO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.common.util.TransactionUtil;
import it.maxmin.dao.jpa.integration.step.constant.TransactionIsolationLevel;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;

public class TransactionStepDefinitions {

	private TransactionUtil transactionUtil;
	private MessageService messageService;
	private ScenarioContext scenarioContext;
	private LogUtil logUtil;

	@Autowired
	public TransactionStepDefinitions(TransactionUtil transactionUtil, MessageService messageService, LogUtil logUtil,
			ScenarioContext scenarioContext) {
		this.transactionUtil = transactionUtil;
		this.messageService = messageService;
		this.scenarioContext = scenarioContext;
		this.logUtil = logUtil;
	}

	@Given("I create a database transaction")
	public void create_database_transaction() {
		logUtil.log("creating a database transaction");
		TransactionDefinition txDefinition = transactionUtil.createTx(scenarioContext.getScenarioName());
		scenarioContext.add(TRANSACTION, txDefinition);
	}

	@And("the transaction isolation level is the default")
	public void set_repeatable_read_transaction_isolation_level() {

		logUtil.log("setting transaction isolation level to {0}", REPEATABLE_READ_ISO.getDescription());

		TransactionDefinition txDefinition = (TransactionDefinition) scenarioContext.get(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		assertEquals(REPEATABLE_READ_ISO.getLevel(), txDefinition.getIsolationLevel());
	}

	@Given("I set the transaction isolation to {string}")
	public void set_transaction_isolation_level(String level) {

		TransactionIsolationLevel transactionIsolationLevel = transactionUtil.getTransactionIsolationLevel(level)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
		logUtil.log("setting transaction isolation level to {0}", transactionIsolationLevel.getDescription());

		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) scenarioContext.get(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));

		txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
	}

	@Given("I start the database transaction")
	public void start_database_transaction() {
		DefaultTransactionDefinition txDefinition = (DefaultTransactionDefinition) scenarioContext.get(TRANSACTION)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction definition")));
		TransactionStatus transactionStatus = transactionUtil.startTx(txDefinition);
		scenarioContext.add(TRANSACTION_STATUS, transactionStatus);
		logUtil.log("database transaction started");
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		logUtil.log("committing the database transaction");
		try {
			TransactionStatus transactionStatus = (TransactionStatus) scenarioContext.get(TRANSACTION_STATUS)
					.orElseThrow(() -> new JpaDaoTestException(
							messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			transactionUtil.commitTx(transactionStatus);
			logUtil.log("transaction committed");
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			scenarioContext.add(EXCEPTION, e);
		}
	}

	@Given("I roll back the database transaction")
	public void rollback_database_transaction() {
		logUtil.log("rolling back the database transaction");
		try {
			TransactionStatus transactionStatus = (TransactionStatus) scenarioContext.get(TRANSACTION_STATUS)
					.orElseThrow(() -> new JpaDaoTestException(
							messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction status")));
			transactionUtil.rollbackTx(transactionStatus);
			logUtil.log("transaction rolled back");
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			scenarioContext.add(EXCEPTION, e);
		}
	}

}
