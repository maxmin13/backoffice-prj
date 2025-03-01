package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.BaseStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStepDefinitions extends BaseStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(FindUserStepDefinitions.class);

	private UserDao userDao;

	public FindUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, UserDao userDao) {
		super(transactionManager, stepUtil, messageService);
		this.userDao = userDao;
	}

	@Given("I search for {string} user account name in the database")
	public void search_user_by_account_name(String accountName) {
		LOGGER.debug(MessageFormat.format("SCE({0}): searching user by account name {1} ...",
				getStepContext().getScenarioId(), accountName));
		Optional<User> user = userDao.findByAccountName(accountName);
		user.ifPresentOrElse(u -> getStepContext().addProperty("user", u),
				() -> getStepContext().removeProperty("user"));
	}

	@Given("I search for {string} user first name in the database")
	public void search_user_by_first_name(String firstName) {
		LOGGER.debug(MessageFormat.format("SCE({0}): searching user by first name {1} ...",
				getStepContext().getScenarioId(), firstName));
		Optional<User> user = userDao.findByFirstName(firstName);
		user.ifPresentOrElse(u -> getStepContext().addProperty("user", u),
				() -> getStepContext().removeProperty("user"));
	}

	@When("I check whether the user it's there")
	public void check_user_is_there() {
		LOGGER.debug(
				MessageFormat.format("SCE({0}): cheking if the user is there  ...", getStepContext().getScenarioId()));
		getStepContext().get("user").ifPresentOrElse(u -> getStepContext().addProperty("found", "Yes"),
				() -> getStepContext().addProperty("found", "Nope"));
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		LOGGER.debug(
				MessageFormat.format("SCE({0}): I should be told {1} ...", getStepContext().getScenarioId(), expected));
		String found = (String) getStepContext().get("found").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "found")));
		assertEquals(expected, found);
		LOGGER.debug(MessageFormat.format("SCE({0}): user found {1} ...", getStepContext().getScenarioId(), expected));
	}

	@Then("the user found should be")
	public void the_user_should_be(DataTable dataTable) {
		LOGGER.debug(
				MessageFormat.format("SCE({0}): checking the user properties ...", getStepContext().getScenarioId()));
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);

		User user = (User) getStepContext().get("user").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		assertEquals(accountName, user.getAccountName());
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(birthDate, user.getBirthDate());
		assertEquals(departmentName, user.getDepartment().getName());
		LOGGER.debug(MessageFormat.format("SCE({0}): {1}", getStepContext().getScenarioId(), user));
	}

	@Before
	public void initStep(Scenario scenario) {
		getStepContext().init(scenario);
		LOGGER.debug(MessageFormat.format("SCE({0}): creating step context ...", getStepContext().getScenarioId()));
	}

	@After
	public void cleanStep() {
		LOGGER.debug(MessageFormat.format("SCE({0}): cleaning step context ...", getStepContext().getScenarioId()));
		getStepContext().get("user").ifPresent(u -> {
			User user = (User) u;
			DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
			txDefinition.setTimeout(300);
			TransactionIsolationLevel transactionIsolationLevel = getStepUtil()
					.getTransactionIsolationLevel(REPEATABLE_READ_ISO.getDescription())
					.orElseThrow(() -> new JpaDaoTestException(
							getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "transaction level")));
			txDefinition.setIsolationLevel(transactionIsolationLevel.getLevel());
			TransactionStatus txStatus = getTransactionManager().getTransaction(txDefinition);
			userDao.delete((User) user);
			getTransactionManager().commit(txStatus);
			LOGGER.debug(MessageFormat.format("SCE({0}): user deleted.", getStepContext().getScenarioId()));
		});
	}
}
