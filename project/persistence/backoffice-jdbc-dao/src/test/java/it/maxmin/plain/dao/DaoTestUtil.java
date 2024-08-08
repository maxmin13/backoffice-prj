package it.maxmin.plain.dao;

import static it.maxmin.plain.dao.QueryTestConstants.FIND_USER_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.model.plain.pojos.User;

public class DaoTestUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(DaoTestUtil.class);
	
	public static void stopDB(MariaDB4jSpringService mariaDB4jSpringService) {
		mariaDB4jSpringService.stop();
	}

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
	
	public static User findUserByUserId(long userId, NamedParameterJdbcTemplate jdbcTemplate) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.queryForObject(FIND_USER_BY_USER_ID, param, BeanPropertyRowMapper.newInstance(User.class));
	}
	
	public static User insertUser(User user, NamedParameterJdbcTemplate jdbcTemplate) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource());
		insertUser.withTableName("User").usingGeneratedKeyColumns("userId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		user.setUserId(result.getKey().longValue());
		
		return user;
	}
}
