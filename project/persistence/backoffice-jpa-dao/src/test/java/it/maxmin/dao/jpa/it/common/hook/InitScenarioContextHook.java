package it.maxmin.dao.jpa.it.common.hook;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.it.context.ScenarioContext;

public class InitScenarioContextHook {

	private ScenarioContext scenarioContext;

	@Autowired
	public InitScenarioContextHook(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@Before
	public void init(Scenario scenario) {
		scenarioContext.set(scenario);
	}
}
