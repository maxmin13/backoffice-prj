package it.maxmin.plain.dao.impl;

import static it.maxmin.plain.dao.QueryConstants.FIND_USER_BY_ACCOUNT_NAME;
import static it.maxmin.plain.dao.QueryConstants.FIND_USER_BY_FIRST_NAME;
import static it.maxmin.plain.dao.QueryConstants.SELECT_ALL_USERS;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.api.UserDao;
import it.maxmin.plain.dao.repo.UpdateUser;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<User> findAll() {
		return jdbcTemplate.query(SELECT_ALL_USERS, BeanPropertyRowMapper.newInstance(User.class));
	}

	@Override
	public User findByAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.queryForObject(FIND_USER_BY_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(User.class));
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		SqlParameterSource param = new MapSqlParameterSource("firstName", firstName);
		return jdbcTemplate.query(FIND_USER_BY_FIRST_NAME, param, BeanPropertyRowMapper.newInstance(User.class));
	}

	@Override
	public User create(User user) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(this.jdbcTemplate.getJdbcTemplate().getDataSource());
		insertUser.withTableName("User").usingGeneratedKeyColumns("userId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		user.setUserId(result.getKey().longValue());

		return user;
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");
		UpdateUser updateUser = new UpdateUser(this.jdbcTemplate.getJdbcTemplate().getDataSource());
		updateUser.updateByNamedParam(Map.of("accountName", user.getAccountName(), "firstName", user.getFirstName(),
				"lastName", user.getLastName(), "birthData", user.getBirthDate(), "userId", user.getUserId()));
	}

	void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
