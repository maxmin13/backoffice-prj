package it.maxmin.dao.jpa.integration.step.common.util;

import static it.maxmin.dao.jpa.integration.step.constant.TransactionIsolationLevel.REPEATABLE_READ_ISO;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import it.maxmin.dao.jpa.integration.step.constant.TransactionIsolationLevel;

public class TransactionUtil {

	private PlatformTransactionManager transactionManager;
	private LogUtil logUtil;

	@Autowired
	public TransactionUtil(PlatformTransactionManager transactionManager, LogUtil logUtil) {
		this.transactionManager = transactionManager;
		this.logUtil = logUtil;
	}

	public TransactionDefinition createTx(String transactionName) {
		DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
		transactionDefinition.setTimeout(300);
		transactionDefinition.setIsolationLevel(REPEATABLE_READ_ISO.getLevel());
		transactionDefinition.setName(transactionName);
		return transactionDefinition;
	}

	public void setTransactionIsolationLevel(TransactionDefinition transactionDefinition,
			TransactionIsolationLevel transactionIsolationLevel) {
		((DefaultTransactionDefinition) transactionDefinition).setIsolationLevel(transactionIsolationLevel.getLevel());
	}

	public TransactionStatus startTx(TransactionDefinition transactionDefinition) {
		return transactionManager.getTransaction(transactionDefinition);
	}

	public void commitTx(TransactionStatus transactionStatus) {
		transactionManager.commit(transactionStatus);
		logUtil.log("transaction committed");
	}

	public void rollbackTx(TransactionStatus transactionStatus) {
		transactionManager.rollback(transactionStatus);
		logUtil.log("transaction rolled back");
	}

	public Optional<TransactionIsolationLevel> getTransactionIsolationLevel(String description) {
		TransactionIsolationLevel transactionIsolationLevel = null;
		for (TransactionIsolationLevel level : TransactionIsolationLevel.values()) {
			if (level.getDescription().equals(description)) {
				transactionIsolationLevel = level;
				break;
			}
		}
		return Optional.ofNullable(transactionIsolationLevel);
	}

}
