package it.maxmin.plain.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DaoTestUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(DaoTestUtil.class);

	public static void runScripts(String[] scripts, NamedParameterJdbcTemplate jdbcTemplate) {
		for (String script : scripts) {
			try {
				Files.readAllLines(Paths.get("src/test/resources/embedded/" + script))
						.forEach(jdbcTemplate.getJdbcTemplate()::update);
			}
			catch (IOException e) {
				LOGGER.error("Error running DB scripts", e);
				throw new DaoTestException("Error running DB scripts", e);
			}
		}
	}
}
