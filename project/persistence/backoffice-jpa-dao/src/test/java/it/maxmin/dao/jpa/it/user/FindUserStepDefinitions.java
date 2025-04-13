package it.maxmin.dao.jpa.it.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.it.constant.StepConstants.NOPE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.RESPONSE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.USER;
import static it.maxmin.dao.jpa.it.constant.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioActionContext;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStepDefinitions {

	private MessageService messageService;
	private ScenarioActionContext scenarioActionContext;
	private LogScenarioUtil logScenarioUtil;
	private UserDao userDao;

	public FindUserStepDefinitions(ScenarioActionContext scenarioActionContext, MessageService messageService,
			LogScenarioUtil logScenarioUtil, UserDao userDao) {
		this.messageService = messageService;
		this.scenarioActionContext = scenarioActionContext;
		this.logScenarioUtil = logScenarioUtil;
		this.userDao = userDao;
	}

	@Given("I search for the user by id in the database")
	public void search_user_by_id() {
		User user = (User) scenarioActionContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		Long id = user.getId();
		userDao.find(id).ifPresentOrElse(u -> {
			scenarioActionContext.setItem(USER, u);
			logScenarioUtil.log("user {0} found by id", id);
		}, () -> {
			logScenarioUtil.log("user {0} not found by id", id);
			scenarioActionContext.removeItem(USER);
		});
	}

	@Given("I search for {string} user account name in the database")
	public void search_user_by_account_name(String accountName) {
		assertNotNull(accountName);
		try {
			userDao.findByAccountName(accountName).ifPresentOrElse(u -> {
				scenarioActionContext.setItem(USER, u);
				logScenarioUtil.log("user {0} found by account name", accountName);
			}, () -> {
				logScenarioUtil.log("user {0} not found by account name", accountName);
				scenarioActionContext.removeItem(USER);
			});
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			scenarioActionContext.setItem(EXCEPTION, e);
		}
	}

	@When("I check if the user {string} is there")
	public void check_if_user_is_there(String userName) {
		assertNotNull(userName);
		scenarioActionContext.setItem(RESPONSE, NOPE);
		scenarioActionContext.getItem(USER).ifPresent(u -> {
			if (userName.equals(((User) u).getAccountName())) {
				scenarioActionContext.setItem(RESPONSE, YES);
			}
		});
	}

	@Then("the user should be")
	public void the_user_should_be(DataTable user) {
		assertNotNull(user);
		List<List<String>> data = user.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);

		User u = (User) scenarioActionContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		assertEquals(accountName, u.getAccountName());
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(birthDate, u.getBirthDate());
		assertEquals(departmentName, u.getDepartment().getName());

		logScenarioUtil.log("the user is as expected");
	}

}
