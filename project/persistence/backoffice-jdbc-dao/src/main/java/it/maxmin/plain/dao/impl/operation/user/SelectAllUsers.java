package it.maxmin.plain.dao.impl.operation.user;

import static it.maxmin.plain.dao.impl.operation.user.UserQueryConstants.SELECT_ALL_USERS;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.plain.pojos.User;

public class SelectAllUsers {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectAllUsers(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<User> execute() {
		return jdbcTemplate.query(SELECT_ALL_USERS, BeanPropertyRowMapper.newInstance(User.class));
	}

}
