package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.NOPE;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.SHOULD_BE_TOLD;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.USER;
import static it.maxmin.dao.jpa.integration.step.common.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.AbstractStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStepDefinitions extends AbstractStepDefinitions {

	private UserDao userDao;

	public FindUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, ScenarioContext scenarioContext, UserDao userDao) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
		this.userDao = userDao;
	}

	@Given("I search for {string} user account name in the database")
	public void search_user_by_account_name(String accountName) {
		assertNotNull(accountName);
		log("searching user by account name {0}", accountName);
		userDao.findByAccountName(accountName).ifPresentOrElse(u -> {
			addToScenarioContext(USER, u);
			log("user {0} found by account name", accountName);
		}, () -> {
			log("user {0} not found by account name", accountName);
		});
	}

	@When("I check if the user {string} is there")
	public void check_if_user_is_there(String userName) {
		assertNotNull(userName);
		log("cheking if {0} is there", userName);
		getFromScenarioContext(USER).ifPresentOrElse(u -> {
			addToScenarioContext(SHOULD_BE_TOLD, YES);
			log("user {0} found", userName);
		}, () -> {
			addToScenarioContext(SHOULD_BE_TOLD, NOPE);
			log("user {0} not found", userName);
		});
	}

	@Then("the user should be")
	public void the_user_should_be(DataTable user) {
		log("checking the user properties");
		assertNotNull(user);
		List<List<String>> data = user.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);

		User u = (User) getFromScenarioContext(USER)
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		assertEquals(accountName, u.getAccountName());
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(birthDate, u.getBirthDate());
		assertEquals(departmentName, u.getDepartment().getName());

		log("the user is as expected");
	}

}
