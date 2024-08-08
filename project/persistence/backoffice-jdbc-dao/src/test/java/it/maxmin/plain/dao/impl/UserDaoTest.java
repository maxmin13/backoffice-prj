package it.maxmin.plain.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static it.maxmin.plain.dao.DaoTestUtil.runScripts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.DaoTestException;
import it.maxmin.plain.dao.EmbeddedJdbcTestCfg;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private UserDaoImpl userDao;

	@BeforeAll
	public static void setup() {
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		String[] scripts = { "create_tables.sql" };
		runScripts(scripts, jdbcTemplate);
	}

	@BeforeEach
	public void init() {
		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);
		String[] scripts = { "insert_roles.sql", "insert_users.sql" };
		runScripts(scripts, jdbcTemplate);
	}

	@AfterEach
	public void cleanUp() {
		String[] scripts = { "delete_users.sql", "delete_roles.sql"};
		runScripts(scripts, jdbcTemplate);
	}

	@AfterAll
	public static void clean() {
		String[] scripts = { "drop_tables.sql" };
		runScripts(scripts, jdbcTemplate);
	}

	@Test
	public void nullCreateThrowsException() {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});
		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void testCreate() throws SQLException {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

//		User user = new User();
//		user.setAccountName("maxmin");
//		user.setBirthDate(LocalDate.of(1988, 1, 13));
//		user.setCreatedDate(LocalDateTime.of(2024, 1, 1, 1, 1));
//		user.setFirstName("MaxMin");
//		user.setLastName("Min");

//		String u = userDao.findByAccountName("maxmin13");
		List<User> us = userDao.findAll();

		for (User u : us) {
			System.out.println(u.getAccountName());
			System.out.println(u.getBirthDate());
			System.out.println(u.getCreatedDate());
		}
//		User created = userDao.create(user);
//		
//		assertEquals(2l, created.getUserId());
//		assertEquals("maxmin", created.getAccountName());
//		assertEquals(LocalDate.of(1988, 1, 13), created.getBirthDate());
//		assertEquals(LocalDateTime.of(2024, 1, 1, 1, 1), created.getCreateDate());
//		assertEquals("MaxMin", created.getFirstName());
//		assertEquals("Min", created.getLastName());
	}

}
