package it.maxmin.dao.jdbc.impl.operation.address;

import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

class SelectAddressHelperTest extends BaseTestUser {

	private SelectAddressHelper selectAddressHelper;

	@Autowired
	SelectAddressHelperTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectAddressHelper = new SelectAddressHelper();
	}

}
