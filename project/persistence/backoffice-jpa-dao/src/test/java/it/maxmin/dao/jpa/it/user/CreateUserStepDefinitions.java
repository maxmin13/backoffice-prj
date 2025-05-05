package it.maxmin.dao.jpa.it.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.USER;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.dao.jpa.it.error.StepErrorManager;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStepDefinitions {

	private LogScenarioUtil logScenarioUtil;
	private ScenarioItemContext scenarioItemContext;
	private StepErrorManager stepErrorManager;
	private FeatureUserHelper featureUserHelper;
	private MessageService messageService;
	private UserDao userDao;

	@Autowired
	public CreateUserStepDefinitions(ScenarioItemContext scenarioItemContext, StepErrorManager stepErrorManager,
			FeatureUserHelper featureUserHelper, MessageService messageService, LogScenarioUtil logScenarioUtil,
			UserDao userDao) {
		this.logScenarioUtil = logScenarioUtil;
		this.scenarioItemContext = scenarioItemContext;
		this.stepErrorManager = stepErrorManager;
		this.featureUserHelper = featureUserHelper;
		this.messageService = messageService;
		this.userDao = userDao;
	}

	@Given("I want to create the following user")
	public void i_want_to_create_the_user(DataTable user) {
		User data = featureUserHelper.buildUser(user);
		logScenarioUtil.log("I want to create a user");
		logScenarioUtil.log("{0}", user);
		scenarioItemContext.addItem(USER, data);
	}

	@When("I create the user")
	public void create_user() {
		User user = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		try {
			userDao.create(user);
			logScenarioUtil.log("inserted new user {0} in the database", user);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError("create error", e);
		}
	}

}
