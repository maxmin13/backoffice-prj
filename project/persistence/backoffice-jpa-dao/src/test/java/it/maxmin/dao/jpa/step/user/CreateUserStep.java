package it.maxmin.dao.jpa.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;

import java.text.MessageFormat;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.BaseDatabaseStep;
import it.maxmin.dao.jpa.step.StepContext;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStep extends BaseDatabaseStep {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserStep.class);

	private UserDao userDao;
	private DepartmentDao departmentDao;
	private MessageService messageService;
	private StepContext stepContext;

	@Autowired
	public CreateUserStep(UserDao userDao, DepartmentDao departmentDao, MessageService messageService) {
		this.userDao = userDao;
		this.departmentDao = departmentDao;
		this.messageService = messageService;
	}

	@Given("I want to create the following user {string} with first name {string} and last name {string}, born the day {int} of the month {int} in the year {int}")
	public void build_user(String accountName, String firstName, String lastName, int day, int month, int year) {
		LOGGER.debug(MessageFormat.format("{0}: building user step ...", stepContext.getId()));
		Department department = departmentDao.selectByDepartmentName("Legal").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "department")));
		User user = User.newInstance().withAccountName(accountName).withBirthDate(LocalDate.of(year, month, day))
				.withFirstName(firstName).withLastName(lastName).withDepartment(department);
		stepContext.addProperty("user", user);
	}

	@When("I create it")
	public void create_user() {
		LOGGER.debug(MessageFormat.format("{0}: creating user step ...", stepContext.getId()));
		stepContext.getProperty("user").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		userDao.create((User) stepContext.getProperty("user").get());
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario.getName());
	}

	@After
	public void clear() {
		stepContext.getProperty("user").ifPresent(user -> userDao.delete((User) user));
		stepContext.removeProperty("user");
	}
}
