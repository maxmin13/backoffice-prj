package it.maxmin.dao.jpa.it.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.COMMIT_ERROR;
import static it.maxmin.dao.jpa.it.constant.StepConstants.ROLLBACK_ERROR;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioTransactionContext;
import it.maxmin.dao.jpa.it.error.StepErrorManager;
import it.maxmin.dao.jpa.transaction.Transaction;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class TransactionStepDefinitions {

	private TransactionManager transactionManager;
	private ScenarioTransactionContext scenarioTransactionContext;
	private FeatureTransactionHelper featureTransactionHelper;
	private StepErrorManager stepErrorManager;
	private MessageService messageService;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public TransactionStepDefinitions(TransactionManager transactionManager,
			ScenarioTransactionContext scenarioTransactionContext, StepErrorManager stepErrorManager,
			FeatureTransactionHelper featureTransactionHelper, MessageService messageService,
			LogScenarioUtil logScenarioUtil) {
		this.scenarioTransactionContext = scenarioTransactionContext;
		this.transactionManager = transactionManager;
		this.stepErrorManager = stepErrorManager;
		this.featureTransactionHelper = featureTransactionHelper;
		this.messageService = messageService;
		this.logScenarioUtil = logScenarioUtil;
	}

	@Given("I create a default transaction {string}")
	public void create_a_database_transaction(String txName) {
		Transaction transaction = transactionManager.createTx();
		scenarioTransactionContext.addTransaction(txName, transaction);
	}

	@Given("I create a database transaction")
	public void create_a_database_transaction(DataTable transaction) {

		FeatureTransaction data = featureTransactionHelper.buildTransaction(transaction);
		String txName = data.getName();
		String txIsolation = data.getIsolation();
		String txPropagation = data.getPropagation();

		TransactionIsolation transactionIsolation = featureTransactionHelper.getTransactionIsolation(txIsolation)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction isolation")));
		TransactionPropagation transactionPropagation = featureTransactionHelper
				.getTransactionPropagation(txPropagation).orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction propagation")));

		Transaction tx = transactionManager.createTx(transactionPropagation, transactionIsolation);
		scenarioTransactionContext.addTransaction(txName, tx);
	}

	@Given("I start the transaction {string}")
	public void start_database_transaction(String txName) {
		Transaction tx = (Transaction) scenarioTransactionContext.getTransaction(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		transactionManager.startTx(tx);
	}

	@Then("I commit the transaction {string}")
	public void commit_database_transaction(String txName) {
		Transaction tx = (Transaction) scenarioTransactionContext.getTransaction(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		scenarioTransactionContext.removeTransaction(txName);
		try {
			transactionManager.commitTx(tx);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError(COMMIT_ERROR, e);
		}
	}

	@Given("I rollback the transaction {string}")
	public void rollback_database_transaction(String txName) {
		Transaction tx = (Transaction) scenarioTransactionContext.getTransaction(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		scenarioTransactionContext.removeTransaction(txName);
		try {
			transactionManager.rollbackTx(tx);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError(ROLLBACK_ERROR, e);
		}
	}

	@Given("I rollback all pending transactions")
	public void rollback_database_pending_transactions() {
		scenarioTransactionContext.getTransactions().forEach(tx -> {
			logScenarioUtil.log("Rolling back transaction {0}", tx.getId());
			rollback_database_transaction(tx.getId());
		});
	}

}
