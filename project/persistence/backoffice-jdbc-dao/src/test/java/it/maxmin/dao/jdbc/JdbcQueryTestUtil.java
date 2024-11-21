package it.maxmin.dao.jdbc;

import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ADDRESSES_BY_USER_USER_ID;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ADDRESS_BY_ADDRESS_ID;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ALL_ADDRESSES;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_DEPARTMENT_BY_ID;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_DEPARTMENT_BY_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ROLES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ROLES_BY_USER_USER_ID;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_ROLE_BY_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_STATE_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_STATE_BY_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_USER_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_USER_BY_ROLE_NAME;
import static it.maxmin.dao.jdbc.JdbcQueryTestConstants.SELECT_USER_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoDepartment;
import it.maxmin.model.jdbc.domain.pojo.PojoRole;
import it.maxmin.model.jdbc.domain.pojo.PojoState;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;
import it.maxmin.model.jdbc.domain.pojo.PojoUserAddress;

public class JdbcQueryTestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcQueryTestUtil.class);

	private NamedParameterJdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public JdbcQueryTestUtil(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = dataSource;
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
	
	public List<PojoUser> findUsersByPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		return jdbcTemplate.query(SELECT_USER_BY_ADDRESS_POSTAL_CODE, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
	}
	
	public List<PojoUser> findUsersByRoleName(String roleName) {
		SqlParameterSource param = new MapSqlParameterSource("roleName", roleName);
		return jdbcTemplate.query(SELECT_USER_BY_ROLE_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public PojoUser createUser(PojoUser user) {
		notNull(user, "The user must not be null");

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.withTableName("User");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		KeyHolder result = simpleJdbcInsert.executeAndReturnKeyHolder(paramSource);
		Number key = result.getKey();
		if (key != null) {
			user.setId(key.longValue());
		} else {
			throw new DaoTestException("User key not generated");
		}

		return user;
	}

	public List<PojoRole> findRolesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ROLES_BY_USER_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoRole.class));
	}
	
	public List<PojoRole> findRolesByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.query(SELECT_ROLES_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoRole.class));
	}
	
	public PojoRole findRoleByRoleName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("roleName", name);
		return jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME, param, BeanPropertyRowMapper.newInstance(PojoRole.class));
	}

	public List<PojoAddress> findAddressesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}
	
	public List<PojoAddress> findAddressesByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}
	
	public PojoAddress findAddressByPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		return jdbcTemplate.queryForObject(SELECT_ADDRESS_BY_POSTAL_CODE, param,
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

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.withTableName("Address");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = simpleJdbcInsert.executeAndReturnKeyHolder(paramSource);
		Number key = result.getKey();
		if (key != null) {
			address.setId(key.longValue());
		} else {
			throw new DaoTestException("Address key not generated");
		}

		return address;
	}

	public void associateUserAddress(PojoUserAddress userAddress) {
		notNull(userAddress, "The user address must not be null");

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		simpleJdbcInsert.execute(paramSource);
		LOGGER.info("User {} associated with address {}", userAddress.getUserId(), userAddress.getAddressId());
	}

	public PojoState findStateByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(SELECT_STATE_BY_NAME, param, BeanPropertyRowMapper.newInstance(PojoState.class));
	}
	
	public PojoState findStateByAddressPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		return jdbcTemplate.queryForObject(SELECT_STATE_BY_ADDRESS_POSTAL_CODE, param, BeanPropertyRowMapper.newInstance(PojoState.class));
	}
	
	public PojoDepartment findDepartmentById(long id) {
		SqlParameterSource param = new MapSqlParameterSource("id", id);
		return jdbcTemplate.queryForObject(SELECT_DEPARTMENT_BY_ID, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
	}

	public PojoDepartment findDepartmentByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.queryForObject(SELECT_DEPARTMENT_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
	}
	
	public PojoDepartment findDepartmentByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.queryForObject(SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
	}

	public void runDBScripts(String[] scripts) {
		for (String script : scripts) {
			try {
				Files.readAllLines(Paths.get("src/test/resources/database/" + script)).stream()
						.filter(line -> !line.trim().isEmpty()).toList()
						.forEach(jdbcTemplate.getJdbcTemplate()::update);
			} catch (IOException e) {
				throw new DaoTestException("Error running DB scripts", e);
			}
		}
	}
}
