package it.maxmin.plain.dao.impl;

import static it.maxmin.plain.dao.QueryConstants.FIND_USER_BY_ACCOUNT_NAME;
import static it.maxmin.plain.dao.QueryConstants.SELECT_ALL_USERS;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.api.UserDao;
import it.maxmin.plain.dao.mapper.UserMapper;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public List<User> findAll() {
		return jdbcTemplate.query(SELECT_ALL_USERS, new UserMapper());
	}

	@Override
	public String findByAccountName(String accountName) {
		return jdbcTemplate.queryForObject(FIND_USER_BY_ACCOUNT_NAME, Map.of("accountName", accountName), String.class);
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		return null; //jdbcTemplate.queryForList(FIND_USER_BY_FIRST_NAME, User.class, firstName);
	}

	@Override
	public User create(User user) {
		notNull(user, "The user must not be null");

		KeyHolder keyHolder = new GeneratedKeyHolder();
/*
		jdbcTemplate.update(connection -> {
			var statement = connection.prepareStatement(INSERT_USER, RETURN_GENERATED_KEYS);
			statement.setString(1, user.getAccountName());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setDate(4, Date.valueOf(user.getBirthDate()));
			PreparedStatement ps = connection.prepareStatement(INSERT_USER, RETURN_GENERATED_KEYS);
			return ps;
		}, keyHolder);

		user.setUserId(keyHolder.getKey().longValue()); */
		return user;
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");/*
		jdbcTemplate.update(UPDATE_USER, user.getAccountName(), user.getFirstName(), user.getLastName(),
				user.getBirthDate(), user.getUserId()); */
	}

	@Override
	public void delete(Long userId) {
	//	jdbcTemplate.update(DELETE_USER, userId);
	}

	void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
