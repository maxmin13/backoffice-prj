package it.maxmin.dao.jpa.it.common;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import io.cucumber.datatable.DataTable;
import it.maxmin.dao.jpa.it.constant.DatabaseExeption;

@Component
public class FeatureErrorHelper {

	public Optional<DatabaseExeption> getDatabaseError(String description) {
		DatabaseExeption stepError = null;
		for (DatabaseExeption err : DatabaseExeption.values()) {
			if (err.getDescription().equals(description)) {
				stepError = err;
				break;
			}
		}
		return Optional.ofNullable(stepError);
	}

	public List<String> buildErrors(DataTable errors) {
		assertNotNull(errors);
		return errors.asLists().get(0).stream().toList();
	}

}
