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
import it.maxmin.dao.jpa.it.step.common.LogUtil;
import it.maxmin.dao.jpa.it.step.common.StepActionManager;
import it.maxmin.dao.jpa.transaction.TransactionIsolation;
import it.maxmin.dao.jpa.transaction.TransactionPropagation;

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

	@Given("I create a default transaction {string}")
	public void create_a_database_transaction(String txName) {
		String id = stepTransactionManager.createTx();
		stepActionManager.setItem(txName, id);
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
		stepActionManager.setItem(txName, id);
	}

	@Given("I start the transaction {string}")
	public void start_database_transaction(String txName) {
		String id = (String) stepActionManager.getItem(txName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
		stepTransactionManager.startTx(id);
	}

	@Then("I commit the transaction {string}")
	public void commit_database_transaction(String txName) {
		try {
			String id = (String) stepActionManager.getItem(txName).orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
			stepTransactionManager.commitTx(id);
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			stepActionManager.setItem(EXCEPTION, e);
		}
	}

	@Given("I rollback the transaction {string}")
	public void rollback_database_transaction(String txName) {
		try {
			String id = (String) stepActionManager.getItem(txName).orElseThrow(() -> new JpaDaoTestException(
					messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction")));
			stepTransactionManager.rollbackTx(id);
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			stepActionManager.setItem(EXCEPTION, e);
		}
	}

}
