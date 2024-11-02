package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

class InsertUserAddressTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserAddressTest.class);
	private InsertUserAddress insertUserAddress;

	@Autowired
	InsertUserAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			InsertUserAddress insertUserAddress) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUserAddress = insertUserAddress;
	}

}
