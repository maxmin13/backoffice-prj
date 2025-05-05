package it.maxmin.dao.jpa.it.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@link ScenarioErrorContext} has the scope of a scenario. Cucumber creates a
 * new instance of each step class for each scenario.
 */
@Component
@Scope(value = "cucumber-glue", proxyMode = TARGET_CLASS)
public class ScenarioErrorContext {

	private Map<String, Exception> errors;

	@Autowired
	public ScenarioErrorContext() {
		errors = new HashMap<>();
	}

	public void addError(String name, Exception error) {
		assertNotNull(name);
		assertNotNull(error);
		errors.put(name, error);
	}

	public Optional<Exception> getError(String name) {
		return Optional.ofNullable(errors.get(name));
	}

	public void removeError(String name) {
		assertNotNull(name);
		errors.remove(name);
	}

	public Collection<Exception> getErrors() {
		return errors.values();// mutable
	}

}
