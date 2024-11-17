package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USERS_BY_FIRST_NAME;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.jdbc.domain.entity.User;

public class SelectUserByFirstName {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<User>> resultSetExtractor;

	public SelectUserByFirstName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		SelectUserHelper helper = new SelectUserHelper();
		this.resultSetExtractor = helper.getResultSetExtractor();
	}

	public List<User> execute(String firstName) {

		notNull(firstName, "The first name must not be null");

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("firstName", firstName, Types.VARCHAR);

		return jdbcTemplate.query(SELECT_USERS_BY_FIRST_NAME, param, resultSetExtractor);
	}
}
