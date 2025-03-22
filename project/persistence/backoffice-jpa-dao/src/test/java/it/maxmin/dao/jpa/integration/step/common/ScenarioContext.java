package it.maxmin.dao.jpa.integration.step.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Scope;

import io.cucumber.java.Scenario;

/**
 * {@link ScenarioContext} has the scope of a scenario. Cucumber creates a new
 * instance of each step class for each scenario.
 */
@Scope("cucumber-glue")
public class ScenarioContext {

	private Scenario scenario;
	private Map<String, Object> objects;

	public ScenarioContext() {
		objects = new HashMap<>();
	}

	public void init(Scenario scenario) {
		this.scenario = scenario;
	}

	public void addProperty(String key, Object value) {
		objects.put(key, value);
	}

	public Optional<Object> get(String key) {
		return Optional.ofNullable(objects.get(key));
	}

	public void removeProperty(String key) {
		objects.remove(key);
	}

	public Boolean containsProperty(String key) {
		return objects.containsKey(key);
	}

	public Collection<Object> getSavedObjects() {
		return objects.values();
	}

	public String getScenarioId() {
		String id = scenario.getId();
		return id.substring(id.lastIndexOf("-") + 1).toUpperCase();
	}

	public String getScenarioName() {
		return scenario.getName();
	}
}
