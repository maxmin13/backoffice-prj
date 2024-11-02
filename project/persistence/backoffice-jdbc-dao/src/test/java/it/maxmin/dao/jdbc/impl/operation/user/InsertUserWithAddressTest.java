package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

public class InsertUserWithAddressTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserWithAddressTest.class);
	InsertUserWithAddress insertUserWithAddress;

	@Autowired
	InsertUserWithAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			InsertUserWithAddress insertUserWithAddress) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUserWithAddress = insertUserWithAddress;
	}

}
