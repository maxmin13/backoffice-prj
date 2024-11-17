package it.maxmin.dao.jdbc.impl.operation.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.Role;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoDepartment;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;
import it.maxmin.model.jdbc.domain.pojo.PojoRole;

class InsertUserTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserTest.class);
	private InsertUser insertUser;

	@Autowired
	InsertUserTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUser = new InsertUser(dataSource);
	}

	@Test
	void executeWithNoUserThrowsException() {

		LOGGER.info("running test executeWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(user));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		User carl = User.newInstance().withId(1l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId())
						.withName(accounts.getName()));

		// run the test
		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoDepartmentThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi");

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(null).withRoleName(administrator.getRoleName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoDepartmentIdThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentIdThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance()
						.withName(accounts.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(null).withRoleName(administrator.getRoleName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// delete all users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId())
						.withName(accounts.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(null).withRoleName(administrator.getRoleName());
		carl.addRole(role);

		// run the test
		User user = insertUser.execute(carl);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), user);

		PojoUser newUser = jdbcQueryTestUtil.findUserByAccountName("carl23");

		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), newUser);

		PojoDepartment department = jdbcQueryTestUtil.findDepartmentById(newUser.getDepartmentId());

		jdbcUserTestUtil.verifyDepartment(accounts.getName(), department);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());

		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserId(user.getId());

		assertEquals(0, roles.size());
	}
}