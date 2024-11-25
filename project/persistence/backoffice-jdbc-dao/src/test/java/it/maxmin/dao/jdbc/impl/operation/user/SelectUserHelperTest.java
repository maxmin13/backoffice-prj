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
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.BaseDaoTest;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.UnitTestContextCfg;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.User;

@SpringJUnitConfig(classes = { UnitTestContextCfg.class })
class SelectUserHelperTest extends BaseDaoTest {

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
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectUserHelper selectUserHelper = new SelectUserHelper(resultSetUserBuilder, resultSetAddressBuilder);
		resultSetExtractor = selectUserHelper.getResultSetExtractor();
	}

	@Test
	void selectAllUsersNotFound() throws SQLException {

		LOGGER.info("running test selectAllUsersNotFound");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		ResultSet rs = null;
		try (PreparedStatement pstmt = dataSource.getConnection().prepareStatement(SELECT_ALL_USERS)) {
			rs = pstmt.executeQuery();
		}

		// run the test
		List<User> users = resultSetExtractor.extractData(rs);

		assertEquals(0, users.size());
	}

	@Test
	void selectAllUsersWithNoAddressesAndNoRoles() throws SQLException {

		LOGGER.info("running test selectAllUsersWithNoAddresses");

		// users without roles and addresses
		String[] scripts = { "2_useraddress.down.sql", "2_userrole.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		ResultSet rs = null;
		try (PreparedStatement pstmt = dataSource.getConnection().prepareStatement(SELECT_ALL_USERS)) {
			rs = pstmt.executeQuery();
		}

		// run the test
		List<User> users = resultSetExtractor.extractData(rs);

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(0, maxmin.getRoles().size());

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());

		User artur = users.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(0, artur.getRoles().size());

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());
	}

	@Test
	void selectAllUsers() throws SQLException {

		LOGGER.info("running test selectAllUsers");

		ResultSet rs = null;
		try (PreparedStatement pstmt = dataSource.getConnection().prepareStatement(SELECT_ALL_USERS)) {
			rs = pstmt.executeQuery();
		}

		// run the test
		List<User> users = resultSetExtractor.extractData(rs);

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		Optional<Role> role1 = maxmin.getRole(administrator.getRoleName());

		assertEquals(true, role1.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role1.get());

		Optional<Role> role2 = maxmin.getRole(user.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role2.get());

		Optional<Role> role3 = maxmin.getRole(worker.getRoleName());

		assertEquals(true, role3.isPresent());

		jdbcUserTestUtil.verifyRole(worker.getRoleName(), role3.get());

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(2, maxmin.getAddresses().size());

		Optional<Address> address1 = maxmin.getAddress("30010");

		assertEquals(true, address1.isPresent());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1.get());
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), address1.get().getState());

		Optional<Address> address2 = maxmin.getAddress("A65TF12");

		assertEquals(true, address2.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2.get());
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address2.get().getState());

		User artur = users.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		Optional<Role> role4 = artur.getRole(administrator.getRoleName());

		assertEquals(true, role4.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4.get());

		Optional<Role> role5 = artur.getRole(user.getRoleName());

		assertEquals(true, role5.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5.get());

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Optional<Address> address3 = artur.getAddress("A65TF12");

		assertEquals(true, address3.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3.get());
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.get().getState());
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

		Optional<Role> role4 = artur.getRole(administrator.getRoleName());

		assertEquals(true, role4.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4.get());

		Optional<Role> role5 = artur.getRole(user.getRoleName());

		assertEquals(true, role5.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5.get());

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Optional<Address> address3 = artur.getAddress("A65TF12");

		assertEquals(true, address3.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3.get());
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.get().getState());
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

		Optional<Role> role4 = artur.getRole(administrator.getRoleName());

		assertEquals(true, role4.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role4.get());

		Optional<Role> role5 = artur.getRole(user.getRoleName());

		assertEquals(true, role5.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role5.get());

		// department
		jdbcUserTestUtil.verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Optional<Address> address3 = artur.getAddress("A65TF12");

		assertEquals(true, address3.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3.get());
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), address3.get().getState());
	}
}