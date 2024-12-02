package it.maxmin.dao.jpa;

import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESSES_BY_USER_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESS_BY_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ALL_ADDRESSES;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_DEPARTMENT_BY_USER_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLES_BY_USER_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLES_BY_USER_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_ROLE_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_STATE_BY_ADDRESS_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_STATE_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_STATE_BY_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USERS_BY_ADDRESS_ID;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USERS_BY_ADDRESS_POSTAL_CODE;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USERS_BY_ROLE_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static it.maxmin.dao.jpa.JpaQueryTestConstants.SELECT_USER_BY_USER_ID;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_USER_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_USER_NOT_NULL_MSG;
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

	public Optional<PojoUser> selectUserByUserId(Long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		List<PojoUser> users = jdbcTemplate.query(SELECT_USER_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
		return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
	}

	public Optional<PojoUser> selectUserByAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		List<PojoUser> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
		return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
	}

	public List<PojoUser> selectUsersByAddressId(Long id) {
		SqlParameterSource param = new MapSqlParameterSource("id", id);
		return jdbcTemplate.query(SELECT_USERS_BY_ADDRESS_ID, param, BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public List<PojoUser> selectUsersByPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		return jdbcTemplate.query(SELECT_USERS_BY_ADDRESS_POSTAL_CODE, param,
				BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public List<PojoUser> selectUsersByRoleName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		return jdbcTemplate.query(SELECT_USERS_BY_ROLE_NAME, param, BeanPropertyRowMapper.newInstance(PojoUser.class));
	}

	public PojoUser insertUser(PojoUser user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.usingColumns("AccountName", "FirstName", "LastName", "DepartmentId", "BirthDate");
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

	public List<PojoRole> selectRolesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ROLES_BY_USER_ID, param, BeanPropertyRowMapper.newInstance(PojoRole.class));
	}

	public List<PojoRole> selectRolesByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.query(SELECT_ROLES_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoRole.class));
	}

	public Optional<PojoRole> selectRoleByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		List<PojoRole> roles = jdbcTemplate.query(SELECT_ROLE_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoRole.class));
		return roles.isEmpty() ? Optional.empty() : Optional.of(roles.get(0));
	}

	public List<PojoAddress> selectAddressesByUserId(long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public List<PojoAddress> selectAddressesByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public Optional<PojoAddress> selectAddressByPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<PojoAddress> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
		return addresses.isEmpty() ? Optional.empty() : Optional.of(addresses.get(0));
	}

	public Optional<PojoAddress> selectAddressByAddressId(long addressId) {
		SqlParameterSource param = new MapSqlParameterSource("id", addressId);
		List<PojoAddress> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_ID, param,
				BeanPropertyRowMapper.newInstance(PojoAddress.class));
		return addresses.isEmpty() ? Optional.empty() : Optional.of(addresses.get(0));
	}

	public List<PojoAddress> selectAllAddresses() {
		return jdbcTemplate.query(SELECT_ALL_ADDRESSES, BeanPropertyRowMapper.newInstance(PojoAddress.class));
	}

	public PojoAddress insertAddress(PojoAddress address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.usingColumns("Description", "City", "Region", "PostalCode", "StateId");
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
		notNull(userAddress, ERROR_USER_ADDRESS_NOT_NULL_MSG);

		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
		simpleJdbcInsert.usingGeneratedKeyColumns("Id");
		simpleJdbcInsert.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		simpleJdbcInsert.execute(paramSource);
		LOGGER.info("User {} associated with address {}", userAddress.getUserId(), userAddress.getAddressId());
	}

	public Optional<PojoState> selectStateByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		List<PojoState> states = jdbcTemplate.query(SELECT_STATE_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoState.class));
		return states.isEmpty() ? Optional.empty() : Optional.of(states.get(0));
	}

	public Optional<PojoState> selectStateByAddressId(Long addressId) {
		SqlParameterSource param = new MapSqlParameterSource("addressId", addressId);
		List<PojoState> states = jdbcTemplate.query(SELECT_STATE_BY_ADDRESS_ID, param,
				BeanPropertyRowMapper.newInstance(PojoState.class));
		return states.isEmpty() ? Optional.empty() : Optional.of(states.get(0));
	}

	public Optional<PojoState> selectStateByAddressPostalCode(String postalCode) {
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<PojoState> states = jdbcTemplate.query(SELECT_STATE_BY_ADDRESS_POSTAL_CODE, param,
				BeanPropertyRowMapper.newInstance(PojoState.class));
		return states.isEmpty() ? Optional.empty() : Optional.of(states.get(0));
	}

	public Optional<PojoDepartment> selectDepartmentById(long id) {
		SqlParameterSource param = new MapSqlParameterSource("id", id);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_ID, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}

	public Optional<PojoDepartment> selectDepartmentByName(String name) {
		SqlParameterSource param = new MapSqlParameterSource("name", name);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}

	public Optional<PojoDepartment> selectDepartmentByUserId(Long userId) {
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_USER_ID, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}

	public Optional<PojoDepartment> selectDepartmentByUserAccountName(String accountName) {
		SqlParameterSource param = new MapSqlParameterSource("accountName", accountName);
		List<PojoDepartment> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_USER_ACCOUNT_NAME, param,
				BeanPropertyRowMapper.newInstance(PojoDepartment.class));
		return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
	}

}
