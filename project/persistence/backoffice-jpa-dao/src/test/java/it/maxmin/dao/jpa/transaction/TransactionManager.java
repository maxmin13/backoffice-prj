package it.maxmin.dao.jpa.transaction;

import static it.maxmin.dao.jpa.transaction.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRED_PROPAGATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.it.step.common.LogUtil;

public class TransactionManager {

	private static long identifer = 0;
	private PlatformTransactionManager platformTransactionManager;
	private LogUtil logUtil;

	@Autowired
	public TransactionManager(PlatformTransactionManager platformTransactionManager, LogUtil logUtil) {
		this.platformTransactionManager = platformTransactionManager;
		this.logUtil = logUtil;
	}

	public Transaction createTx() {
		String id = createID();
		Transaction transaction = Transaction.newInstance().withId(id).withTimeout(300)
				.withIsolationLevel(REPEATABLE_READ_ISO).withPropagationBehaviour(REQUIRED_PROPAGATION);
		logUtil.log("created transaction {0} with propagation {1} and isolation {2}", id,
				REQUIRED_PROPAGATION.getDescription(), REPEATABLE_READ_ISO.getDescription());
		return transaction;
	}

	public Transaction createTx(TransactionPropagation transactionPropagation,
			TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		Transaction transaction = createTx();
		transaction.withPropagationBehaviour(transactionPropagation).withIsolationLevel(transactionIsolation);
		logUtil.log("set transaction {0} propagation {1} and isolation {2}", transaction.getId(),
				transactionPropagation.getDescription(), transactionIsolation.getDescription());
		return transaction;
	}

	public void startTx(Transaction transaction) {
		assertNotNull(transaction);
		try {
			TransactionStatus transactionStatus = platformTransactionManager
					.getTransaction(transaction.getTransactionDefinition());
			transaction.withTransactionStatus(transactionStatus);
			logUtil.log("transaction {0} started", transaction.getId());
		}
		catch (TransactionException e) {
			throw new JpaDaoTestException("start transaction error", e);
		}
	}

	public void commitTx(Transaction transaction) {
		assertNotNull(transaction);
		try {
			platformTransactionManager.commit(transaction.getTransactionStatus());
			logUtil.log("transaction {0} committed", transaction.getId());
		}
		catch (Exception e) {
			throw new JpaDaoTestException("commit transaction error", e);
		}
	}

	public void rollbackTx(Transaction transaction) {
		assertNotNull(transaction);
		try {
			platformTransactionManager.rollback(transaction.getTransactionStatus());
			logUtil.log("transaction {0} rolled-back", transaction.getId());
		}
		catch (Exception e) {
			throw new JpaDaoTestException("rollback transaction error", e);
		}
	}

	private static synchronized String createID() {
		return String.valueOf(identifer++);
	}
}
