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
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.BaseDaoTest;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.UnitTestContextCfg;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { UnitTestContextCfg.class })
class InsertUserRoleTest extends BaseDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserRoleTest.class);
	private InsertUserRole insertUserRole;

	@Autowired
	InsertUserRoleTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUserRole = new InsertUserRole(dataSource);
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		Long userId = null;
		Long roleId = 1l;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserRole.execute(userId, roleId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoRoleIdThrowsException() {

		LOGGER.info("running test executeWithNoRoleIdThrowsException");

		Long userId = 1l;
		Long roleId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserRole.execute(userId, roleId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		List<PojoUser> users = jdbcQueryTestUtil.findUsersByRoleName(worker.getRoleName());

		assertEquals(1, users.size());

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), users.get(0));

		PojoUser franco = PojoUser.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartmentId(legal.getId());

		PojoUser newUser = jdbcQueryTestUtil.createUser(franco);

		// run the test
		insertUserRole.execute(newUser.getId(), worker.getId());

		users = jdbcQueryTestUtil.findUsersByRoleName(worker.getRoleName());

		assertEquals(2, users.size());

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), users.get(0));
		jdbcUserTestUtil.verifyUser("franc", "Franco", "Red", LocalDate.of(1981, 11, 12), users.get(1));
	}
}