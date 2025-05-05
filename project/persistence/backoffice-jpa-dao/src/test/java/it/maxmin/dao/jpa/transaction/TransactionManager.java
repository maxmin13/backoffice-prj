package it.maxmin.dao.jpa.transaction;

import static it.maxmin.dao.jpa.transaction.TransactionIsolation.REPEATABLE_READ_ISO;
import static it.maxmin.dao.jpa.transaction.TransactionPropagation.REQUIRED_PROPAGATION;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

@Component
public class TransactionManager {

	private static long identifier = 0;
	private static final int TRANSACTION_TIMEOUT = 10;
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	public TransactionManager(PlatformTransactionManager platformTransactionManager) {
		this.platformTransactionManager = platformTransactionManager;
	}

	public Transaction createTx() {
		return createTx(REQUIRED_PROPAGATION, REPEATABLE_READ_ISO);
	}

	public Transaction createTx(TransactionPropagation transactionPropagation,
			TransactionIsolation transactionIsolation) {
		assertNotNull(transactionPropagation);
		assertNotNull(transactionIsolation);
		String id = createID();
		return Transaction.newInstance().withId(id).withTimeout(TRANSACTION_TIMEOUT)
				.withIsolationLevel(transactionIsolation).withPropagationBehaviour(transactionPropagation);
	}

	public void startTx(Transaction transaction) {
		assertNotNull(transaction);
		assertNotNull(transaction.getId());
		TransactionStatus transactionStatus = platformTransactionManager
				.getTransaction(transaction.getTransactionDefinition());
		transaction.withTransactionStatus(transactionStatus);
	}

	public void commitTx(Transaction transaction) {
		assertNotNull(transaction);
		assertNotNull(transaction.getId());
		platformTransactionManager.commit(transaction.getTransactionStatus());
	}

	public void rollbackTx(Transaction transaction) {
		assertNotNull(transaction);
		assertNotNull(transaction.getId());
		platformTransactionManager.rollback(transaction.getTransactionStatus());
	}

	private static synchronized String createID() {
		return String.valueOf(identifier++);
	}
}
