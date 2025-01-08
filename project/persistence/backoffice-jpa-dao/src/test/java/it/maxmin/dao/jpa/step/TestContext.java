package it.maxmin.dao.jpa.step;

import org.springframework.stereotype.Component;

@Component
public class TestContext {

	public ScenarioContext scenarioContext;

	public TestContext() {
		scenarioContext = new ScenarioContext();
	}

	public ScenarioContext getScenarioContext() {
		return scenarioContext;
	}
}
