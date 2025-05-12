package it.maxmin.dao.jpa.it.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.NOPE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.RESPONSE;
import static it.maxmin.dao.jpa.it.constant.StepConstants.USER;
import static it.maxmin.dao.jpa.it.constant.StepConstants.YES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.it.error.StepErrorManager;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStepDefinitions {

	private MessageService messageService;
	private ScenarioItemContext scenarioItemContext;
	private StepErrorManager stepErrorManager;
	private FeatureUserHelper featureUserHelper;
	private LogScenarioUtil logScenarioUtil;
	private UserDao userDao;

	public FindUserStepDefinitions(ScenarioItemContext scenarioItemContext, MessageService messageService,
			StepErrorManager stepErrorManager, FeatureUserHelper featureUserHelper, LogScenarioUtil logScenarioUtil,
			UserDao userDao) {
		this.messageService = messageService;
		this.scenarioItemContext = scenarioItemContext;
		this.stepErrorManager = stepErrorManager;
		this.featureUserHelper = featureUserHelper;
		this.logScenarioUtil = logScenarioUtil;
		this.userDao = userDao;
	}

	@Given("I search for the user by id in the database")
	public void search_user_by_id() {
		User user = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		Long id = user.getId();
		userDao.find(id).ifPresentOrElse(u -> {
			scenarioItemContext.addItem(USER, u);
			logScenarioUtil.log("user {0} found by id", id);
		}, () -> {
			logScenarioUtil.log("user {0} not found by id", id);
			scenarioItemContext.removeItem(USER);
		});
	}

	@Given("I search for {string} user account name in the database")
	public void search_user_by_account_name(String accountName) {
		assertNotNull(accountName);
		try {
			userDao.findByAccountName(accountName).ifPresentOrElse(u -> {
				scenarioItemContext.addItem(USER, u);
				logScenarioUtil.log("user {0} found by account name", accountName);
			}, () -> {
				logScenarioUtil.log("user {0} not found by account name", accountName);
				scenarioItemContext.removeItem(USER);
			});
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError("find user by account name error", e);
		}
	}

	@Given("I refresh the user")
	public void refresh_user() {
		try {
			User user = (User) scenarioItemContext.getItem(USER).orElseThrow(
					() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
			userDao.refresh(user);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError("refresh user in the persistent content error", e);
		}
	}

	@When("I check if the user {string} is there")
	public void check_if_user_is_there(String userName) {
		assertNotNull(userName);
		scenarioItemContext.addItem(RESPONSE, NOPE);
		scenarioItemContext.getItem(USER).ifPresent(u -> {
			if (userName.equals(((User) u).getAccountName())) {
				scenarioItemContext.addItem(RESPONSE, YES);
			}
		});
	}

	@Then("the user should be")
	public void the_user_should_be(DataTable user) {
		assertNotNull(user);
		User data = featureUserHelper.buildUser(user);
		User u = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		assertEquals(data.getAccountName(), u.getAccountName());
		assertEquals(data.getFirstName(), u.getFirstName());
		assertEquals(data.getLastName(), u.getLastName());
		assertEquals(data.getBirthDate(), u.getBirthDate());
		assertEquals(data.getDepartment().getName(), u.getDepartment().getName());

		logScenarioUtil.log("the user is as expected");
	}

}
