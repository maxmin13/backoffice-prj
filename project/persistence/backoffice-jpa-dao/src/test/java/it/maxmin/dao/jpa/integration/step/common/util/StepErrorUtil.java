package it.maxmin.dao.jpa.integration.step.common.util;

import java.util.Optional;

import it.maxmin.dao.jpa.integration.step.constant.StepError;

public class StepErrorUtil {

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
