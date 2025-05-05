package it.maxmin.dao.jpa.it.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@link ScenarioItemContext} has the scope of a scenario. Cucumber creates a
 * new instance of each step class for each scenario.
 */
@Component
@Scope(value = "cucumber-glue", proxyMode = TARGET_CLASS)
public class ScenarioItemContext {

	private Map<String, Object> items;

	@Autowired
	public ScenarioItemContext() {
		items = new HashMap<>();
	}

	public void addItem(String name, Object item) {
		assertNotNull(name);
		assertNotNull(item);
		items.put(name, item);
	}

	public Optional<Object> getItem(String name) {
		return Optional.ofNullable(items.get(name));
	}

	public void removeItem(String name) {
		assertNotNull(name);
		items.remove(name);
	}

	public List<Object> getItems() {
		return items.values().stream().toList();
	}

	public <T extends Object> List<T> getItemsOfType(Class<T> itemClass) {
		assertNotNull(itemClass);
		return items.values().stream().filter(item -> itemClass.isAssignableFrom(item.getClass())).map(itemClass::cast)
				.toList();
	}

}
