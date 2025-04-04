package it.maxmin.dao.jpa.it.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.it.step.constant.StepConstants.USER;

import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.common.LogUtil;
import it.maxmin.dao.jpa.it.step.common.StepActionManager;
import it.maxmin.model.jpa.dao.entity.User;

public class DeleteUserStepDefinitions {

	private StepActionManager stepActionManager;
	private MessageService messageService;
	private LogUtil logUtil;
	private UserDao userDao;

	public DeleteUserStepDefinitions(StepActionManager stepActionManager, MessageService messageService,
			LogUtil logUtil, UserDao userDao) {
		this.stepActionManager = stepActionManager;
		this.messageService = messageService;
		this.logUtil = logUtil;
		this.userDao = userDao;
	}

	@When("I delete the user")
	public void delete_user() {
		logUtil.log("deleting user from database ...");
		User user = (User) stepActionManager.getItem(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, USER)));
		userDao.delete(user);
	}

}
