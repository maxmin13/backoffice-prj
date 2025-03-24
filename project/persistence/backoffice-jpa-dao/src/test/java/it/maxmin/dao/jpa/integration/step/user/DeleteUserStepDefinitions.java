package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;

import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.util.LogUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class DeleteUserStepDefinitions {

	private ScenarioContext scenarioContext;
	private MessageService messageService;
	private LogUtil logUtil;
	private UserDao userDao;

	public DeleteUserStepDefinitions(MessageService messageService, ScenarioContext scenarioContext, LogUtil logUtil,
			UserDao userDao) {
		this.scenarioContext = scenarioContext;
		this.messageService = messageService;
		this.logUtil = logUtil;
		this.userDao = userDao;
	}

	@When("I delete the user")
	public void delete_user() {
		logUtil.log("deleting user from database ...");
		User user = (User) scenarioContext.get("user").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		userDao.deleteByAccountName(user.getAccountName());
	}

}
