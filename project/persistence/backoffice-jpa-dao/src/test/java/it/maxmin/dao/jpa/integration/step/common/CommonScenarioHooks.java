package it.maxmin.dao.jpa.integration.step.common;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.integration.step.common.util.LogUtil;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;

public class CommonScenarioHooks {

	private ScenarioContext scenarioContext;
	private LogUtil logUtil;

	@Autowired
	public CommonScenarioHooks(ScenarioContext scenarioContext, LogUtil logUtil) {
		this.scenarioContext = scenarioContext;
		this.logUtil = logUtil;
	}

	@Before
	public void initScenario(Scenario scenario) {
		scenarioContext.init(scenario);
		logUtil.log("scenario {0} initialized", scenarioContext.getScenarioName());
	}
}
