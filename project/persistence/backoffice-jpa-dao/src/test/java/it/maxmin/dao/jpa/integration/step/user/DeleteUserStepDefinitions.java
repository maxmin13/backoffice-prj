package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.BaseStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.model.jpa.dao.entity.User;

public class DeleteUserStepDefinitions extends BaseStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserStepDefinitions.class);

	private UserDao userDao;

	public DeleteUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, UserDao userDao) {
		super(transactionManager, stepUtil, messageService);
		this.userDao = userDao;
	}

	@When("I delete the user")
	public void delete_user() {
		LOGGER.debug(
				MessageFormat.format("SCE({0}): deleting user from database ...", getStepContext().getScenarioId()));
		User user = (User) getStepContext().get("user").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		userDao.delete(user);
	}

	@Before
	public void initStep(Scenario scenario) {
		getStepContext().init(scenario);
		LOGGER.debug(MessageFormat.format("{0}: creating step context ...", getStepContext().getScenarioId()));
	}

	@After
	public void cleanStep() {
		LOGGER.debug(MessageFormat.format("SCE({0}): cleaning step context ...", getStepContext().getScenarioId()));
	}

}
