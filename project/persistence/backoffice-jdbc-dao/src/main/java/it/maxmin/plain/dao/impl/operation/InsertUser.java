package it.maxmin.plain.dao.impl.operation;

import static org.springframework.util.Assert.notNull;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import it.maxmin.model.plain.pojos.User;

public class InsertUser {
	
	private JdbcTemplate jdbcTemplate;
	
	public InsertUser(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate.getJdbcTemplate();
	}

	public User execute(User user) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertUser.withTableName("User").usingGeneratedKeyColumns("userId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		user.setUserId(result.getKey().longValue());

		return user;
	}
}
