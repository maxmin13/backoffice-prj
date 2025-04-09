package it.maxmin.dao.jpa.it.common;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import it.maxmin.dao.jpa.it.transaction.StepTransactionManager;

public class CleanTransactionScenarioHooks {

	private StepTransactionManager stepTransactionManager;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public CleanTransactionScenarioHooks(StepTransactionManager stepTransactionManager,
			LogScenarioUtil logScenarioUtil) {
		this.stepTransactionManager = stepTransactionManager;
		this.logScenarioUtil = logScenarioUtil;
	}

	@After(order = 1000)
	public void cleanScenarioContext(Scenario scenario) {
		stepTransactionManager.getPendingTransaction().forEach(tx -> {
			logScenarioUtil.error("Found pending transaction {0}", tx.getId());
			stepTransactionManager.rollbackTx(tx.getId());
		});
	}

}
