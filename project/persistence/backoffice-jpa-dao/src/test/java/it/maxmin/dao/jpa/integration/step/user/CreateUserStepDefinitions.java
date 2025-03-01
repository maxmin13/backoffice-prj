package it.maxmin.dao.jpa.integration.step.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel.REPEATABLE_READ_ISO;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
import it.maxmin.dao.jpa.api.repo.DepartmentDao;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.common.BaseStepDefinitions;
import it.maxmin.dao.jpa.integration.step.common.StepError;
import it.maxmin.dao.jpa.integration.step.common.StepUtil;
import it.maxmin.dao.jpa.integration.step.common.TransactionIsolationLevel;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.User;

public class CreateUserStepDefinitions extends BaseStepDefinitions {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserStepDefinitions.class);

	private UserDao userDao;
	private DepartmentDao departmentDao;

	public CreateUserStepDefinitions(PlatformTransactionManager transactionManager, MessageService messageService,
			StepUtil stepUtil, UserDao userDao, DepartmentDao departmentDao) {
		super(transactionManager, stepUtil, messageService);
		this.userDao = userDao;
		this.departmentDao = departmentDao;
	}

	@Given("I want to create the following user")
	public void build_user(DataTable dataTable) {
		LOGGER.debug(
				MessageFormat.format("SCE({0}): I want to create a user user ...", getStepContext().getScenarioId()));
		List<List<String>> data = dataTable.asLists();
		String accountName = data.get(0).get(0);
		String firstName = data.get(0).get(1);
		String lastName = data.get(0).get(2);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM dd");
		LocalDate birthDate = LocalDate.parse(data.get(0).get(3), formatter);
		String departmentName = data.get(0).get(4);
		Department department = departmentDao.selectByDepartmentName(departmentName)
				.orElseThrow(() -> new JpaDaoTestException(
						getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "department")));
		User user = User.newInstance().withAccountName(accountName).withBirthDate(birthDate).withFirstName(firstName)
				.withLastName(lastName).withDepartment(department);
		getStepContext().addProperty("user", user);
	}

	@When("I create it")
	public void create_user() {
		LOGGER.debug(MessageFormat.format("SCE({0}): inserting user in database step ...",
				getStepContext().getScenarioId()));
		User user = (User) getStepContext().get("user").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "user")));
		try {
			userDao.create(user);
			LOGGER.debug(MessageFormat.format("SCE({0}): created user {1}", getStepContext().getScenarioId(), user));
		}
		catch (Exception e) {
			LOGGER.debug(MessageFormat.format("SCE({0})", e));
			getStepContext().addProperty("exception", e);
		}
	}
	
	@Then("a create {string} error should have been raised")
	public void a_error_should_have_been_thrown(String description) {
		LOGGER.debug(MessageFormat.format("SCE({0}): checking error ...", getStepContext().getScenarioId()));
		Exception ex = (Exception) getStepContext().get("exception").orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "exception")));

		StepError stepError = getStepUtil().getStepError(description).orElseThrow(
				() -> new JpaDaoTestException(getMessageService().getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "step error")));

		assertEquals(stepError.getExceptionClass(), ex.getClass());
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
