package it.maxmin.dao.jpa.integration.step.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.cucumber.java.Scenario;

/**
 * For each scenario, Cucumber create a new instance of each step class.
 * */
public class StepContext {

	private Scenario scenario;
	private Map<String, Object> properties;

	public StepContext() {
		properties = new HashMap<>();
	}
	
	public void init(Scenario scenario) {
		this.scenario = scenario;
	}

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public Optional<Object> get(String key) {
		return Optional.ofNullable(properties.get(key));
	}

	public void removeProperty(String key) {
		properties.remove(key);
	}

	public Boolean containsProperty(String key) {
		return properties.containsKey(key);
	}

	public String getScenarioId() {
		String id = scenario.getId();
		return id.substring(id.lastIndexOf("-") + 1).toUpperCase();
	}
	
	public String getScenarioName() {
		return scenario.getName();
	}
}
