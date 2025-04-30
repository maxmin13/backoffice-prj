package it.maxmin.dao.jpa.transaction;

import static it.maxmin.dao.jpa.transaction.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRED_PROPAGATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import it.maxmin.dao.jpa.it.common.LogScenarioUtil;

public class TransactionManager {

	private static long identifier = 0;
	private static final int TRANSACTION_TIMEOUT = 10;
	private PlatformTransactionManager platformTransactionManager;
	private LogScenarioUtil logScenarioUtil;

	@Autowired
	public TransactionManager(PlatformTransactionManager platformTransactionManager, LogScenarioUtil logScenarioUtil) {
		this.platformTransactionManager = platformTransactionManager;
		this.logScenarioUtil = logScenarioUtil;
	}

	public Transaction createTx() {
		return createTx(REQUIRED_PROPAGATION, REPEATABLE_READ_ISO);
	}

	public Transaction createTx(TransactionPropagation transactionPropagation,
			TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		String id = createID();
		Transaction transaction = Transaction.newInstance().withId(id).withTimeout(TRANSACTION_TIMEOUT)
				.withIsolationLevel(transactionIsolation).withPropagationBehaviour(transactionPropagation);
		logScenarioUtil.log("created transaction {0}", transaction.getId());
		logScenarioUtil.log("propagation behaviour {0}", transactionPropagation.getDescription());
		logScenarioUtil.log("transaction isolation {0}", transactionIsolation.getDescription());
		return transaction;
	}

	public void startTx(Transaction transaction) {
		assertNotNull(transaction);
		TransactionStatus transactionStatus = platformTransactionManager
				.getTransaction(transaction.getTransactionDefinition());
		transaction.withTransactionStatus(transactionStatus);
		logScenarioUtil.log("transaction {0} started", transaction.getId());
	}

	public void commitTx(Transaction transaction) {
		assertNotNull(transaction);
		platformTransactionManager.commit(transaction.getTransactionStatus());
		logScenarioUtil.log("transaction {0} committed", transaction.getId());
	}

	public void rollbackTx(Transaction transaction) {
		assertNotNull(transaction);
		platformTransactionManager.rollback(transaction.getTransactionStatus());
		logScenarioUtil.log("transaction {0} rolled-back", transaction.getId());
	}

	private static synchronized String createID() {
		return String.valueOf(identifier++);
	}
}
