package it.maxmin.dao.jpa;

import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESSES_BY_USER_USER_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESS_BY_ADDRESS_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ALL_ADDRESSES;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLES_BY_USER_USER_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLE_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_STATE_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_STATE_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_ROLE_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

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

import it.maxmin.model.jpa.dao.pojo.PojoAddress;
import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoRole;
import it.maxmin.model.jpa.dao.pojo.PojoState;
import it.maxmin.model.jpa.dao.pojo.PojoUser;
import it.maxmin.model.jpa.dao.pojo.PojoUserAddress;

public class JpaQueryTestUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaQueryTestUtil.class);

	private NamedParameterJdbcTemplate jdbcTemplate;
	private DataSource dataSource;

	public JpaQueryTestUtil(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.dataSource = dataSource;
	}

	public Optional<PojoUser> findUserByUserId(Long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		List<PojoUser> users = jdbcTemplate.query(SELECT_USER_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
		return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
	}

	public Optional<PojoUser> findUserByAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		List<PojoUser> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
		return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
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
			throw new JpaDaoTestException("User key not generated");
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
	
	public Optional<PojoRole> findRoleByRoleName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("roleName", name);
		List<PojoRole> roles = jdbcTemplate.query(SELECT_ROLE_BY_NAME, param, BeanPropertyRowMapper.newInstance(PojoRole.class));
		return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
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
	
	public Optional<PojoAddress> findAddressByPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<PojoAddress> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
		return addresses.isEmpty() ? Optional.empty() : Optional.of(addresses.get(0));
	}

	public Optional<PojoAddress> findAddressByAddressId(long addressId) {
		SqlParameterSource param = new MapSqlParameterSource("addressId", addressId);
		List<PojoAddress> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_ADDRESS_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
		return addresses.isEmpty() ? Optional.empty() : Optional.of(addresses.get(0));
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
			throw new JpaDaoTestException("Address key not generated");
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

	public Optional<PojoState> findStateByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		List<PojoState> states = jdbcTemplate.query(SELECT_STATE_BY_NAME, param, BeanPropertyRowMapper.newInstance(PojoState.class));
		return states.isEmpty() ? Optional.empty() : Optional.of(states.get(0));
	}
	
	public Optional<PojoState> findStateByAddressPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<PojoState> states = jdbcTemplate.query(SELECT_STATE_BY_ADDRESS_POSTAL_CODE, param, BeanPropertyRowMapper.newInstance(PojoState.class));
		return states.isEmpty() ? Optional.empty() : Optional.of(states.get(0));
	}
	
	public Optional<PojoDepartment> findDepartmentById(long id) {
		SqlParameterSource param = new MapSqlParameterSource("id", id);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_ID, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}

	public Optional<PojoDepartment> findDepartmentByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}
	
	public Optional<PojoDepartment> findDepartmentByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}
}
