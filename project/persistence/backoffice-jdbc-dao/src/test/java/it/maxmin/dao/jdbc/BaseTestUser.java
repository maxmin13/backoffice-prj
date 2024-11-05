package it.maxmin.dao.jdbc;

import static it.maxmin.dao.jdbc.impl.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jdbc.impl.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static it.maxmin.dao.jdbc.impl.constant.UserRole.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.model.jdbc.domain.pojo.PojoDepartment;
import it.maxmin.model.jdbc.domain.pojo.PojoState;
import it.maxmin.model.jdbc.domain.pojo.PojoUserRole;

@SpringJUnitConfig(classes = { JdbcUnitTestContextCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class BaseTestUser {

	protected JdbcQueryTestUtil jdbcQueryTestUtil;
	protected JdbcUserTestUtil jdbcUserTestUtil;
	protected PojoDepartment legal;
	protected PojoDepartment accounts;
	protected PojoDepartment production;
	protected PojoState italy;
	protected PojoState ireland;
	protected PojoUserRole administrator;
	protected PojoUserRole user;
	protected PojoUserRole worker;

	@Autowired
	protected BaseTestUser(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.jdbcUserTestUtil = jdbcUserTestUtil;

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		this.legal = jdbcQueryTestUtil.findDepartmentByName(LEGAL.getName());
		this.accounts = jdbcQueryTestUtil.findDepartmentByName(ACCOUNTS.getName());
		this.production = jdbcQueryTestUtil.findDepartmentByName(PRODUCTION.getName());
		this.italy = jdbcQueryTestUtil.findStateByName(ITALY.getName());
		this.ireland = jdbcQueryTestUtil.findStateByName(IRELAND.getName());
		this.administrator = jdbcQueryTestUtil.findRoleByName(ADMINISTRATOR.getRoleName());
		this.worker = jdbcQueryTestUtil.findRoleByName(WORKER.getRoleName());
		this.user = jdbcQueryTestUtil.findRoleByName(USER.getRoleName());
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_department.down.sql", "2_userrole.down.sql", "1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}
}
