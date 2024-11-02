package it.maxmin.dao.jdbc.impl.operation.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;

class UpdateUserTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserTest.class);
	private UpdateUser updateUser;

	@Autowired
	UpdateUserTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			UpdateUser updateUser) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.updateUser = updateUser;
	}

}
