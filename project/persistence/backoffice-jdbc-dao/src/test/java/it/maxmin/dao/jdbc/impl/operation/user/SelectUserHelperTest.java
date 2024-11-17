package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_ALL_USERS;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USERS_BY_FIRST_NAME;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.Role;

class SelectUserHelperTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserHelperTest.class);
	private DataSource dataSource;
	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<User>> resultSetExtractor;

	@Autowired
	SelectUserHelperTest(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate,
			JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.dataSource = dataSource;
		this.jdbcTemplate = jdbcTemplate;
	}

	@BeforeEach
	void initTests() {
		SelectUserHelper selectUserHelper = new SelectUserHelper();
		resultSetExtractor = selectUserHelper.getResultSetExtractor();
	}

	@Test
	void selectAllUsersNotFound() throws SQLException {

		LOGGER.info("running test extractDataUserNotFound");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(SELECT_ALL_USERS);
		ResultSet rs = pstmt.executeQuery();

		// run the test
		List<User> users = resultSetExtractor.extractData(rs);

		assertEquals(0, users.size());
	}

	@Test
	void selectAllUsers() throws SQLException {

		LOGGER.info("running test extractData");

		PreparedStatement pstmt = dataSource.getConnection().prepareStatement(SELECT_ALL_USERS);
		ResultSet rs = pstmt.executeQuery();

		// run the test
		List<User> users = resultSetExtractor.extractData(rs);

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		Role role1 = maxmin.getRole(administrator.getRoleName());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role1);

		Role role2 = maxmin.getRole(user.getRoleName());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role2);

		Role role3 = maxmin.getRole(worker.getRoleName());

		jdbcUserTestUtil.verifyRole(worker.getRoleName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(2, maxmin.getAddresses().size());

		Address address1 = maxmin.getAddress("30010");

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), address1.getState());

		Address address2 = maxmin.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address2.getState());

		User artur = users.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		Role role4 = artur.getRole(administrator.getRoleName());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4);

		Role role5 = artur.getRole(user.getRoleName());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.getState());
	}

	@Test
	void selectUserByAccountNameNotFound() {

		LOGGER.info("running test selectUserByAccountNameNotFound");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("accountName", "artur", Types.VARCHAR);

		// run the test
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, resultSetExtractor);

		assertEquals(0, users.size());
	}

	@Test
	void selectUserByAccountName() {

		LOGGER.info("running test selectUserByAccountName");

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("accountName", "artur", Types.VARCHAR);

		// run the test
		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, resultSetExtractor);

		assertEquals(1, users.size());

		User artur = users.get(0);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		Role role4 = artur.getRole(administrator.getRoleName());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4);

		Role role5 = artur.getRole(user.getRoleName());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.getState());
	}
	
	@Test
	void selectUserByFirstNameNotFound() {

		LOGGER.info("running test selectUserByFirstNameNotFound");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("firstName", "Arturo", Types.VARCHAR);

		// run the test
		List<User> users = jdbcTemplate.query(SELECT_USERS_BY_FIRST_NAME, param, resultSetExtractor);

		assertEquals(0, users.size());
	}

	@Test
	void selectUserByFirstName() {

		LOGGER.info("running test selectUserByFirstName");

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("firstName", "Arturo", Types.VARCHAR);

		// run the test
		List<User> users = jdbcTemplate.query(SELECT_USERS_BY_FIRST_NAME, param, resultSetExtractor);

		assertEquals(1, users.size());

		User artur = users.get(0);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		Role role4 = artur.getRole(administrator.getRoleName());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4);

		Role role5 = artur.getRole(user.getRoleName());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.getState());
	}
}