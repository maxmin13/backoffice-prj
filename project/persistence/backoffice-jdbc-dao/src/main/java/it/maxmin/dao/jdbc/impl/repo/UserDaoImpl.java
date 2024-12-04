package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUser;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserRole;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByAccountName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByFirstName;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;
import it.maxmin.model.jdbc.dao.entity.User;

@Transactional
@Repository("userDao")
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private SelectUserByFirstName selectUserByFirstName;
	private SelectUserByAccountName selectUserByAccountName;
	private SelectAllUsers selectAllUsers;
	private UpdateUser updateUser;
	private InsertUser insertUser;
	private InsertUserAddress insertUserAddress;
	private InsertUserRole insertUserRole;

	@Autowired
	public UserDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateUser = new UpdateUser(dataSource);
		this.insertUser = new InsertUser(dataSource);
		this.insertUserAddress = new InsertUserAddress(dataSource);
		this.insertUserRole = new InsertUserRole(dataSource);
		this.selectUserByFirstName = new SelectUserByFirstName(jdbcTemplate);
		this.selectUserByAccountName = new SelectUserByAccountName(jdbcTemplate);
		this.selectAllUsers = new SelectAllUsers(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> selectAll() {
		return selectAllUsers.execute();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> selectByAccountName(String accountName) {
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		return selectUserByAccountName.execute(accountName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> selectByFirstName(String firstName) {
		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);
		return selectUserByFirstName.execute(firstName);
	}

	@Override
	public User insert(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		if (user.getId() == null) {
			LOGGER.info("Inserting new user ...");
			User newUser = insertUser.execute(user);
			LOGGER.info("User created with id: {}", user.getId());
			return newUser;
		} else {
			throw new IllegalArgumentException(ERROR_USER_ID_NULL_MSG);
		}
	}

	@Override
	public void associateAddress(Long userId, Long addressId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		notNull(addressId, ERROR_ADDRESS_ID_NOT_NULL_MSG);
		this.insertUserAddress.execute(userId, addressId);
		LOGGER.info("User {} associated with address {}", userId, addressId);
	}

	@Override
	public void associateRole(Long userId, Long roleId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		notNull(roleId, ERROR_ROLE_ID_NOT_NULL_MSG);
		this.insertUserRole.execute(userId, roleId);
		LOGGER.info("User {} associated with role {}", userId, roleId);
	}

	@Override
	public Integer update(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		if (user.getId() == null) {
			throw new IllegalArgumentException(ERROR_USER_ID_NOT_NULL_MSG);
		} else {
			LOGGER.info("Updating new user ...");
			return updateUser.execute(user);
		}
	}
}
