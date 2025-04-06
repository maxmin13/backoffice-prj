package it.maxmin.dao.jdbc.ut.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.config.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;
import it.maxmin.dao.jdbc.util.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.util.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class UpdateUserTest extends JdbcBaseTestDao {

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

		assertThrows(IllegalArgumentException.class, () -> updateUser.execute(user));
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance()
						.withId(accountingDepartment.getId()).withName(accountingDepartment.getName()));

		// run the test
		assertThrows(IllegalArgumentException.class, () -> updateUser.execute(carl));
	}

	// TODO ?????? @Test
	void executeWithNoVersionThrowsException() {

		LOGGER.info("running test executeWithNoVersionThrowsException");

		User carl = User.newInstance().withId(3l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance()
						.withId(accountingDepartment.getId()).withName(accountingDepartment.getName()));

		// run the test
		assertThrows(IllegalArgumentException.class, () -> updateUser.execute(carl));
	}

	@Test
	void executeWithNoDepartmentThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withVersion(2);

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(irelandState.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(administratorRole.getId()).withName(administratorRole.getName());
		carl.addRole(role);

		assertThrows(IllegalArgumentException.class, () -> updateUser.execute(carl));
	}

	@Test
	void executeWithNoDepartmentIdThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentIdThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withName(accountingDepartment.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(irelandState.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(administratorRole.getId()).withName(administratorRole.getName());
		carl.addRole(role);

		assertThrows(IllegalArgumentException.class, () -> updateUser.execute(carl));
	}

	@Test
	void execute() {

		LOGGER.info("running test executeWithNoVersion");

		// Find an existing user
		PojoUser user = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user);
		Integer initialVersion = user.getVersion();

		assertEquals(productionDepartment.getId(), user.getDepartmentId());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(user.getId());
		assertEquals(3, roles.size());
		jdbcUserTestUtil.verifyRole(administratorRole.getName(), roles.get(0));
		jdbcUserTestUtil.verifyRole(userRole.getName(), roles.get(1));
		jdbcUserTestUtil.verifyRole(workerRole.getName(), roles.get(2));

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(user.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(addresses.get(0).getStateId(), italyState.getId());
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(addresses.get(1).getStateId(), irelandState.getId());

		// update the user
		User carl = User.newInstance().withId(user.getId()).withVersion(user.getVersion()).withAccountName("carl123")
				.withFirstName("Carlo").withLastName("Rossi").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(accountingDepartment.getId())
						.withName(accountingDepartment.getName()));

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(administratorRole.getId()).withName(administratorRole.getName());
		carl.addRole(role);

		// run the test
		updateUser.execute(carl);

		PojoUser updated = jdbcQueryTestUtil.selectUserByUserId(user.getId()).orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyUser("carl123", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), updated);
		assertEquals((initialVersion + 1), updated.getVersion());

		assertEquals(accountingDepartment.getId(), updated.getDepartmentId());

		roles = jdbcQueryTestUtil.selectRolesByUserId(user.getId());
		assertEquals(3, roles.size());
		jdbcUserTestUtil.verifyRole(administratorRole.getName(), roles.get(0));
		jdbcUserTestUtil.verifyRole(userRole.getName(), roles.get(1));
		jdbcUserTestUtil.verifyRole(workerRole.getName(), roles.get(2));

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(user.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(addresses.get(0).getStateId(), italyState.getId());
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(addresses.get(1).getStateId(), irelandState.getId());
	}
}
