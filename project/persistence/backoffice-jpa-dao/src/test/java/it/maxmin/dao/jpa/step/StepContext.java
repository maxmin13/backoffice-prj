package it.maxmin.dao.jpa.step;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * For each scenario, Cucumber create a new instance of each step class.
 * */
public class StepContext {

	private String id;
	private Map<String, Object> properties;

	public StepContext(String id) {
		this.id = id;
		properties = new HashMap<>();
	}

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public Optional<Object> getProperty(String key) {
		return Optional.ofNullable(properties.get(key));
	}

	public void removeProperty(String key) {
		properties.remove(key);
	}

	public Boolean containsProperty(String key) {
		return properties.containsKey(key);
	}

	public String getId() {
		return id;
	}
}
