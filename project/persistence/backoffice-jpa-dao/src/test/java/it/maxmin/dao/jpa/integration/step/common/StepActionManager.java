package it.maxmin.dao.jpa.integration.step.common;

import static it.maxmin.common.constant.MessageConstants.ERROR_OBJECT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.integration.step.constant.StepConstants.RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.dao.jpa.integration.step.context.ScenarioContext;

public class StepActionManager {

	private ScenarioContext scenarioContext;
	private MessageService messageService;

	@Autowired
	public StepActionManager(ScenarioContext scenarioContext, MessageService messageService) {
		this.scenarioContext = scenarioContext;
		this.messageService = messageService;
	}

	public void setItem(String key, Object value) {
		assertNotNull(key);
		assertNotNull(value);
		scenarioContext.getItems().put(key, value);
	}

	public Optional<Object> getItem(String key) {
		assertNotNull(key);
		Optional<Object> value = Optional.ofNullable(scenarioContext.getItems().get(key));
		scenarioContext.getItems().get(key);
		return value;
	}
	
	public void removeItem(String key) {
		assertNotNull(key);
		scenarioContext.getItems().remove(key);
	}

	public <T extends Object> List<T> getItemsOfType(Class<T> itemClass) {
		assertNotNull(itemClass);
		return scenarioContext.getItems().values().stream()
				.filter(item -> itemClass.isAssignableFrom(item.getClass())).map(itemClass::cast).toList();
	}

	public void saveResponse(String response) {
		assertNotNull(response);
		setItem(RESPONSE, response);
	}

	public void verifyResponse(String expected) {
		assertNotNull(expected);
		String response = (String) getItem(RESPONSE).orElseThrow(
				() -> new JpaDaoTestException(messageService.getMessage(ERROR_OBJECT_NOT_FOUND_MSG, "response")));
		assertEquals(expected, response);
	}
}
