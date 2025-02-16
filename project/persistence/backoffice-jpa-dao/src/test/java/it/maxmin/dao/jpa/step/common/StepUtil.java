package it.maxmin.dao.jpa.step.common;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
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
}
