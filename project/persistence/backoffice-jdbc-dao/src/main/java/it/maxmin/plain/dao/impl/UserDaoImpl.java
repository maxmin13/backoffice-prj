package it.maxmin.plain.dao.impl;

import static it.maxmin.plain.dao.QueryConstants.DELETE_USER;
import static it.maxmin.plain.dao.QueryConstants.FIND_USER_BY_ACCOUNT_NAME;
import static it.maxmin.plain.dao.QueryConstants.FIND_USER_BY_FIRST_NAME;
import static it.maxmin.plain.dao.QueryConstants.INSERT_USER;
import static it.maxmin.plain.dao.QueryConstants.SELECT_ALL_USERS;
import static it.maxmin.plain.dao.QueryConstants.UPDATE_USER;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.springframework.util.Assert.notNull;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.api.UserDao;
import it.maxmin.plain.dao.exception.DataAccessException;

public class UserDaoImpl extends BaseDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@Override
	public Set<User> findAll() {
		Set<User> result = new HashSet<>();
		try (var connection = getConnection();
				var statement = connection.prepareStatement(SELECT_ALL_USERS);
				var resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				var user = new User();
				user.setUserId(resultSet.getLong("UserId"));
				user.setAccountName(resultSet.getString("AccountName"));
				user.setFirstName(resultSet.getString("FirstName"));
				user.setLastName(resultSet.getString("LastName"));
				user.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
				user.setCreateDate(resultSet.getTimestamp("CreatedDate").toLocalDateTime());
				result.add(user);
			}
		} catch (SQLException ex) {
			LOGGER.error("Problem when executing SELECT!", ex);
			throw new DataAccessException("Problem when executing SELECT!", ex);
		}
		return result;
	}

	@Override
	public User findByAccountName(String accountName) {
		try (var connection = getConnection()) {
			var statement = connection.prepareStatement(FIND_USER_BY_ACCOUNT_NAME);
			statement.setString(1, accountName);
			var resultSet = statement.executeQuery();
			if (resultSet.next()) {
				var user = new User();
				user.setUserId(resultSet.getLong("UserId"));
				user.setAccountName(resultSet.getString("AccountName"));
				user.setFirstName(resultSet.getString("FirstName"));
				user.setLastName(resultSet.getString("LastName"));
				user.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
				user.setCreateDate(resultSet.getTimestamp("CreatedDate").toLocalDateTime());
				return user;
			}
		} catch (SQLException ex) {
			LOGGER.error("Problem when executing SELECT!", ex);
			throw new DataAccessException("Problem when executing SELECT!", ex);
		}
		return null;
	}

	@Override
	public Set<User> findByFirstName(String firstName) {
		Set<User> result = new HashSet<>();
		try (var connection = getConnection()) {
			var statement = connection.prepareStatement(FIND_USER_BY_FIRST_NAME);
			statement.setString(1, firstName);
			var resultSet = statement.executeQuery();
			while (resultSet.next()) {
				var user = new User();
				user.setUserId(resultSet.getLong("UserId"));
				user.setAccountName(resultSet.getString("AccountName"));
				user.setFirstName(resultSet.getString("FirstName"));
				user.setLastName(resultSet.getString("LastName"));
				user.setBirthDate(resultSet.getDate("BirthDate").toLocalDate());
				user.setCreateDate(resultSet.getTimestamp("CreatedDate").toLocalDateTime());
				result.add(user);
			}
		} catch (SQLException ex) {
			LOGGER.error("Problem when executing SELECT!", ex);
			throw new DataAccessException("Problem when executing SELECT!", ex);
		}
		return result;
	}

	@Override
	public User create(User user) {
		notNull(user, "The user must not be null");
		try (var connection = getConnection()) {
			var statement = connection.prepareStatement(INSERT_USER, RETURN_GENERATED_KEYS);
			statement.setString(1, user.getAccountName());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setDate(4, Date.valueOf(user.getBirthDate()));
			statement.execute();
			var generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				user.setUserId(generatedKeys.getLong(1));
			}
			return user;
		} catch (SQLException ex) {
			LOGGER.error("Problem executing INSERT", ex);
			throw new DataAccessException("Problem executing INSERT", ex);
		}
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");
		try (var connection = getConnection()) {
			var statement = connection.prepareStatement(UPDATE_USER);
			statement.setString(1, user.getAccountName());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setDate(4, Date.valueOf(user.getBirthDate()));
			statement.setLong(5, user.getUserId());
			statement.execute();
		} catch (SQLException ex) {
			LOGGER.error("Problem executing UPDATE", ex);
			throw new DataAccessException("Problem executing UPDATE", ex);
		}
	}

	@Override
	public void delete(Long userId) {
		try (var connection = getConnection(); var statement = connection.prepareStatement(DELETE_USER)) {
			statement.setLong(1, userId);
			statement.execute();
		} catch (SQLException ex) {
			LOGGER.error("Problem executing DELETE", ex);
			throw new DataAccessException("Problem executing DELETE", ex);
		}
	}

}
