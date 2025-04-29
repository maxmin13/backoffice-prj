package it.maxmin.dao.jpa.it.context;

import static it.maxmin.common.constant.MessageConstants.ERROR_MORE_THAN_ONE_OBJECT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.common.service.api.MessageService;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;

public class StepErrorManager {

	private ScenarioErrorContext scenarioErrorContext;
	private MessageService messageService;

	@Autowired
	public StepErrorManager(ScenarioErrorContext scenarioErrorContext, MessageService messageService) {
		this.scenarioErrorContext = scenarioErrorContext;
		this.messageService = messageService;
	}

	public Optional<Exception> getError(String name) {
		assertNotNull(name);
		Optional<Exception> error = scenarioErrorContext.getError(name);
		error.ifPresent(er -> scenarioErrorContext.removeError(name));
		return error;
	}

	/**
	 * @throw JpaDaoTestException if more the one error of the same type is found.
	 */
	public Optional<Exception> getErrorByType(Class<Exception> clazz) {
		assertNotNull(clazz);
		Predicate<Throwable> classIsEqual = error -> clazz.equals(error.getCause().getClass());
		List<Throwable> errors = scenarioErrorContext.getErrors().stream().filter(classIsEqual).map(Throwable::getCause)
				.toList();
		Optional<Exception> opt = switch (errors.size()) {
			case 0 -> {
				yield Optional.empty();
			}
			case 1 -> {
				Exception ex = (Exception) errors.get(0);
				yield Optional.of(ex);
			}
			default ->
				throw new JpaDaoTestException(messageService.getMessage(ERROR_MORE_THAN_ONE_OBJECT_FOUND_MSG, clazz));
		};
		scenarioErrorContext.getErrors().removeIf(classIsEqual);
		return opt;
	}

	public void addError(String name, Exception error) {
		assertNotNull(name);
		assertNotNull(error);
		scenarioErrorContext.addError(name, error);
	}

	public List<Exception> getUnhandledErrors() {
		return scenarioErrorContext.getErrors().stream().toList();
	}

}
