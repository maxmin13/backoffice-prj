package it.maxmin.dao.jdbc;

import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_ADDRESS_BY_ADDRESS_ID;
import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_ALL_ADDRESSES;
import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_STATE_BY_NAME;
import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_USER_BY_USER_ID;
import static it.maxmin.dao.jdbc.QueryTestConstants.SELECT_ADDRESSES_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.model.jdbc.Address;
import it.maxmin.model.jdbc.State;
import it.maxmin.model.jdbc.User;
import it.maxmin.model.jdbc.UserAddress;

public class DaoTestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DaoTestUtil.class);

	@Autowired
	private MariaDB4jSpringService mariaDB4jSpringService;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void stopTestDB() {
		mariaDB4jSpringService.stop();
	}

	public void runDBScripts(String[] scripts) {
		for (String script : scripts) {
			try {
				Files.readAllLines(Paths.get("src/test/resources/database/" + script)).stream()
						.filter(line -> !line.trim().isEmpty()).toList()
						.forEach(jdbcTemplate.getJdbcTemplate()::update);
			}
			catch (IOException e) {
				throw new DaoTestException("Error running DB scripts", e);
			}
		}
	}

	public User findUserByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(User.class));
	}

	public User findUserByAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(User.class));
	}

	public User createUser(User user) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource());
		insertUser.withTableName("User").usingGeneratedKeyColumns("userId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		user.setUserId(result.getKey().longValue());

		return user;
	}

	public List<Address> findAddressesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, BeanPropertyRowMapper.newInstance(Address.class));
	}

	public Address findAddressByAddressId(long addressId) {
		SqlParameterSource param = new MapSqlParameterSource("addressId", addressId);
		return jdbcTemplate.queryForObject(SELECT_ADDRESS_BY_ADDRESS_ID, param,
				BeanPropertyRowMapper.newInstance(Address.class));
	}

	public List<Address> findAllAddresses() {
		return jdbcTemplate.query(SELECT_ALL_ADDRESSES, BeanPropertyRowMapper.newInstance(Address.class));
	}

	public Address createAddress(Address address) {
		notNull(address, "The address must not be null");

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(jdbcTemplate.getJdbcTemplate().getDataSource());
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("addressId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = insertAddress.executeAndReturnKeyHolder(paramSource);
		address.setAddressId(result.getKey().longValue());

		return address;
	}

	public void associateUserAddress(UserAddress userAddress) {
		notNull(userAddress, "The user address must not be null");

		SimpleJdbcInsert insertUserAddress = new SimpleJdbcInsert(this.jdbcTemplate.getJdbcTemplate().getDataSource());
		insertUserAddress.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		insertUserAddress.execute(paramSource);
		LOGGER.info("User {} associated with address  {}", userAddress.getUserId(), userAddress.getAddressId());
	}

	public State findStateByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(SELECT_STATE_BY_NAME, param, BeanPropertyRowMapper.newInstance(State.class));
	}

}
