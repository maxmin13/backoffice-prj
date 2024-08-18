package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.jdbc.domain.entity.User;

public class SelectUserByAccountName extends SelectUser {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserByAccountName.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectUserByAccountName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public User execute(String accountName) {

		notNull(accountName, "The account name must not be null");
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("accountName", accountName, Types.VARCHAR);

		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, userExtractor);
		 
		return (users == null || users.isEmpty()) ? null : users.get(0);
	}
}
