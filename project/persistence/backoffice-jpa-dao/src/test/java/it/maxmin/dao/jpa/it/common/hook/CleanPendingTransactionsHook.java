package it.maxmin.dao.jpa.it.common.hook;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jpa.it.common.LogScenarioUtil;
import it.maxmin.dao.jpa.it.transaction.StepTransactionManager;

public class CleanPendingTransactionsHook {

	private StepTransactionManager stepTransactionManager;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public CleanPendingTransactionsHook(StepTransactionManager stepTransactionManager,
			LogScenarioUtil logScenarioUtil) {
		this.stepTransactionManager = stepTransactionManager;
		this.logScenarioUtil = logScenarioUtil;
	}

//	@After(order = 1000)
//	public void clean(Scenario scenario) {
//		stepTransactionManager.getPendingTransaction().forEach(tx -> {
//			logScenarioUtil.error("Found pending transaction {0}", tx.getId());
//			stepTransactionManager.rollbackTx(tx.getId());
//		});
//	}

}
