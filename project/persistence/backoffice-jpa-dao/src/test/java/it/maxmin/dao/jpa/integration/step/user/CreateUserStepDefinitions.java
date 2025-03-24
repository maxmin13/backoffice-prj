package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.EXCEPTION;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.USER;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStepDefinitions {

	private LogUtil logUtil;
	private ScenarioContext scenarioContext;
	private MessageService messageService;
	private UserDao userDao;
	private DepartmentDao departmentDao;

	@Autowired
	public CreateUserStepDefinitions(ScenarioContext scenarioContext, MessageService messageService, LogUtil logUtil,
			UserDao userDao, DepartmentDao departmentDao) {
		this.logUtil = logUtil;
		this.scenarioContext = scenarioContext;
		this.messageService = messageService;
		this.userDao = userDao;
		this.departmentDao = departmentDao;
	}

	@Given("I want to create the following user")
	public void build_user(DataTable dataTable) {
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);
		Department department = departmentDao.findByDepartmentName(departmentName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "department")));
		User user = User.newInstance().withAccountName(accountName).withBirthDate(birthDate).withFirstName(firstName)
				.withLastName(lastName).withDepartment(department);
		logUtil.log("I want to create a user");
		logUtil.log("{0}", user);
		scenarioContext.add(USER, user);
	}

	@When("I create it")
	public void create_user() {
		logUtil.log("inserting user in database");
		User user = (User) scenarioContext.get(USER).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		try {
			userDao.create(user);
			logUtil.log("created user {0}", user);
		}
		catch (Exception e) {
			logUtil.log("{0}", e);
			scenarioContext.add(EXCEPTION, e);
		}
	}

}
