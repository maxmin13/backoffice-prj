package it.maxmin.dao.jpa.it.step.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.transaction.Transaction;

/**
 * {@link ScenarioContext} has the scope of a scenario. Cucumber creates a new
 * instance of each step class for each scenario.
 */
@Scope(value = "cucumber-glue", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScenarioContext {

	private Scenario scenario;
	private Map<String, Object> scenarioItems;
	private Map<String, Transaction> scenarioTransactions;

	public ScenarioContext() {
		scenarioItems = new HashMap<>();
		scenarioTransactions = new HashMap<>();
	}

	public void init(Scenario scenario) {
		assertNotNull(scenario);
		this.scenario = scenario;
	}

	public String getScenarioName() {
		String id = scenario.getId();
		return id.substring(id.lastIndexOf("-") + 1).toUpperCase();
	}

	public Map<String, Transaction> getTrasactions() {
		return scenarioTransactions;
	}

	public Map<String, Object> getItems() {
		return scenarioItems;
	}

}
