package it.maxmin.dao.jpa.it.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.constant.StepConstants.USER;

import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.ScenarioItemContext;
import it.maxmin.model.jpa.dao.entity.User;

public class DeleteUserStepDefinitions {

	private ScenarioItemContext scenarioItemContext;
	private MessageService messageService;
	private LogScenarioUtil logScenarioUtil;
	private UserDao userDao;

	public DeleteUserStepDefinitions(ScenarioItemContext scenarioItemContext, MessageService messageService,
			LogScenarioUtil logScenarioUtil, UserDao userDao) {
		this.scenarioItemContext = scenarioItemContext;
		this.messageService = messageService;
		this.logScenarioUtil = logScenarioUtil;
		this.userDao = userDao;
	}

	@When("I delete the user")
	public void delete_user() {
		logScenarioUtil.log("deleting user from database ...");
		User user = (User) scenarioItemContext.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, USER)));
		userDao.delete(user);
	}

}
