package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jdbc.impl.constant.UserRole.ADMINISTRATOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.UserRole;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoDepartment;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;
import it.maxmin.model.jdbc.domain.pojo.PojoUserRole;

class InsertUserWithAddressAndRoleTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserWithAddressAndRoleTest.class);
	private InsertUserWithAddressAndRole insertUserWithAddressAndRole;

	@Autowired
	InsertUserWithAddressAndRoleTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUserWithAddressAndRole = new InsertUserWithAddressAndRole(dataSource, jdbcTemplate);
	}

	@Test
	void executeWithNoUserThrowsException() {

		LOGGER.info("running test executeWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserWithAddressAndRole.execute(user));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withId(1l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()).withName(ACCOUNTS.getName()));

		// run the test
		Throwable throwable = assertThrows(Throwable.class, () -> insertUserWithAddressAndRole.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoDepartmentThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentThrowsException");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi");

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		UserRole role = UserRole.newInstance().withId(null).withRoleName(ADMINISTRATOR.getRoleName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserWithAddressAndRole.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoDepartmentIdThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentIdThrowsException");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withName(ACCOUNTS.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		UserRole role = UserRole.newInstance().withId(null).withRoleName(ADMINISTRATOR.getRoleName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserWithAddressAndRole.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void executeWithNoAddressAndNoRole() {

		LOGGER.info("running test execute");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()).withName(ACCOUNTS.getName()));

		// run the test
		insertUserWithAddressAndRole.execute(carl);

		PojoUser user = jdbcQueryTestUtil.findUserByAccountName("carl23");

		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), user);

		PojoDepartment department = jdbcQueryTestUtil.findDepartmentById(user.getDepartmentId());

		jdbcUserTestUtil.verifyDepartment(ACCOUNTS.getName(), department);

		List<PojoUserRole> roles = jdbcQueryTestUtil.findRolesByUserId(user.getId());

		assertEquals(0, roles.size());

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(user.getId());

		assertEquals(0, addresses.size());
	}
	
	@Test
	void execute() {

		LOGGER.info("running test execute");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()).withName(ACCOUNTS.getName()));

		Address address1 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address1);

		// get an existing address
		PojoAddress address2 = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address2);
		carl.addAddress(Address.newInstance().withId(address2.getId()).withCity(address2.getCity())
				.withRegion(address2.getRegion()).withDescription(address2.getDescription())
				.withPostalCode(address2.getPostalCode()));

		UserRole role = UserRole.newInstance().withId(administrator.getId()).withRoleName(ADMINISTRATOR.getRoleName());
		carl.addRole(role);

		// run the test
		insertUserWithAddressAndRole.execute(carl);

		PojoUser user = jdbcQueryTestUtil.findUserByAccountName("carl23");

		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), user);

		PojoDepartment department = jdbcQueryTestUtil.findDepartmentById(user.getDepartmentId());

		jdbcUserTestUtil.verifyDepartment(ACCOUNTS.getName(), department);

		List<PojoUserRole> roles = jdbcQueryTestUtil.findRolesByUserId(user.getId());

		assertEquals(1, roles.size());
		
		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), roles.get(0));

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(user.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(italy.getId(), addresses.get(0).getStateId());
		
		jdbcUserTestUtil.verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(ireland.getId(), addresses.get(1).getStateId());
	}

}
