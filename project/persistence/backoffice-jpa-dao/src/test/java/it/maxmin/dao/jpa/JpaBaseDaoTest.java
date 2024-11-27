package it.maxmin.dao.jpa;

import static it.maxmin.dao.jpa.JpaTestMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.JpaTestMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.JpaTestMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jpa.constant.Department.LEGAL;
import static it.maxmin.dao.jpa.constant.Department.PRODUCTION;
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

import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoRole;
import it.maxmin.model.jpa.dao.pojo.PojoState;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public abstract class JpaBaseDaoTest {

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
	protected JpaBaseDaoTest(JpaQueryTestUtil jdbcQueryTestUtil, JpaUserTestUtil userTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.userTestUtil = userTestUtil;

		this.legal = jdbcQueryTestUtil.findDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.accounts = jdbcQueryTestUtil.findDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.production = jdbcQueryTestUtil.findDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		this.italy = jdbcQueryTestUtil.findStateByName(ITALY.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.ireland = jdbcQueryTestUtil.findStateByName(IRELAND.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		this.administrator = jdbcQueryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.worker = jdbcQueryTestUtil.findRoleByRoleName(WORKER.getRoleName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		this.user = jdbcQueryTestUtil.findRoleByRoleName(USER.getRoleName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}

}