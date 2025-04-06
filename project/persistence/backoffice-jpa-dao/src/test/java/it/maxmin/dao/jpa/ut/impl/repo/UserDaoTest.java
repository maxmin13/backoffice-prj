package it.maxmin.dao.jpa.ut.impl.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.dao.jpa.ut.config.JpaDaoSpringContextUnitTestCfg;

@SpringJUnitConfig(classes = { JpaDaoSpringContextUnitTestCfg.class })
class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	UserDao userDao;

	@Autowired
	public UserDaoTest(UserDao userDao) {
		this.userDao = userDao;
	}

	@Test
	@Order(1)
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
//		List<User> users = userDao.findAll();

//		assertEquals(0, users.size());
	}

	@Test
	@Order(2)
//	@Sql(scripts = { "classpath:database/down/2_userrole.down.sql", "classpath:database/down/2_useraddress.down.sql",
//	"classpath:database/down/2_user.down.sql",
//	"classpath:database/down/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// TODO
	}

}
