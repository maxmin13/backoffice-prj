package it.maxmin.dao.jpa.it.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.USER;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

public class UpdateUserStepDefinitions {

	private LogScenarioUtil logScenarioUtil;
	private ScenarioItemContext scenarioItemContext;
	private StepErrorManager stepErrorManager;
	private FeatureUserHelper featureUserHelper;
	private MessageService messageService;
	private UserDao userDao;

	@Autowired
	public UpdateUserStepDefinitions(ScenarioItemContext scenarioItemContext, StepErrorManager stepErrorManager,
			FeatureUserHelper featureUserHelper, MessageService messageService, LogScenarioUtil logScenarioUtil,
			UserDao userDao) {
		this.logScenarioUtil = logScenarioUtil;
		this.scenarioItemContext = scenarioItemContext;
		this.stepErrorManager = stepErrorManager;
		this.featureUserHelper = featureUserHelper;
		this.messageService = messageService;
		this.userDao = userDao;
	}

	@Given("I want to update the user")
	public void i_want_to_update_the_user() {
		User user = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		logScenarioUtil.log("I want to update the user");
		logScenarioUtil.log("{0}", user);
	}

	@When("I update the user")
	public void update_user(DataTable user) {
		assertNotNull(user);
		User data = featureUserHelper.buildUser(user);

		User u = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		u.setAccountName(data.getAccountName());
		u.setFirstName(data.getFirstName());
		u.setLastName(data.getLastName());
		u.setBirthDate(data.getBirthDate());

		try {
			User updated = userDao.update(u);
			// replace the managed entity
			scenarioItemContext.setItem(USER, updated);
			logScenarioUtil.log("updated user {0} in the database", updated);
		}
		catch (Exception e) {
			logScenarioUtil.log("{0}", e);
			stepErrorManager.addError("insert error", e);
		}
	}
}
