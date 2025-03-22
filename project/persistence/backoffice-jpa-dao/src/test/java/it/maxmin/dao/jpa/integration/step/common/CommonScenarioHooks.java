package it.maxmin.dao.jpa.integration.step.common;

import org.springframework.transaction.PlatformTransactionManager;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import it.maxmin.common.service.api.MessageService;

public class CommonScenarioHooks extends AbstractStepDefinitions {
	
	public CommonScenarioHooks(PlatformTransactionManager transactionManager, StepUtil stepUtil,
			MessageService messageService, ScenarioContext scenarioContext) {
		super(transactionManager, stepUtil, messageService, scenarioContext);
	}

	@Before
	public void initScenario(Scenario scenario) {
		init(scenario);
		log("scenario {0} initialized", getScenarioName());
	}
}
