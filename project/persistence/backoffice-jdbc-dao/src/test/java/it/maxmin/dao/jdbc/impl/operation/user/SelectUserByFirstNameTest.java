package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

class SelectUserByFirstNameTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserByFirstNameTest.class);
	private SelectUserByFirstName selectUserByFirstName;

	@Autowired
	SelectUserByFirstNameTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			SelectUserByFirstName selectUserByFirstName) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectUserByFirstName = selectUserByFirstName;
	}

}