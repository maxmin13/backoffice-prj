package it.maxmin.dao.jpa.step;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import it.maxmin.dao.jpa.JpaDaoSpringContextUnitTestCfg;
import it.maxmin.dao.jpa.api.repo.UserDao;

@CucumberContextConfiguration
@ContextConfiguration(classes = {JpaDaoSpringContextUnitTestCfg.class})
@TestPropertySource("classpath:jndi/jndi-integration.properties") 
public class FindUserStep {
	
	@Autowired
	UserDao dao;

	@Given("today is Sunday")
	public void today_is_sunday() {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
		dao.findByAccountName("maxmin13");
	}
	@When("I ask whether it's Friday yet")
	public void i_ask_whether_it_s_friday_yet() {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
	}
	@Then("I should be told {string}")
	public void i_should_be_told(String string) {
	    // Write code here that turns the phrase above into concrete actions
//	    throw new io.cucumber.java.PendingException();
	}
}
