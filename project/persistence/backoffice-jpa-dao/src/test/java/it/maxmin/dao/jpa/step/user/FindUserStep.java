package it.maxmin.dao.jpa.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.StepContext;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStep { 

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
		LOGGER.debug(MessageFormat.format("{0}: searching user step ...", stepContext.getScenarioId()));
		Optional<User> user = userDao.findByAccountName(accountName);
		user.ifPresentOrElse(u -> stepContext.addProperty("user", u), () -> stepContext.removeProperty("user"));
	}

	@When("I check whether the user it's there")
	public void check_user_is_there() {
		LOGGER.debug(MessageFormat.format("{0}: check user step ...", stepContext.getScenarioId()));
		stepContext.getProperty("user").ifPresentOrElse(u -> stepContext.addProperty("found", "Yes"),
				() -> stepContext.addProperty("found", "Nope"));
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		LOGGER.debug(MessageFormat.format("{0}: I should be told {1} step ...", stepContext.getScenarioId(), expected));
		String found = (String) stepContext.getProperty("found").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "found")));
		assertEquals(expected, found);
	}

	@Then("the user found should be")
	public void the_user_should_be(DataTable dataTable) {
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);
		
		User user = (User) stepContext.getProperty("user").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		
		assertEquals(accountName, user.getAccountName());
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(birthDate, user.getBirthDate());
		assertEquals(departmentName, user.getDepartment().getName());
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario);
	}

}
