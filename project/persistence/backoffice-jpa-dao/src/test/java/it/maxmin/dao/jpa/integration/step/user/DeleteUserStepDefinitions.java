package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;

import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.AbstractStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.ScenarioContext;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class DeleteUserStepDefinitions extends AbstractStepDefinitions {

	private UserDao userDao;

	public DeleteUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, ScenarioContext scenarioContext, UserDao userDao) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
		this.userDao = userDao;
	}

	@When("I delete the user")
	public void delete_user() {
		log("deleting user from database ...");
		User user = (User) getFromScenarioContext("user")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		userDao.deleteByAccountName(user.getAccountName());
	}

}
