package it.maxmin.dao.jpa.it.step.transaction;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.EXCEPTION;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.step.context.ScenarioActionContext;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

public class TransactionStepDefinitions {

	private StepTransactionManager stepTransactionManager;
	private StepTransactionHelper stepTransactionHelper;
	private ScenarioActionContext scenarioActionContext;
	private MessageService messageService;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public TransactionStepDefinitions(ScenarioActionContext scenarioActionContext,
			StepTransactionManager stepTransactionManager, StepTransactionHelper stepTransactionHelper,
			MessageService messageService, LogScenarioUtil logScenarioUtil) {
		this.scenarioActionContext = scenarioActionContext;
		this.stepTransactionManager = stepTransactionManager;
		this.stepTransactionHelper = stepTransactionHelper;
		this.messageService = messageService;
		this.logScenarioUtil = logScenarioUtil;
	}

	@Given("I create a default transaction {string}")
	public void create_a_database_transaction(String txName) {
		String id = stepTransactionManager.createTx();
		scenarioActionContext.setItem(txName, id);
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
		scenarioActionContext.setItem(txName, id);
	}

	@Given("I start the transaction {string}")
	public void start_database_transaction(String txName) {
		String id = (String) scenarioActionContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		stepTransactionManager.startTx(id);
	}

	@Then("I commit the transaction {string}")
	public void commit_database_transaction(String txName) {
		String id = (String) scenarioActionContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			stepTransactionManager.commitTx(id);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			scenarioActionContext.setItem(EXCEPTION, e.getCause());
		}
	}

	@Given("I rollback the transaction {string}")
	public void rollback_database_transaction(String txName) {
		String id = (String) scenarioActionContext.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		try {
			stepTransactionManager.rollbackTx(id);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			scenarioActionContext.setItem(EXCEPTION, e.getCause());
		}
	}

}
