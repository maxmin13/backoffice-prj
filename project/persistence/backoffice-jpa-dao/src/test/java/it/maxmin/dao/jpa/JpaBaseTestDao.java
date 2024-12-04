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

import it.maxmin.dao.jpa.exception.JpaDaoTestException;
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
	protected PojoDepartment legalDepartment;
	protected PojoDepartment accountsDepartment;
	protected PojoDepartment productionDepartment;
	protected PojoState italyState;
	protected PojoState irelandState;
	protected PojoRole administratorRole;
	protected PojoRole userRole;
	protected PojoRole workerRole;

	@Autowired
	protected JpaBaseTestDao(JpaQueryTestUtil jdbcQueryTestUtil, JpaUserTestUtil userTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.userTestUtil = userTestUtil;

		this.legalDepartment = jdbcQueryTestUtil.selectDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.accountsDepartment = jdbcQueryTestUtil.selectDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.productionDepartment = jdbcQueryTestUtil.selectDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.italyState = jdbcQueryTestUtil.selectStateByName(ITALY.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.irelandState = jdbcQueryTestUtil.selectStateByName(IRELAND.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.administratorRole = jdbcQueryTestUtil.selectRoleByName(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.workerRole = jdbcQueryTestUtil.selectRoleByName(WORKER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.userRole = jdbcQueryTestUtil.selectRoleByName(USER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}

}
