package it.maxmin.dao.jdbc;

import static it.maxmin.dao.jdbc.TestMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.TestMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.TestMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jdbc.impl.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static it.maxmin.dao.jdbc.impl.constant.Role.WORKER;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.model.jdbc.dao.pojo.PojoDepartment;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoState;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class BaseDaoTest {

	protected JdbcQueryTestUtil jdbcQueryTestUtil;
	protected JdbcUserTestUtil jdbcUserTestUtil;
	protected PojoDepartment legal;
	protected PojoDepartment accounts;
	protected PojoDepartment production;
	protected PojoState italy;
	protected PojoState ireland;
	protected PojoRole administrator;
	protected PojoRole user;
	protected PojoRole worker;

	@Autowired
	protected BaseDaoTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.jdbcUserTestUtil = jdbcUserTestUtil;

		String[] scripts = { "1_create_database.up.sql", "2_role.up.sql", "2_state.up.sql", "2_department.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		this.legal = jdbcQueryTestUtil.findDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new DaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.accounts = jdbcQueryTestUtil.findDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new DaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.production = jdbcQueryTestUtil.findDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new DaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.italy = jdbcQueryTestUtil.findStateByName(ITALY.getName())
				.orElseThrow(() -> new DaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.ireland = jdbcQueryTestUtil.findStateByName(IRELAND.getName())
				.orElseThrow(() -> new DaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.administrator = jdbcQueryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName())
				.orElseThrow(() -> new DaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.worker = jdbcQueryTestUtil.findRoleByRoleName(WORKER.getRoleName())
				.orElseThrow(() -> new DaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.user = jdbcQueryTestUtil.findRoleByRoleName(USER.getRoleName())
				.orElseThrow(() -> new DaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_department.down.sql", "2_role.down.sql", "1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

}
