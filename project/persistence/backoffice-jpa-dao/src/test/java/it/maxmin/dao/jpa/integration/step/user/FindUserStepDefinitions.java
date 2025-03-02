package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.PlatformTransactionManager;

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
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStepDefinitions extends BaseStepDefinitions {

	private UserDao userDao;

	public FindUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, UserDao userDao) {
		super(transactionManager, stepUtil, messageService);
		this.userDao = userDao;
	}

	@Given("I search for {string} user account name in the database")
	public void search_user_by_account_name(String accountName) {
		log("searching user by account name {0} ...", accountName);
		Optional<User> user = userDao.findByAccountName(accountName);
		user.ifPresentOrElse(u -> addToScenarioContext("user", u), () -> removeFromScenarioContext("user"));
	}

	@Given("I search for {string} user first name in the database")
	public void search_user_by_first_name(String firstName) {
		log("searching user by first name {0} ...", firstName);
		Optional<User> user = userDao.findByFirstName(firstName);
		user.ifPresentOrElse(u -> addToScenarioContext("user", u), () -> removeFromScenarioContext("user"));
	}

	@When("I check whether the user it's there")
	public void check_user_is_there() {
		log("cheking if the user is there  ...");
		getFromScenarioContext("user").ifPresentOrElse(u -> addToScenarioContext("found", "Yes"),
				() -> addToScenarioContext("found", "Nope"));
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String expected) {
		log("I should be told {0} ...", expected);
		String found = (String) getFromScenarioContext("found")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "found")));
		assertEquals(expected, found);
		log("user found {0} ...", expected);
	}

	@Then("the user found should be")
	public void the_user_should_be(DataTable dataTable) {
		log("checking the user properties ...");
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);

		User user = (User) getFromScenarioContext("user")
				.orElseThrow(() -> new JpaDaoTestException(getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));

		assertEquals(accountName, user.getAccountName());
		assertEquals(firstName, user.getFirstName());
		assertEquals(lastName, user.getLastName());
		assertEquals(birthDate, user.getBirthDate());
		assertEquals(departmentName, user.getDepartment().getName());
		log("{0}", user);
	}

	@Before
	public void initStep(Scenario scenario) {
		init(scenario);
	}

	@After
	public void cleanStep() {
		log("cleaning step context ...");
		getFromScenarioContext("user").ifPresent(u -> {
			User user = (User) u;
			if (user.getId() != null) {
				startTx();
				userDao.find(user.getId()).ifPresentOrElse(us -> {
					userDao.delete(us);
					commitTx();
				}, () -> rollbackTx());
				log("user deleted");
			}
		});
	}
}
