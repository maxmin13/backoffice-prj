package it.maxmin.dao.jpa.it.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.COMMIT_ERROR;
import static it.maxmin.dao.jpa.it.constant.StepConstants.ROLLBACK_ERROR;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.it.context.StepErrorManager;
import it.maxmin.dao.jpa.it.context.StepTransactionManager;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class TransactionStepDefinitions {

	private StepTransactionManager stepTransactionManager;
	private FeatureTransactionHelper stepTransactionHelper;
	private StepErrorManager stepErrorManager;
	private ScenarioItemContext scenarioItemContext;
	private MessageService messageService;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public TransactionStepDefinitions(ScenarioItemContext scenarioItemContext,
			StepTransactionManager stepTransactionManager, StepErrorManager stepErrorManager,
			FeatureTransactionHelper stepTransactionHelper, MessageService messageService,
			LogScenarioUtil logScenarioUtil) {
		this.scenarioItemContext = scenarioItemContext;
		this.stepTransactionManager = stepTransactionManager;
		this.stepErrorManager = stepErrorManager;
		this.stepTransactionHelper = stepTransactionHelper;
		this.messageService = messageService;
		this.logScenarioUtil = logScenarioUtil;
	}

	@Given("I create a default transaction {string}")
	public void create_a_database_transaction(String txName) {
		String id = stepTransactionManager.createTx();
		scenarioItemContext.setItem(txName, id);
	}

	@Given("I create a database transaction")
	public void create_a_database_transaction(DataTable transaction) {

		List<List<String>> data = transaction.asLists();
		String txName = data.get(0).get(0);
		String txIsolation = data.get(0).get(1);
		String txPropagation = data.get(0).get(2);

		TransactionIsolation transactionIsolation = stepTransactionHelper.getTransactionIsolation(txIsolation)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction isolation")));
		TransactionPropagation transactionPropagation = stepTransactionHelper.getTransactionPropagation(txPropagation)
				.orElseThrow(() -> new JpaDaoTestException(
						messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction propagation")));

		String id = stepTransactionManager.createTx(transactionPropagation, transactionIsolation);
		scenarioItemContext.setItem(txName, id);
	}

	@Given("I start the transaction {string}")
	public void start_database_transaction(String txName) {
		String id = (String) scenarioItemContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		stepTransactionManager.startTx(id);
	}

	@Then("I commit the transaction {string}")
	public void commit_database_transaction(String txName) {
		String id = (String) scenarioItemContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			stepTransactionManager.commitTx(id);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError(COMMIT_ERROR, e);
		}
	}

	@Given("I rollback the transaction {string}")
	public void rollback_database_transaction(String txName) {
		String id = (String) scenarioItemContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			stepTransactionManager.rollbackTx(id);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError(ROLLBACK_ERROR, e);
		}
	}

	@Given("I rollback all pending transactions")
	public void rollback_database_pending_transactions() {
		stepTransactionManager.getPendingTransaction().forEach(tx -> {
			logScenarioUtil.log("Rolling back transaction {0}", tx.getId());
			rollback_database_transaction(tx.getId());
		});
	}

}
