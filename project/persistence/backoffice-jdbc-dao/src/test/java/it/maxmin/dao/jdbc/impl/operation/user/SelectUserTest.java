package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

public class SelectUserTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserTest.class);
	SelectUser selectUser;

	@Autowired
	SelectUserTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, SelectUser selectUser) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectUser = selectUser;
	}

}