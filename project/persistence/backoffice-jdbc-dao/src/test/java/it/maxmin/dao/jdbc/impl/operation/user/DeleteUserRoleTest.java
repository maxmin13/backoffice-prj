package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static it.maxmin.dao.jdbc.impl.constant.Role.WORKER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class DeleteUserRoleTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserRoleTest.class);
	private DeleteUserRole deleteUserRole;

	@Autowired
	DeleteUserRoleTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.deleteUserRole = new DeleteUserRole(dataSource);
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		Long userId = null;
		Long roleId = 1l;

		Throwable throwable = assertThrows(Throwable.class, () -> deleteUserRole.execute(userId, roleId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoRoleIdThrowsException() {

		LOGGER.info("running test executeWithNoRoleIdThrowsException");

		Long userId = 1l;
		Long roleId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> deleteUserRole.execute(userId, roleId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// find a user with a role
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(maxmin.getId());

		assertEquals(3, roles.size());

		PojoRole role1 = roles.stream().filter(each -> each.getName().equals(ADMINISTRATOR.getName())).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		roles.stream().filter(each -> each.getName().equals(USER.getName())).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		roles.stream().filter(each -> each.getName().equals(WORKER.getName())).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		// run the test
		deleteUserRole.execute(maxmin.getId(), role1.getId());

		roles = jdbcQueryTestUtil.selectRolesByUserId(maxmin.getId());

		assertEquals(2, roles.size());

		assertEquals(0, roles.stream().filter(each -> each.getName().equals(ADMINISTRATOR.getName())).count());

		roles.stream().filter(each -> each.getName().equals(USER.getName())).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		roles.stream().filter(each -> each.getName().equals(WORKER.getName())).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}
}