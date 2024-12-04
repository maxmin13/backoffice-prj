package it.maxmin.dao.jdbc;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
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
public abstract class JdbcBaseTestDao {

	protected JdbcQueryTestUtil jdbcQueryTestUtil;
	protected JdbcUserTestUtil jdbcUserTestUtil;
	protected PojoDepartment legalDepartment;
	protected PojoDepartment accountsDepartment;
	protected PojoDepartment productionDepartment;
	protected PojoState italyState;
	protected PojoState irelandState;
	protected PojoRole administratorRole;
	protected PojoRole userRole;
	protected PojoRole workerRole;

	@Autowired
	protected JdbcBaseTestDao(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.jdbcUserTestUtil = jdbcUserTestUtil;

		String[] scripts = { "1_create_database.up.sql", "2_role.up.sql", "2_state.up.sql", "2_department.up.sql",
				"2_transactiontype.up.sql", "2_accountstatus.up.sql", "2_accounttype.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		this.legalDepartment = jdbcQueryTestUtil.selectDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.accountsDepartment = jdbcQueryTestUtil.selectDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.productionDepartment = jdbcQueryTestUtil.selectDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.italyState = jdbcQueryTestUtil.selectStateByName(ITALY.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.irelandState = jdbcQueryTestUtil.selectStateByName(IRELAND.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.administratorRole = jdbcQueryTestUtil.selectRoleByName(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.workerRole = jdbcQueryTestUtil.selectRoleByName(WORKER.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.userRole = jdbcQueryTestUtil.selectRoleByName(USER.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql", "2_account.up.sql", "2_transaction.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void destroy() {
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_transactiontype.down.sql",
				"2_accountstatus.down.sql", "2_accounttype.down.sql", "2_useraddress.down.sql", "2_user.down.sql",
				"2_address.down.sql", "2_state.down.sql", "2_department.down.sql", "2_role.down.sql",
				"1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

}
