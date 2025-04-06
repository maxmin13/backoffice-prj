package it.maxmin.dao.jpa.it.step.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.NOPE;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.RESPONSE;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.constant.StepError;
import it.maxmin.dao.jpa.it.step.context.ScenarioActionContext;

public class CommonStepDefinitions {

	private MessageService messageService;
	private StepErrorHelper stepErrorHelper;
	private LogScenarioUtil logScenarioUtil;
	private ScenarioActionContext scenarioActionContext;

	@Autowired
	public CommonStepDefinitions(MessageService messageService, StepErrorHelper stepErrorHelper, LogScenarioUtil logScenarioUtil,
			ScenarioActionContext scenarioActionContext) {
		this.messageService = messageService;
		this.stepErrorHelper = stepErrorHelper;
		this.logScenarioUtil = logScenarioUtil;
		this.scenarioActionContext = scenarioActionContext;
	}

	@Then("I check if a {string} error have been raised")
	public void check_if_specific_error_have_been_raised(String description) {
		assertNotNull(description);
		logScenarioUtil.log("checking if a {0} error has been raised", description);
		StepError stepError = stepErrorHelper.getStepError(description).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
		Class<?> expected = stepError.getExceptionClass();
		scenarioActionContext.getItem(EXCEPTION).filter(ob -> ob.getClass().equals(expected)).ifPresentOrElse(ex -> {
			scenarioActionContext.setItem(RESPONSE, YES);
			logScenarioUtil.log("a {0} error has been raised", description);
		}, () -> {
			scenarioActionContext.setItem(RESPONSE, NOPE);

			logScenarioUtil.log("no {0} error has been raised", description);
		});
	}

	@And("I check if one of the following errors have been raised")
	public void check_if_one_of_these_error_have_been_raised(DataTable errors) {
		logScenarioUtil.log("checking if one of the described errors has been raised");
		assertNotNull(errors);
		scenarioActionContext.getItem(EXCEPTION).ifPresentOrElse(trownEx -> {
			errors.asLists().get(0).stream().map(errorDescription -> {
				StepError stepError = stepErrorHelper.getStepError(errorDescription)
						.orElseThrow(() -> new JpaDaoTestException(
								messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
				return stepError.getClass();
			}).filter(trownEx.getClass()::equals).findAny().ifPresentOrElse(ex -> {
				scenarioActionContext.setItem(RESPONSE, YES);
				logScenarioUtil.log("a {0} error has been raised", ex.getName());
			}, () -> {
				scenarioActionContext.setItem(RESPONSE, NOPE);
				logScenarioUtil.log("none of the described errors has been raised");
			});
		}, () -> {
			scenarioActionContext.setItem(RESPONSE, NOPE);
			logScenarioUtil.log("no error has been raised");
		});
	}

	@Then("I verify if the {string} action was successful")
	public void check_action_success(String action) {
		logScenarioUtil.log("checking {0} success", action);
		scenarioActionContext.getItem(EXCEPTION).ifPresentOrElse(u -> {
			scenarioActionContext.setItem(RESPONSE, NOPE);
			logScenarioUtil.log("action {0} wasn''t successfull", action);
		}, () -> {
			scenarioActionContext.setItem(RESPONSE, YES);
			logScenarioUtil.log("action {0} was successfull", action);
		});
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		assertNotNull(expected);
		logScenarioUtil.log("I should be told {0}", expected);
		String response = (String) scenarioActionContext.getItem(RESPONSE).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "response")));
		assertEquals(expected, response);
		logScenarioUtil.log("I have been told {0}", expected);
	}

}
