package it.maxmin.dao.jpa.integration.step.context;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

public class Transaction {

	private TransactionDefinition transactionDefinition;
	private TransactionStatus transactionStatus;

	public static Transaction newInstance() {
		return new Transaction();
	}

	public TransactionDefinition getTransactionDefinition() {
		return transactionDefinition;
	}

	public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
	}

	public Transaction withTransactionDefinition(TransactionDefinition transactionDefinition) {
		this.transactionDefinition = transactionDefinition;
		return this;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public Transaction withTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
		return this.withTransactionDefinition(transactionDefinition);
	}
}
