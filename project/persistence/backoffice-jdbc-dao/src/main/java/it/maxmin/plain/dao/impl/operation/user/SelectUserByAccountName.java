package it.maxmin.plain.dao.impl.operation.user;

import static it.maxmin.plain.dao.impl.operation.user.UserQueryConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.plain.pojos.User;

public class SelectUserByAccountName {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserByAccountName.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectUserByAccountName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public User execute(String accountName) {
		notNull(accountName, "The account name must not be null");
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, BeanPropertyRowMapper.newInstance(User.class));
		return users.isEmpty() ? null : users.get(0);
	}
}
