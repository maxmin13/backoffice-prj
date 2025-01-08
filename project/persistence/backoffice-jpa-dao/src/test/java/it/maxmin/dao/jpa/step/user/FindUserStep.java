package it.maxmin.dao.jpa.step.user;

import static it.maxmin.dao.jpa.step.Context.FOUND;
import static it.maxmin.dao.jpa.step.Context.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.step.BaseDatabaseStep;
import it.maxmin.dao.jpa.step.TestContext;
import it.maxmin.model.jpa.dao.entity.User;

public class FindUserStep extends BaseDatabaseStep {

	private UserDao userDao;
	private TestContext testContext;
	
	@Autowired
	public FindUserStep(TestContext testContext, UserDao userDao) {
		this.testContext = testContext;
		this.userDao = userDao;
	}

	@Given("I search for {string} user in the database")
	public void search_user(String accountName) {
		Optional<User> user = userDao.findByAccountName(accountName);
		user.ifPresent(u -> testContext.scenarioContext.setContext(USER, u));
		
	}

	@When("I check whether it's there")
	public void check_user_is_there() {
		if (testContext.scenarioContext.contains(USER)) {
			testContext.scenarioContext.setContext(FOUND, "Yes");
		}
		else {
			testContext.scenarioContext.setContext(FOUND, "Nope");
		}
	}

	@Then("I should be told {string}")
	public void i_should_be_told(String response) {
		assertEquals(response, testContext.scenarioContext.getContext(FOUND));
	}

}
