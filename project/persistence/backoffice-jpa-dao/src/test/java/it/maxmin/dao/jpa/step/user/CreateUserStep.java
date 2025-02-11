package it.maxmin.dao.jpa.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.step.StepContext;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStep { 

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

	@Given("I want to create the following user")
	public void build_user(DataTable dataTable) {
		LOGGER.debug(MessageFormat.format("{0}: preparing the user step ...", stepContext.getScenarioId()));
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);
		Department department = departmentDao.selectByDepartmentName(departmentName).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "department")));
		User user = User.newInstance().withAccountName(accountName).withBirthDate(birthDate).withFirstName(firstName)
				.withLastName(lastName).withDepartment(department);
		stepContext.addProperty("user", user);
	}

	@When("I create it")
	public void create_user() {
		LOGGER.debug(MessageFormat.format("{0}: creating user step ...", stepContext.getScenarioId()));
		User user = (User) stepContext.getProperty("user").orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		userDao.create(user);
		LOGGER.debug(MessageFormat.format("{0}: {1}", stepContext.getScenarioId(), user));
	}

	@Before
	public void init(Scenario scenario) {
		stepContext = new StepContext(scenario);
	}

}
