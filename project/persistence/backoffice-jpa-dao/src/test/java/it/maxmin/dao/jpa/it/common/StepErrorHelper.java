package it.maxmin.dao.jpa.it.common;

import java.util.Optional;

import it.maxmin.dao.jpa.it.constant.StepError;

public class StepErrorHelper {

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
	
}
