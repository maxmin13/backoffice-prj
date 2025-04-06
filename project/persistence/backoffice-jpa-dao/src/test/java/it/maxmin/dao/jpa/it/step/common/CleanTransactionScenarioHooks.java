package it.maxmin.dao.jpa.it.step.common;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.transaction.StepTransactionManager;

public class CleanTransactionScenarioHooks {

	private StepTransactionManager stepTransactionManager;

	@Autowired
	public CleanTransactionScenarioHooks(StepTransactionManager stepTransactionManager) {
		this.stepTransactionManager = stepTransactionManager;
	}

	@After(order = 1000)
	public void cleanScenarioContext(Scenario scenario) {
		if (stepTransactionManager.getPendingTransaction().size() > 0) {
			throw new JpaDaoTestException("Found pending scenario transactions");
		}
	}

}
