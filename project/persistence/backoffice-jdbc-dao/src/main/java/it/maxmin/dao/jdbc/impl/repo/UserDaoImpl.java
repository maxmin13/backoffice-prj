package it.maxmin.dao.jdbc.impl.repo;

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
		return this.selectAllUsers.execute();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<User> selectByAccountName(String accountName) {
		notNull(accountName, "The account name must not be null");
		return this.selectUserByAccountName.execute(accountName);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> selectByFirstName(String firstName) {
		notNull(firstName, "The first name must not be null");
		return this.selectUserByFirstName.execute(firstName);
	}

	@Override
	public User insert(User user) {
		notNull(user, "The user must not be null");
		if (user.getId() == null) {
			LOGGER.info("Inserting new user ...");
			User newUser = this.insertUser.execute(user);
			LOGGER.info("User created with id: {}", user.getId());
			return newUser;
		}
		else {
			throw new IllegalArgumentException("The user ID must be null");
		}
	}

	@Override
	public void associateAddress(Long userId, Long addressId) {
		notNull(userId, "The user ID must not be null");
		notNull(addressId, "The address ID must not be null");
		this.insertUserAddress.execute(userId, addressId);
		LOGGER.info("User {} associated with address {}", userId, addressId);
	}

	@Override
	public void associateRole(Long userId, Long roleId) {
		notNull(userId, "The user ID must not be null");
		notNull(roleId, "The role ID must not be null");
		this.insertUserRole.execute(userId, roleId);
		LOGGER.info("User {} associated with role {}", userId, roleId);
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");
		if (user.getId() == null) {
			throw new IllegalArgumentException("The user ID must not be null");
		}
		else {
			LOGGER.info("Updating new user ...");
			updateUser.execute(user);
			LOGGER.info("User saved with id: {}", user.getId());
		}
	}
}
