package it.maxmin.dao.jpa.it.common;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.it.context.ScenarioContext;

public class CommonScenarioHooks {

	private ScenarioContext scenarioContext;

	@Autowired
	public CommonScenarioHooks(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@Before
	public void initScenarioContext(Scenario scenario) {
		scenarioContext.set(scenario);
	}
}
