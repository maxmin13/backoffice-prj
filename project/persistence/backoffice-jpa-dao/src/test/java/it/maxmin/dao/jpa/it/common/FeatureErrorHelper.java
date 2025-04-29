package it.maxmin.dao.jpa.it.common;

import java.util.Optional;

import it.maxmin.dao.jpa.it.constant.FeatureError;

public class FeatureErrorHelper {

	public Optional<FeatureError> getFeatureError(String featureDescription) {
		FeatureError stepError = null;
		for (FeatureError err : FeatureError.values()) {
			if (err.getDescription().equals(featureDescription)) {
				stepError = err;
				break;
			}
		}
		return Optional.ofNullable(stepError);
	}
	
}
