package it.maxmin.plain.dao.impl;

import static it.maxmin.plain.dao.DaoTestUtil.findUserByUserId;
import static it.maxmin.plain.dao.DaoTestUtil.insertUser;
import static it.maxmin.plain.dao.DaoTestUtil.runDBScripts;
import static it.maxmin.plain.dao.DaoTestUtil.stopTestDB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.EmbeddedJdbcTestCfg;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static MariaDB4jSpringService mariaDB4jSpringService;
	private UserDaoImpl userDao;

	@BeforeAll
	public static void setup() {
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		mariaDB4jSpringService = springJdbcCtx.getBean("mariaDB4jSpringService", MariaDB4jSpringService.class);
	}

	@BeforeEach
	public void init() {
		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);
		String[] scripts = { "create_tables.sql", "insert_roles.sql", "insert_users.sql" };
		runDBScripts(scripts, jdbcTemplate);
	}

	@AfterEach
	public void cleanUp() {
		String[] scripts = { "delete_users.sql", "delete_roles.sql", "drop_tables.sql" };
		runDBScripts(scripts, jdbcTemplate);
	}
	
	@AfterAll
	public static void clear() {
		stopTestDB(mariaDB4jSpringService);
	}

	@Test
	public void testFindAll() throws SQLException {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		List<User> users = userDao.findAll();

		assertTrue(users.size() == 2);

		User first = users.get(0);

		assertEquals("maxmin13", first.getAccountName());
		assertEquals("Max", first.getFirstName());
		assertEquals("Minardi", first.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), first.getBirthDate());
		assertNotNull(first.getCreatedDate());
		assertNotNull(first.getUserId());

		User second = users.get(1);
		assertEquals("artur", second.getAccountName());
		assertEquals("art", second.getFirstName());
		assertEquals("artur", second.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), second.getBirthDate());
		assertNotNull(second.getCreatedDate());
		assertNotNull(second.getUserId());
	}

	@Test
	public void findByAccountName() throws SQLException {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = userDao.findByAccountName("maxmin13");

		assertEquals("maxmin13", user.getAccountName());
		assertEquals("Max", user.getFirstName());
		assertEquals("Minardi", user.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), user.getBirthDate());
		assertNotNull(user.getCreatedDate());
		assertNotNull(user.getUserId());
	}

	@Test
	public void findByFirstName() throws SQLException {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		List<User> users = userDao.findByFirstName("art");

		assertTrue(users.size() == 1);

		User second = users.get(0);

		assertEquals("artur", second.getAccountName());
		assertEquals("art", second.getFirstName());
		assertEquals("artur", second.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), second.getBirthDate());
		assertNotNull(second.getCreatedDate());
		assertNotNull(second.getUserId());
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
	public void create() {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		User newUser = userDao.create(user);
		
		assertEquals("franc", newUser.getAccountName());
		assertEquals("Franco", newUser.getFirstName());
		assertEquals("Red", newUser.getLastName());
		assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		assertNull(newUser.getCreatedDate());
		assertNotNull(newUser.getUserId());
		
		User created = findUserByUserId(newUser.getUserId(), jdbcTemplate);
		assertNotNull(created.getCreatedDate());
	}

	@Test
	public void nullUpdateThrowsException() {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	public void update() {

		userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = new User();
		user.setAccountName("reg");
		user.setBirthDate(LocalDate.of(1983, 12, 13));
		user.setFirstName("Reginald");
		user.setLastName("Regi");

		long userId = insertUser(user, jdbcTemplate).getUserId();
		LocalDateTime createdDate = findUserByUserId(userId, jdbcTemplate).getCreatedDate();
		
		user = new User();
		user.setUserId(userId);
		user.setAccountName("regUpdated");
		user.setBirthDate(LocalDate.of(1984, 9, 1));
		user.setFirstName("ReginaldUpdated");
		user.setLastName("RegiUpdated");

		userDao.update(user);
		
		User updated = findUserByUserId(userId, jdbcTemplate);
		
		assertEquals("regUpdated", updated.getAccountName());
		assertEquals("ReginaldUpdated", updated.getFirstName());
		assertEquals("RegiUpdated", updated.getLastName());
		assertEquals(LocalDate.of(1984, 9, 1), updated.getBirthDate());
		assertEquals(createdDate, updated.getCreatedDate());
	}
}
