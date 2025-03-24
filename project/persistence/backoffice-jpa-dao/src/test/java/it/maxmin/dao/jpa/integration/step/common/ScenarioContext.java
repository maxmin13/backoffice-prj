package it.maxmin.dao.jpa.integration.step.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import io.cucumber.java.Scenario;

/**
 * {@link ScenarioContext} has the scope of a scenario. Cucumber creates a new
 * instance of each step class for each scenario.
 */
@Scope(value = "cucumber-glue", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ScenarioContext {

	private Scenario scenario;
	private Map<String, Object> scenarioObjects;

	public ScenarioContext() {
		scenarioObjects = new HashMap<>();
	}

	public void init(Scenario scenario) {
		this.scenario = scenario;
	}

	public void add(String key, Object value) {
		scenarioObjects.put(key, value);
	}

	public Optional<Object> get(String key) {
		return Optional.ofNullable(scenarioObjects.get(key));
	}

	public void remove(String key) {
		scenarioObjects.remove(key);
	}

	public Boolean contains(String key) {
		return scenarioObjects.containsKey(key);
	}

	public Collection<Object> getSavedObjects() {
		return scenarioObjects.values();
	}

	public String getScenarioName() {
		String id = scenario.getId();
		return id.substring(id.lastIndexOf("-") + 1).toUpperCase();
	}
}
