package it.maxmin.dao.jpa.integration.step.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.NOPE;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.SHOULD_BE_TOLD;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.YES;
import static java.util.concurrent.CompletableFuture.delayedExecutor;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.constant.StepError;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.util.StepErrorUtil;

public class CommonStepDefinitions {

	private MessageService messageService;
	private StepErrorUtil stepErrorUtil;
	private LogUtil logUtil;
	private ScenarioContext scenarioContext;

	@Autowired
	public CommonStepDefinitions(MessageService messageService, StepErrorUtil stepErrorUtil, LogUtil logUtil,
			ScenarioContext scenarioContext) {
		this.messageService = messageService;
		this.stepErrorUtil = stepErrorUtil;
		this.logUtil = logUtil;
		this.scenarioContext = scenarioContext;
	}

	@Then("I check if a {string} error have been raised")
	public void check_if_specific_error_have_been_raised(String description) {
		assertNotNull(description);
		logUtil.log("checking if a {0} error has been raised", description);
		StepError stepError = stepErrorUtil.getStepError(description).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
		Class<?> expected = stepError.getExceptionClass();
		scenarioContext.get(EXCEPTION).filter(ob -> ob.getClass().equals(expected)).ifPresentOrElse(ex -> {
			scenarioContext.add(SHOULD_BE_TOLD, YES);
			logUtil.log("a {0} error has been raised", description);
		}, () -> {
			scenarioContext.add(SHOULD_BE_TOLD, NOPE);
			logUtil.log("no {0} error has been raised", description);
		});
	}

	@And("I check if one of the following errors have been raised")
	public void check_if_one_of_these_error_have_been_raised(DataTable errors) {
		logUtil.log("checking if one of the described errors has been raised");
		assertNotNull(errors);
		scenarioContext.get(EXCEPTION).ifPresentOrElse(trownEx -> {
			errors.asLists().get(0).stream().map(errorDescription -> {
				StepError stepError = stepErrorUtil.getStepError(errorDescription)
						.orElseThrow(() -> new JpaDaoTestException(
								messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));
				return stepError.getClass();
			}).filter(trownEx.getClass()::equals).findAny().ifPresentOrElse(ex -> {
				scenarioContext.add(SHOULD_BE_TOLD, YES);
				logUtil.log("a {0} error has been raised", ex.getName());
			}, () -> {
				scenarioContext.add(SHOULD_BE_TOLD, NOPE);
				logUtil.log("none of the described errors has been raised");
			});
		}, () -> {
			scenarioContext.add(SHOULD_BE_TOLD, NOPE);
			logUtil.log("no error has been raised");
		});
	}

	@Then("I verify if the {string} action was successful")
	public void check_success(String action) {
		logUtil.log("checking {0} success", action);
		scenarioContext.get(EXCEPTION).ifPresentOrElse(u -> {
			scenarioContext.add(SHOULD_BE_TOLD, NOPE);
			logUtil.log("action {0} wasn''t successfull", action);
		}, () -> {
			scenarioContext.add(SHOULD_BE_TOLD, YES);
			logUtil.log("action {0} was successfull", action);
		});
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		assertNotNull(expected);
		logUtil.log("I should be told {0}", expected);
		String found = (String) scenarioContext.get(SHOULD_BE_TOLD).orElseThrow(() -> new JpaDaoTestException(
				messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "should be told response")));
		assertEquals(expected, found);
		logUtil.log("I have been told {0}", expected);
	}

	@When("I wait a little")
	public void wait_a_little() {
		runAsync(() -> {
		}, delayedExecutor(2000, MILLISECONDS)).join();
	}
}
