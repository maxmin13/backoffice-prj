package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
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
import it.maxmin.dao.jdbc.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoDepartment;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class InsertUserTest extends JdbcBaseTestDao {

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
	void executeWithNoDepartmentThrowsException() {

		LOGGER.info("running test executeWithNoDepartmentThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi");

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(irelandState.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withId(administratorRole.getId()).withName(administratorRole.getName());
		carl.addRole(role);

		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
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

		Throwable throwable = assertThrows(Throwable.class, () -> insertUser.execute(carl));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// delete all users
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_useraddress.down.sql",
				"2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance()
						.withId(accountingDepartment.getId()).withName(accountingDepartment.getName()));

		Address address = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(irelandState.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address);

		Role role = Role.newInstance().withName(administratorRole.getName());
		carl.addRole(role);

		// run the test
		User user = insertUser.execute(carl);

		jdbcUserTestUtil.verifyUser("carl23", user);

		PojoUser us = jdbcQueryTestUtil.selectUserByUserId(user.getId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), us);
		assertEquals(0, us.getVersion());

		PojoDepartment department = jdbcQueryTestUtil.selectDepartmentById(us.getDepartmentId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyDepartment(accountingDepartment.getName(), department);

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(us.getId());

		assertEquals(0, addresses.size());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(user.getId());

		assertEquals(0, roles.size());
	}
}
