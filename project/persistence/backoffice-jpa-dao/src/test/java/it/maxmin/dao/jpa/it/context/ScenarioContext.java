package it.maxmin.dao.jpa.it.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import io.cucumber.java.Scenario;

/**
 * {@link ScenarioContext} has the scope of a scenario. Cucumber creates a
 * new instance of each step class for each scenario.
 */
@Scope(value = "cucumber-glue", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScenarioContext {

	private Scenario scenario;
	
	public void set(Scenario scenario) {
		assertNotNull(scenario);
		this.scenario = scenario;
	}

	public String getScenarioName() {
		String id = scenario.getId();
		return id.substring(id.lastIndexOf("-") + 1).toUpperCase();
	}
}
