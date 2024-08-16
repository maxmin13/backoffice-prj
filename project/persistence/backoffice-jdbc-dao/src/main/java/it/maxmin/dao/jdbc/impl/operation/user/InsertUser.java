package it.maxmin.dao.jdbc.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import it.maxmin.model.jdbc.User;

public class InsertUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUser.class);

	private DataSource dataSource;

	public InsertUser(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public User execute(User user) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(dataSource);
		insertUser.withTableName("User").usingGeneratedKeyColumns("Id");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		Number keyValue = result.getKey();
		if (keyValue != null) {
			user.setId(keyValue.longValue());
		}
		else {
			LOGGER.warn("User primary key not found");
		}

		return user;
	}
}
