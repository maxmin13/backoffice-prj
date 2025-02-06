package it.maxmin.dao.jpa.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.BaseDatabaseStep;
import it.maxmin.dao.jpa.step.StepContext;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStep extends BaseDatabaseStep {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindUserStep.class);

	private UserDao userDao;
	private MessageService messageService;
	private StepContext stepContext;

	@Autowired
	public FindUserStep(UserDao userDao, MessageService messageService) {
		this.userDao = userDao;
		this.messageService = messageService;
	}

	@Given("I search for {string} user in the database")
	public void search_user(String accountName) {
		LOGGER.debug(MessageFormat.format("{0}: searching user step ...", stepContext.getId()));
		Optional<User> user = userDao.findByAccountName(accountName);
		user.ifPresentOrElse(u -> stepContext.addProperty("user", u), () -> stepContext.removeProperty("user"));
	}

	@When("I check whether the user it's there")
	public void check_user_is_there() {
		LOGGER.debug(MessageFormat.format("{0}: check user step ...", stepContext.getId()));
		stepContext.getProperty("user").ifPresentOrElse(u -> stepContext.addProperty("found", "Yes"),
				() -> stepContext.addProperty("found", "Nope"));
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		LOGGER.debug(MessageFormat.format("{0}: I should be told {1} step ...", stepContext.getId(), expected));
		stepContext.getProperty("found").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "found")));
		assertEquals(expected, stepContext.getProperty("found").get());
		stepContext.removeProperty("found");
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario.getName());
	}

}
