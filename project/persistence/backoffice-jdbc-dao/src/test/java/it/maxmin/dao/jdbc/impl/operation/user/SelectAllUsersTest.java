package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

public class SelectAllUsersTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAllUsersTest.class);
	SelectAllUsers selectAllUsers;

	@Autowired
	SelectAllUsersTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			SelectAllUsers selectAllUsers) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectAllUsers = selectAllUsers;
	}

}
