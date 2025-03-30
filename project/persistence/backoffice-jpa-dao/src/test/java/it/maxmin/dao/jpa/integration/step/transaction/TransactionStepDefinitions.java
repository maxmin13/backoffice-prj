package it.maxmin.dao.jpa.integration.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.TRANSACTION;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.LogUtil;
import it.maxmin.dao.jpa.integration.step.common.StepActionManager;
import it.maxmin.dao.jpa.integration.step.constant.TransactionIsolation;
import it.maxmin.dao.jpa.integration.step.constant.TransactionPropagation;

public class TransactionStepDefinitions {

	private StepTransactionManager stepTransactionManager;
	private StepTransactionHelper stepTransactionHelper;
	private StepActionManager stepActionManager;
	private MessageService messageService;
	private LogUtil logUtil;

	@Autowired
	public TransactionStepDefinitions(StepActionManager stepActionManager,
			StepTransactionManager stepTransactionManager, StepTransactionHelper stepTransactionHelper,
			MessageService messageService, LogUtil logUtil) {
		this.stepTransactionManager = stepTransactionManager;
		this.stepActionManager = stepActionManager;
		this.stepTransactionHelper = stepTransactionHelper;
		this.messageService = messageService;
		this.logUtil = logUtil;
	}

	@Given("I create a default database transaction")
	public void create_a_database_transaction() {
		logUtil.log("creating a database transaction");
		String id = stepTransactionManager.createTx();
		stepActionManager.setItem(TRANSACTION, id);
	}

	@Given("I set the transaction isolation to {string}")
	public void set_transaction_isolation(String isolation) {
		logUtil.log("setting transaction isolation to {0}", isolation);
		TransactionIsolation transactionIsolation = stepTransactionHelper.getTransactionIsolation(isolation)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction isolation")));
		String id = (String) stepActionManager.getItem(TRANSACTION).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		logUtil.log("found transaction {0}", id);
		stepTransactionManager.setTransactionIsolation(id, transactionIsolation);
	}

	@Given("I set the transaction propagation to {string}")
	public void set_transaction_propagation(String propagation) {
		logUtil.log("setting transaction propagation to {0}", propagation);
		TransactionPropagation transactionPropagation = stepTransactionHelper.getTransactionPropagation(propagation)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction propagation")));
		String id = (String) stepActionManager.getItem(TRANSACTION).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		logUtil.log("found transaction {0}", id);
		stepTransactionManager.setTransactionPropagation(id, transactionPropagation);
	}

	@Given("I start the database transaction")
	public void start_database_transaction() {
		logUtil.log("starting the database transaction");
		String id = (String) stepActionManager.getItem(TRANSACTION).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		logUtil.log("found transaction {0}", id);
		stepTransactionManager.startTx(id);
		logUtil.log("database transaction started");
	}

	@Then("I commit the database transaction")
	public void commit_database_transaction() {
		logUtil.log("committing the database transaction");
		try {
			String id = (String) stepActionManager.getItem(TRANSACTION).orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
			logUtil.log("found transaction {0}", id);
			stepTransactionManager.commitTx(id);
			logUtil.log("transaction committed");
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			stepActionManager.setItem(EXCEPTION, e);
		}
	}

	@Given("I rollback the database transaction")
	public void rollback_database_transaction() {
		logUtil.log("rolling back the database transaction");
		try {
			String id = (String) stepActionManager.getItem(TRANSACTION).orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
			logUtil.log("found transaction {0}", id);
			stepTransactionManager.rollbackTx(id);
			logUtil.log("transaction rolled back");
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			stepActionManager.setItem(EXCEPTION, e);
		}
	}

}
