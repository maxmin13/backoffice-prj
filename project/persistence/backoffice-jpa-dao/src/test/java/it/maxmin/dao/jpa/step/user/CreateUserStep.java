package it.maxmin.dao.jpa.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.step.Context.USER;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.BaseDatabaseStep;
import it.maxmin.dao.jpa.step.TestContext;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStep extends BaseDatabaseStep {

	private UserDao userDao;
	private DepartmentDao departmentDao;
	private TestContext testContext;
	private MessageService messageService;

	@Autowired
	public CreateUserStep(TestContext testContext, UserDao userDao, DepartmentDao departmentDao,
			MessageService messageService) {
		this.testContext = testContext;
		this.userDao = userDao;
		this.departmentDao = departmentDao;
		this.messageService = messageService;
	}

	@Given("I want to create the following user {string} with first name {string} and last name {string}, born the day {int} of the month {int} in the year {int}")
	public void build_user(String accountName, String firstName, String lastName, int day, int month, int year) {
		Department department = departmentDao.selectByDepartmentName("Legal").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "department")));
		User user = User.newInstance().withAccountName(accountName).withBirthDate(LocalDate.of(year, month, day))
				.withFirstName(firstName).withLastName(lastName).withDepartment(department);
		testContext.scenarioContext.setContext(USER, user);
	}

	@When("I create it")
	public void create_user() {
		User user = (User) testContext.scenarioContext.getContext(USER);
		userDao.create(user);
	}

	@After
	public void clear() {
		if (testContext.scenarioContext.contains(USER)) {
			userDao.delete((User) testContext.scenarioContext.getContext(USER));
		}
	}
}
