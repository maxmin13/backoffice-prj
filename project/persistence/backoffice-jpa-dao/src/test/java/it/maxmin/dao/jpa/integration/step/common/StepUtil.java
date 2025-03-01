package it.maxmin.dao.jpa.integration.step.common;

import java.util.Optional;

public class StepUtil {

	public Optional<StepError> getStepError(String description) {
		StepError stepError = null;
		for (StepError err : StepError.values()) {
			if (err.getDescription().equals(description)) {
				stepError = err;
				break;
			}
		}
		return Optional.ofNullable(stepError);
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
