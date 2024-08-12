package it.maxmin.plain.dao.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import it.maxmin.model.plain.pojos.Address;
import it.maxmin.model.plain.pojos.State;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.DaoTestUtil;
import it.maxmin.plain.dao.EmbeddedJdbcTestCfg;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DaoTestUtil daoTestUtil;

	@BeforeAll
	public static void setup() {
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@BeforeEach
	public void init() {
		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_address.up.sql",
				"2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	public void cleanUp() {
		String[] scripts = { "2_user.down.sql", "2_address.down.sql", "2_state.down.sql", "2_userrole.down.sql",
				"1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	public static void clear() {
		daoTestUtil.stopTestDB();
	}

	@Test
	public void testFindAll() {

		UserDaoImpl userDao = new UserDaoImpl();
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
	public void testFindAllNotFound() {

		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		List<User> users = userDao.findAll();

		assertTrue(users.size() == 0);
	}

	@Test
	public void findByAccountName() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Optional<User> user = userDao.findByAccountName("maxmin13");

		assertEquals("maxmin13", user.get().getAccountName());
		assertEquals("Max", user.get().getFirstName());
		assertEquals("Minardi", user.get().getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), user.get().getBirthDate());
		assertNotNull(user.get().getCreatedDate());
		assertNotNull(user.get().getUserId());
	}

	@Test
	public void findByAccountNameNotFound() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Optional<User> user = userDao.findByAccountName("franz");

		assertTrue(user.isEmpty());
	}

	@Test
	public void findByFirstName() {

		UserDaoImpl userDao = new UserDaoImpl();
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
	public void findByFirstNameNotFound() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		List<User> users = userDao.findByFirstName("franco");

		assertTrue(users.size() == 0);
	}

	@Test
	public void nullCreateThrowsException() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void create_with_no_address() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		userDao.create(user);

		/*
		 * assertEquals("franc", newUser.getAccountName()); assertEquals("Franco",
		 * newUser.getFirstName()); assertEquals("Red", newUser.getLastName());
		 * assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		 * assertNull(newUser.getCreatedDate()); assertNotNull(newUser.getUserId());
		 * assertTrue(newUser.getAddresses().size() == 0);
		 * 
		 * User created = daoTestUtil.findUserByUserId(newUser.getUserId());
		 * assertNotNull(created.getCreatedDate());
		 */
	}

	@Test
	public void create_with_addresses() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		State state1 = daoTestUtil.findStateByName("Italy");

		Address address1 = new Address();
		address1.setAddress("Via Nuova");
		address1.setCity("Venice");
		address1.setStateId(state1.getStateId());
		address1.setRegion("Veneto");
		address1.setPostalCode("30033");
		user.addAddress(address1);

		State state2 = daoTestUtil.findStateByName("Ireland");

		Address address2 = new Address();
		address2.setAddress("Via Vecchia");
		address2.setCity("Padova");
		address2.setStateId(state2.getStateId());
		address2.setRegion("Romagna");
		address2.setPostalCode("32133");
		user.addAddress(address2);

		userDao.create(user);

		/*
		 * assertEquals("franc", newUser.getAccountName()); assertEquals("Franco",
		 * newUser.getFirstName()); assertEquals("Red", newUser.getLastName());
		 * assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		 * assertNull(newUser.getCreatedDate()); assertNotNull(newUser.getUserId());
		 * assertTrue(newUser.getAddresses().size() == 2);
		 * 
		 * User created = daoTestUtil.findUserByUserId(newUser.getUserId());
		 * assertNotNull(created.getCreatedDate());
		 * 
		 * Address newAddress1 = user.getAddresses().get(0);
		 * 
		 * assertEquals("Via Nuova", newAddress1.getAddress()); assertEquals("Venice",
		 * newAddress1.getCity()); assertEquals(state1.getStateId(),
		 * newAddress1.getStateId()); assertEquals("Veneto", newAddress1.getRegion());
		 * assertEquals("30033", newAddress1.getPostalCode());
		 * 
		 * Address newAddress2 = user.getAddresses().get(1);
		 * 
		 * assertEquals("Via Vecchia", newAddress2.getAddress()); assertEquals("Padova",
		 * newAddress2.getCity()); assertEquals(state2.getStateId(),
		 * newAddress2.getStateId()); assertEquals("Romagna", newAddress2.getRegion());
		 * assertEquals("32133", newAddress2.getPostalCode());
		 */
	}

	@Test
	public void nullUpdateThrowsException() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void update() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(jdbcTemplate);

		User user = new User();
		user.setAccountName("reg");
		user.setBirthDate(LocalDate.of(1983, 12, 13));
		user.setFirstName("Reginald");
		user.setLastName("Regi");

		long userId = daoTestUtil.insertUser(user).getUserId();
		LocalDateTime createdDate = daoTestUtil.findUserByUserId(userId).getCreatedDate();

		user = new User();
		user.setUserId(userId);
		user.setAccountName("regUpdated");
		user.setBirthDate(LocalDate.of(1984, 9, 1));
		user.setFirstName("ReginaldUpdated");
		user.setLastName("RegiUpdated");

		userDao.update(user);

		User updated = daoTestUtil.findUserByUserId(userId);

		assertEquals("regUpdated", updated.getAccountName());
		assertEquals("ReginaldUpdated", updated.getFirstName());
		assertEquals("RegiUpdated", updated.getLastName());
		assertEquals(LocalDate.of(1984, 9, 1), updated.getBirthDate());
		assertEquals(createdDate, updated.getCreatedDate());
	}
}
