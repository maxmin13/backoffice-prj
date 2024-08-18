package it.maxmin.dao.hibernate;

import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_ADDRESSES_BY_USER_ID;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_ADDRESS_BY_ADDRESS_ID;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_ALL_ADDRESSES;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_DEPARTMENT_BY_NAME;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_STATE_BY_NAME;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static it.maxmin.dao.hibernate.HibernateQueryTestConstants.SELECT_USER_BY_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.domain.hibernate.pojo.PojoAddress;
import it.maxmin.domain.hibernate.pojo.PojoDepartment;
import it.maxmin.domain.hibernate.pojo.PojoState;
import it.maxmin.domain.hibernate.pojo.PojoUser;
import it.maxmin.domain.hibernate.pojo.PojoUserAddress;


public class HibernateDaoTestUtil {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateDaoTestUtil.class);

	private MariaDB4jSpringService mariaDB4jSpringService;
	private NamedParameterJdbcTemplate jdbcTemplate;
	private SimpleJdbcInsert simpleJdbcInsert;

	public HibernateDaoTestUtil(MariaDB4jSpringService mariaDB4jSpringService, NamedParameterJdbcTemplate jdbcTemplate,
			DataSource dataSource) {
		simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		this.jdbcTemplate = jdbcTemplate;
		this.mariaDB4jSpringService = mariaDB4jSpringService;
	}

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

	public void testDataSource(DataSource dataSource) throws SQLException {
		try (var connection = dataSource.getConnection();
				var statement = connection.prepareStatement("SELECT 1");
				var resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int mockVal = resultSet.getInt("1");
				assertEquals(1, mockVal);
			}
		}
	}

	public PojoUser findUserByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public PojoUser findUserByAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.queryForObject(SELECT_USER_BY_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public PojoUser createUser(PojoUser user) {
		notNull(user, "The user must not be null");

		simpleJdbcInsert.withTableName("User");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = simpleJdbcInsert.executeAndReturnKeyHolder(paramSource);
		user.setId(result.getKey().longValue());

		return user;
	}

	public List<PojoAddress> findAddressesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public PojoAddress findAddressByAddressId(long addressId) {
		SqlParameterSource param = new MapSqlParameterSource("addressId", addressId);
		return jdbcTemplate.queryForObject(SELECT_ADDRESS_BY_ADDRESS_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public List<PojoAddress> findAllAddresses() {
		return jdbcTemplate.query(SELECT_ALL_ADDRESSES, BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public PojoAddress createAddress(PojoAddress address) {
		notNull(address, "The address must not be null");

		simpleJdbcInsert.withTableName("Address");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = simpleJdbcInsert.executeAndReturnKeyHolder(paramSource);
		address.setId(result.getKey().longValue());

		return address;
	}

	public void associateUserAddress(PojoUserAddress userAddress) {
		notNull(userAddress, "The user address must not be null");

		simpleJdbcInsert.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		simpleJdbcInsert.execute(paramSource);
		LOGGER.info("User {} associated with address  {}", userAddress.getUserId(), userAddress.getAddressId());
	}

	public PojoState findStateByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(SELECT_STATE_BY_NAME, param, BeanPropertyRowMapper.newInstance(PojoState.class));
	}

	public PojoDepartment findDepartmentByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(SELECT_DEPARTMENT_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
	}

}
