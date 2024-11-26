package it.maxmin.dao.jdbc.impl.operation.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseDaoTest;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.JdbcDaoUnitTestContextCfg;
import it.maxmin.model.jdbc.dao.entity.Role;

@SpringJUnitConfig(classes = { JdbcDaoUnitTestContextCfg.class })
class SelectRoleByRoleNameTest extends JdbcBaseDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectRoleByRoleNameTest.class);
	private SelectRoleByRoleName selectRoleByRoleName;

	@Autowired
	SelectRoleByRoleNameTest(NamedParameterJdbcTemplate jdbcTemplate, JdbcQueryTestUtil jdbcQueryTestUtil,
			JdbcUserTestUtil jdbcUserTestUtil) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectRoleByRoleName = new SelectRoleByRoleName(jdbcTemplate);
	}

	@Test
	void executeWithNoRoleName() {

		LOGGER.info("running test executeWithNoRoleName");

		String roleName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> selectRoleByRoleName.execute(roleName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeNotFound() {

		LOGGER.info("running test executeNotFound");

		// delete all roles
		String[] scripts = { "2_userrole.down.sql", "2_role.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		Role role = selectRoleByRoleName.execute(administrator.getRoleName());

		assertNull(role);
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// run the test
		Role role = selectRoleByRoleName.execute(administrator.getRoleName());

		this.jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role);
	}

}
