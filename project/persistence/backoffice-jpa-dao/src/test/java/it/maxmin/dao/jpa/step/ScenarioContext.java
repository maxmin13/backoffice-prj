package it.maxmin.dao.jpa.step;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

	private Map<String, Object> scenarioContext;

	public ScenarioContext() {
		scenarioContext = new HashMap<>();
	}

	public void setContext(Context key, Object value) {
		scenarioContext.put(key.toString(), value);
	}

	public Object getContext(Context key) {
		return scenarioContext.get(key.toString());
	}
	
	public void removeContext(Context key) {
		getContext(key);
	}

	public Boolean contains(Context key) {
		return scenarioContext.containsKey(key.toString());
	}
}
