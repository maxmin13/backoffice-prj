package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

public class InsertUserTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserTest.class);
	InsertUser insertUser;

	@Autowired
	InsertUserTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			InsertUser insertUser) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUser = insertUser;
	}

}
