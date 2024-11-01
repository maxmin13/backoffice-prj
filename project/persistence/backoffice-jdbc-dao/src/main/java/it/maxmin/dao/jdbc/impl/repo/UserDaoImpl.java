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

import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserWithAddress;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByAccountName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByFirstName;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;
import it.maxmin.model.jdbc.domain.entity.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private UpdateUser updateUser;
	private SelectUserByFirstName selectUserByFirstName;
	private SelectUserByAccountName selectUserByAccountName;
	private SelectAllUsers selectAllUsers;
	private InsertUserWithAddress insertUserWithAddress;
	private InsertUserAddress insertUserAddress;

	@Autowired
	public void setJdbcTemplate(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateUser = new UpdateUser(dataSource);
		this.insertUserWithAddress = new InsertUserWithAddress(dataSource);
		this.insertUserAddress = new InsertUserAddress(dataSource);
		this.selectUserByFirstName = new SelectUserByFirstName(jdbcTemplate);
		this.selectUserByAccountName = new SelectUserByAccountName(jdbcTemplate);
		this.selectAllUsers = new SelectAllUsers(jdbcTemplate);
	}

	@Override
	public List<User> findAll() {
		return this.selectAllUsers.execute();
	}

	@Override
	public Optional<User> findByAccountName(String accountName) {
		notNull(accountName, "The account name must not be null");
		User user = this.selectUserByAccountName.execute(accountName);
		return user != null ? Optional.of(user) : Optional.empty();
	}

	@Override
	public List<User> findByFirstName(String firstName) {
		notNull(firstName, "The first name must not be null");
		return this.selectUserByFirstName.execute(firstName);
	}

	@Override
	public void create(User user) {
		notNull(user, "The user must not be null");
		if (user.getId() == null) {
			LOGGER.info("Inserting new user ...");
			this.insertUserWithAddress.execute(user);
			LOGGER.info("User created with id: {}", user.getId());
		} else {
			throw new IllegalArgumentException("User identifier must be null");
		}
	}

	@Override
	public void associate(long userId, long addressId) {
		this.insertUserAddress.execute(userId, addressId);
		LOGGER.info("User {} associated with address {}", userId, addressId);
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");
		if (user.getId() == null) {
			throw new IllegalArgumentException("User identifier must not be null");
		} else {
			LOGGER.info("Updating new user ...");
			updateUser.execute(user);
			LOGGER.info("User saved with id: {}", user.getId());
		}
	}
}
