package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.JdbcTestMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseDaoTest;
import it.maxmin.dao.jdbc.JdbcDaoTestException;
import it.maxmin.dao.jdbc.JdbcDaoUnitTestContextCfg;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoUnitTestContextCfg.class })
class UpdateUserTest extends JdbcBaseDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserTest.class);
	private UpdateUser updateUser;

	@Autowired
	UpdateUserTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.updateUser = new UpdateUser(dataSource);
	}

	@Test
	void executeWithNoUserThrowsException() {

		LOGGER.info("running test executeWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> updateUser.execute(user));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()).withName(accounts.getName()));

		// run the test
		Throwable throwable = assertThrows(Throwable.class, () -> updateUser.execute(carl));

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

		Throwable throwable = assertThrows(Throwable.class, () -> updateUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoDepartmentIdThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentIdThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withName(accounts.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(null).withRoleName(administrator.getRoleName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> updateUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// Find an existing user
		Optional<PojoUser> pojoUser = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser us = pojoUser.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		
		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), us);

		assertEquals(production.getId(), us.getDepartmentId());

		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserId(us.getId());
		assertEquals(3, roles.size());
		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), roles.get(0));
		jdbcUserTestUtil.verifyRole(user.getRoleName(), roles.get(1));
		jdbcUserTestUtil.verifyRole(worker.getRoleName(), roles.get(2));

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(us.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(addresses.get(0).getStateId(), italy.getId());
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(addresses.get(1).getStateId(), ireland.getId());

		// update the user
		User carl = User.newInstance().withId(us.getId()).withAccountName("carl123").withFirstName("Carlo")
				.withLastName("Rossi").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(accounts.getId()).withName(accounts.getName()));

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(administrator.getId()).withRoleName(administrator.getRoleName());
		carl.addRole(role);

		// run the test
		updateUser.execute(carl);

		Optional<PojoUser> updated = jdbcQueryTestUtil.findUserByUserId(us.getId());
		PojoUser up = updated.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyUser("carl123", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), up);

		assertEquals(accounts.getId(), up.getDepartmentId());

		roles = jdbcQueryTestUtil.findRolesByUserId(us.getId());
		assertEquals(3, roles.size());
		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), roles.get(0));
		jdbcUserTestUtil.verifyRole(user.getRoleName(), roles.get(1));
		jdbcUserTestUtil.verifyRole(worker.getRoleName(), roles.get(2));

		addresses = jdbcQueryTestUtil.findAddressesByUserId(us.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(addresses.get(0).getStateId(), italy.getId());
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(addresses.get(1).getStateId(), ireland.getId());
	}
}
