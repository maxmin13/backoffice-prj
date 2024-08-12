package it.maxmin.plain.dao.impl.operation.user;

import static it.maxmin.plain.dao.impl.operation.user.UserQueryConstants.SELECT_USER_BY_FIRST_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.plain.pojos.User;

public class SelectUserByFirstName {
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public SelectUserByFirstName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<User> execute(String firstName) {
		notNull(firstName, "The first name must not be null");
		SqlParameterSource param = new MapSqlParameterSource("firstName", firstName);
		return jdbcTemplate.query(SELECT_USER_BY_FIRST_NAME, param, BeanPropertyRowMapper.newInstance(User.class));
	}
}
