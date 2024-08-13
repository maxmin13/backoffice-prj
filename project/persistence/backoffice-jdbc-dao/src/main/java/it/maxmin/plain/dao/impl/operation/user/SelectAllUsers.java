package it.maxmin.plain.dao.impl.operation.user;

import static it.maxmin.plain.dao.impl.operation.user.UserQueryConstants.SELECT_ALL_USERS;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.plain.pojos.User;

public class SelectAllUsers extends SelectUser {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAllUsers.class);

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectAllUsers(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<User> execute() {
		return jdbcTemplate.getJdbcTemplate().query(SELECT_ALL_USERS, usersExtractor);
	}

}
