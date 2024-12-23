package it.maxmin.dao.jpa.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jpa.JpaBaseTestDao;
import it.maxmin.dao.jpa.JpaDaoSpringContextTestCfg;
import it.maxmin.dao.jpa.JpaQueryTestUtil;
import it.maxmin.dao.jpa.JpaUserTestUtil;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.model.jpa.dao.entity.User;

@SpringJUnitConfig(classes = { JpaDaoSpringContextTestCfg.class })
class UserDaoTest extends JpaBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	UserDao userDao;

	@Autowired
	public UserDaoTest(JpaQueryTestUtil jdbcQueryTestUtil, JpaUserTestUtil jdbcUserTestUtil, UserDao userDao) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.userDao = userDao;
	}

	@Test
	@Order(1)
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(0, users.size());
	}

	@Test
	@Order(2)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// TODO
	}

}
