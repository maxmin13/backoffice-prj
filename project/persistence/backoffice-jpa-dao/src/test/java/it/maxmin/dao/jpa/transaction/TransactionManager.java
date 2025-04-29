package it.maxmin.dao.jpa.transaction;

import static it.maxmin.dao.jpa.transaction.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRED_PROPAGATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.common.LogScenarioUtil;

public class TransactionManager {

	private static long identifer = 0;
	private static final int TRANSACTION_TIMEOUT = 65;
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
		try {
			TransactionStatus transactionStatus = platformTransactionManager
					.getTransaction(transaction.getTransactionDefinition());
			transaction.withTransactionStatus(transactionStatus);
			logScenarioUtil.log("transaction {0} started", transaction.getId());
		} catch (TransactionException e) {
			throw new JpaDaoTestException("start transaction error", e);
		}
	}

	public void commitTx(Transaction transaction) {
		assertNotNull(transaction);
		try {
			platformTransactionManager.commit(transaction.getTransactionStatus());
			logScenarioUtil.log("transaction {0} committed", transaction.getId());
		} catch (Exception e) {
			throw new JpaDaoTestException("commit transaction error", e);
		}
	}

	public void rollbackTx(Transaction transaction) {
		assertNotNull(transaction);
		try {
			platformTransactionManager.rollback(transaction.getTransactionStatus());
			logScenarioUtil.log("transaction {0} rolled-back", transaction.getId());
		} catch (Exception e) {
			throw new JpaDaoTestException("rollback transaction error", e);
		}
	}

	private static synchronized String createID() {
		return String.valueOf(identifer++);
	}
}
