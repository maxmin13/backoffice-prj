package it.maxmin.dao.jpa;

import static it.maxmin.dao.jpa.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jpa.constant.Department.LEGAL;
import static it.maxmin.dao.jpa.constant.Department.PRODUCTION;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jpa.constant.Role.USER;
import static it.maxmin.dao.jpa.constant.Role.WORKER;
import static it.maxmin.dao.jpa.constant.State.IRELAND;
import static it.maxmin.dao.jpa.constant.State.ITALY;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoRole;
import it.maxmin.model.jpa.dao.pojo.PojoState;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = { "classpath:database/1_create_database.up.sql", "classpath:database/2_role.up.sql",
		"classpath:database/2_state.up.sql",
		"classpath:database/2_department.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/2_state.down.sql", "classpath:database/2_department.down.sql",
		"classpath:database/2_role.down.sql",
		"classpath:database/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public abstract class JpaBaseTestDao {

	protected JpaQueryTestUtil jdbcQueryTestUtil;
	protected JpaUserTestUtil userTestUtil;
	protected PojoDepartment legal;
	protected PojoDepartment accounts;
	protected PojoDepartment production;
	protected PojoState italy;
	protected PojoState ireland;
	protected PojoRole administrator;
	protected PojoRole user;
	protected PojoRole worker;

	@Autowired
	protected JpaBaseTestDao(JpaQueryTestUtil jdbcQueryTestUtil, JpaUserTestUtil userTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.userTestUtil = userTestUtil;

		this.legal = jdbcQueryTestUtil.selectDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.accounts = jdbcQueryTestUtil.selectDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.production = jdbcQueryTestUtil.selectDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.italy = jdbcQueryTestUtil.selectStateByName(ITALY.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.ireland = jdbcQueryTestUtil.selectStateByName(IRELAND.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.administrator = jdbcQueryTestUtil.selectRoleByName(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.worker = jdbcQueryTestUtil.selectRoleByName(WORKER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.user = jdbcQueryTestUtil.selectRoleByName(USER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}

}
