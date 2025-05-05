package it.maxmin.dao.jpa.it.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.NOPE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.RESPONSE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.constant.DatabaseExeption;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.it.error.StepErrorManager;

public class CommonStepDefinitions<T extends Exception> {

	private MessageService messageService;
	private FeatureErrorHelper featureErrorHelper;
	private LogScenarioUtil logScenarioUtil;
	private ScenarioItemContext scenarioItemContext;
	private StepErrorManager stepErrorManager;

	@Autowired
	public CommonStepDefinitions(MessageService messageService, FeatureErrorHelper featureErrorHelper,
			LogScenarioUtil logScenarioUtil, ScenarioItemContext scenarioItemContext,
			StepErrorManager stepErrorManager) {
		this.messageService = messageService;
		this.featureErrorHelper = featureErrorHelper;
		this.logScenarioUtil = logScenarioUtil;
		this.scenarioItemContext = scenarioItemContext;
		this.stepErrorManager = stepErrorManager;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Then("I check if a {string} error have been raised")
	public void check_if_specific_error_has_been_raised(String error) {
		assertNotNull(error);
		logScenarioUtil.log("checking if a {0} error has been raised", error);
		DatabaseExeption featureError = featureErrorHelper.getDatabaseError(error).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "feature error")));
		Class expected = featureError.getExceptionClass();
		stepErrorManager.getErrorByType(expected).ifPresentOrElse(er -> {
			scenarioItemContext.addItem(RESPONSE, YES);
			logScenarioUtil.log("a {0} error has been raised", error);
		}, () -> {
			scenarioItemContext.addItem(RESPONSE, NOPE);
			logScenarioUtil.log("no {0} error has been raised", error);
		});
	}

	@And("I check if one of the following errors have been raised")
	public void check_if_one_of_these_error_have_been_raised(DataTable errors) {
		logScenarioUtil.log("checking if one of the described errors has been raised");
		assertNotNull(errors);
		featureErrorHelper.buildErrors(errors).forEach(this::check_if_specific_error_has_been_raised);
	}

	@Then("I check if an error has been raised")
	public void check_if_an_error_has_been_raised() {
		logScenarioUtil.log("checking if an error has been raised");
		int size = stepErrorManager.getUnhandledErrors().size();

		if (size == 0) {
			scenarioItemContext.addItem(RESPONSE, NOPE);
			logScenarioUtil.log("no error was raised");
		}
		else {
			scenarioItemContext.addItem(RESPONSE, YES);
			logScenarioUtil.log("an error has been raised");
		}
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		assertNotNull(expected);
		logScenarioUtil.log("I should be told {0}", expected);
		String response = (String) scenarioItemContext.getItem(RESPONSE).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "response")));
		assertEquals(expected, response);
		logScenarioUtil.log("I have been told {0}", expected);
	}

}
