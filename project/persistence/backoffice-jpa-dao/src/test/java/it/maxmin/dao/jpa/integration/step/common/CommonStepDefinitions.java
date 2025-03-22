package it.maxmin.dao.jpa.integration.step.common;
import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.NOPE;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.SHOULD_BE_TOLD;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;

public class CommonStepDefinitions extends AbstractStepDefinitions {

	public CommonStepDefinitions(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService, ScenarioContext scenarioContext) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
	}

	@Then("I check if a {string} error have been raised")
	public void check_if_specific_error_have_been_raised(String description) {
		assertNotNull(description);
		log("checking {0} error", description);
		StepError stepError = getStepError(description);
		Class<?> expected = stepError.getExceptionClass();
		getFromScenarioContext(EXCEPTION).filter(expected::equals).ifPresentOrElse(ex -> {
			addToScenarioContext(SHOULD_BE_TOLD, YES);
			log("a {0} error has been raised", description);
		}, () -> {
			addToScenarioContext(SHOULD_BE_TOLD, NOPE);
			log("no {0} error has been raised", description);
		});
	}

	@And("I check if one of the following errors have been raised")
	public void check_if_one_of_these_error_have_been_raised(DataTable errors) {
		log("checking if one of the described errors has been raised");
		assertNotNull(errors);
		getFromScenarioContext(EXCEPTION).ifPresentOrElse(trownEx -> {
			errors.asLists().get(0).stream().map(errorDescription -> getStepError(errorDescription).getExceptionClass())
					.filter(trownEx.getClass()::equals).findAny().ifPresentOrElse(ex -> {
						addToScenarioContext(SHOULD_BE_TOLD, YES);
						log("a {0} error has been raised", ex.getClass().getName());
					}, () -> {
						addToScenarioContext(SHOULD_BE_TOLD, NOPE);
						log("none of the described errors has been raised");
					});

		}, () -> {
			addToScenarioContext(SHOULD_BE_TOLD, NOPE);
			log("no error has been raised");
		});
	}

	@Then("I verify if the {string} was successful")
	public void check_success(String action) {
		log("checking {0} success", action);
		getFromScenarioContext(EXCEPTION).ifPresentOrElse(u -> {
			addToScenarioContext(SHOULD_BE_TOLD, NOPE);
			log("action {0} wasn''t successfull", action);
		}, () -> {
			addToScenarioContext(SHOULD_BE_TOLD, YES);
			log("action {0} was successfull", action);
		});
	}
	
	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		assertNotNull(expected);
		log("I should be told {0}", expected);
		String found = (String) getFromScenarioContext(SHOULD_BE_TOLD).orElseThrow(
				() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "should be told response")));
		assertEquals(expected, found);
		log("I have been told {0}", expected);
	}

}
