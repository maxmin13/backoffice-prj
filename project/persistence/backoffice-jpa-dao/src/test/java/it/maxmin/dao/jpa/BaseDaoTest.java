package it.maxmin.dao.jpa;

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
public abstract class BaseDaoTest {

	protected QueryTestUtil jdbcQueryTestUtil;
	protected UserTestUtil userTestUtil;
	protected PojoDepartment legal;
	protected PojoDepartment accounts;
	protected PojoDepartment production;
	protected PojoState italy;
	protected PojoState ireland;
	protected PojoRole administrator;
	protected PojoRole user;
	protected PojoRole worker;

	@Autowired
	protected BaseDaoTest(QueryTestUtil jdbcQueryTestUtil, UserTestUtil userTestUtil) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.userTestUtil = userTestUtil;

		this.legal = jdbcQueryTestUtil.findDepartmentByName(LEGAL.getName())
				.orElseThrow(() -> new DaoTestException("Error department not found"));
		this.accounts = jdbcQueryTestUtil.findDepartmentByName(ACCOUNTS.getName())
				.orElseThrow(() -> new DaoTestException("Error department not found"));
		this.production = jdbcQueryTestUtil.findDepartmentByName(PRODUCTION.getName())
				.orElseThrow(() -> new DaoTestException("Error department not found"));
		this.italy = jdbcQueryTestUtil.findStateByName(ITALY.getName())
				.orElseThrow(() -> new DaoTestException("Error state not found"));
		this.ireland = jdbcQueryTestUtil.findStateByName(IRELAND.getName())
				.orElseThrow(() -> new DaoTestException("Error state not found"));
		this.administrator = jdbcQueryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName())
				.orElseThrow(() -> new DaoTestException("Error role not found"));
		this.worker = jdbcQueryTestUtil.findRoleByRoleName(WORKER.getRoleName())
				.orElseThrow(() -> new DaoTestException("Error role not found"));
		this.user = jdbcQueryTestUtil.findRoleByRoleName(USER.getRoleName())
				.orElseThrow(() -> new DaoTestException("Error role not found"));
	}

}
