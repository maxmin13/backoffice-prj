package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

public class SelectUserByAccountNameTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUserByAccountNameTest.class);
	SelectUserByAccountName selectUserByAccountName;

	@Autowired
	SelectUserByAccountNameTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			SelectUserByAccountName selectUserByAccountName) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectUserByAccountName = selectUserByAccountName;
	}

}
