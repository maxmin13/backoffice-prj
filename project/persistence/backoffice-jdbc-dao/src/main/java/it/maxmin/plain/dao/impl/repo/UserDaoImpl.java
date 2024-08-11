package it.maxmin.plain.dao.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.api.repo.UserDao;
import it.maxmin.plain.dao.impl.operation.user.InsertUser;
import it.maxmin.plain.dao.impl.operation.user.SelectAllUsers;
import it.maxmin.plain.dao.impl.operation.user.SelectUserByAccountName;
import it.maxmin.plain.dao.impl.operation.user.SelectUserByFirstName;
import it.maxmin.plain.dao.impl.operation.user.UpdateUser;

@Repository
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private UpdateUser updateUser;
	private InsertUser insertUser;
	private SelectUserByFirstName selectUserByFirstName;
	private SelectUserByAccountName selectUserByAccountName;
	private SelectAllUsers selectAllUsers;

	@Autowired
	public void setJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateUser = new UpdateUser(jdbcTemplate);
		this.insertUser = new InsertUser(jdbcTemplate);
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
	public User create(User user) {
		notNull(user, "The user must not be null");
		User newUser = this.insertUser.execute(user);
		LOGGER.info("New user  {} {} inserted with id: {}", user.getFirstName(), user.getLastName(),
				newUser.getUserId());
		return user;
	}

	@Override
	public void update(User user) {
		notNull(user, "The user must not be null");
		updateUser.execute(user);
		LOGGER.info("Existing user updated with id: {}", user.getUserId());
	}
}
