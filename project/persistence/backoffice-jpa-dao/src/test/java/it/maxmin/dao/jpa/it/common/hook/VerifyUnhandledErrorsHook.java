package it.maxmin.dao.jpa.it.common.hook;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.context.StepErrorManager;

public class VerifyUnhandledErrorsHook {

	private StepErrorManager stepItemManager;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public VerifyUnhandledErrorsHook(StepErrorManager stepItemManager, LogScenarioUtil logScenarioUtil) {
		this.stepItemManager = stepItemManager;
		this.logScenarioUtil = logScenarioUtil;
	}

	@After
	public void verify(Scenario scenario) {
		stepItemManager.getUnhandledErrors().forEach(error -> {
			logScenarioUtil.error("Found pending errors {0}", error);
			throw new JpaDaoTestException("Unhandled error in scenario");
		});
	}

}
