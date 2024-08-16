package it.maxmin.dao.jdbc.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

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

import it.maxmin.dao.jdbc.DaoTestUtil;
import it.maxmin.dao.jdbc.JdbcTestCfg;
import it.maxmin.model.jdbc.Address;
import it.maxmin.model.jdbc.State;
import it.maxmin.model.jdbc.User;
import it.maxmin.model.jdbc.UserAddress;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DataSource dataSource;
	private static DaoTestUtil daoTestUtil;

	@BeforeAll
	static void setup() {
		LOGGER.info("Running UserDaoTest tests");
		springJdbcCtx = new AnnotationConfigApplicationContext(JdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@BeforeEach
	void init() {
		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_address.up.sql",
				"2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_userrole.down.sql", "1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	static void clear() {
		daoTestUtil.stopTestDB();
	}

	@Test
	void testFindAllNotFound() {

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(0, users.size());
	}

	@Test
	void testFindAll() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(3, users.size());

		User maxmin = users.get(0);

		assertEquals("maxmin13", maxmin.getAccountName());
		assertEquals("Max", maxmin.getFirstName());
		assertEquals("Minardi", maxmin.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), maxmin.getBirthDate());
		assertNotNull(maxmin.getCreatedDate());
		assertNotNull(maxmin.getId());

		assertEquals(3, maxmin.getAddresses().size());

		State ireland = daoTestUtil.findStateByName("Ireland");
		State italy = daoTestUtil.findStateByName("Italy");

		assertEquals("Via borgo di sotto", maxmin.getAddresses().get(0).getDescription());
		assertEquals(italy.getId(), maxmin.getAddresses().get(0).getStateId());

		assertEquals("Via Roma", maxmin.getAddresses().get(1).getDescription());
		assertEquals(italy.getId(), maxmin.getAddresses().get(1).getStateId());

		assertEquals("Connolly street", maxmin.getAddresses().get(2).getDescription());
		assertEquals(ireland.getId(), maxmin.getAddresses().get(2).getStateId());

		User artur = users.get(1);

		assertEquals("artur", artur.getAccountName());
		assertEquals(1, artur.getAddresses().size());

		User reginald = users.get(2);

		assertEquals("reginald123", reginald.getAccountName());
		assertEquals(0, reginald.getAddresses().size());
	}

	@Test
	void findByAccountNameNotFound() {

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		Optional<User> user = userDao.findByAccountName("maxmin13");

		assertTrue(user.isEmpty());
	}

	@Test
	void findByAccountName() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		User maxmin = userDao.findByAccountName("maxmin13").get();

		assertEquals("maxmin13", maxmin.getAccountName());
		assertEquals("Max", maxmin.getFirstName());
		assertEquals("Minardi", maxmin.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), maxmin.getBirthDate());
		assertNotNull(maxmin.getCreatedDate());
		assertNotNull(maxmin.getId());

		assertEquals(3, maxmin.getAddresses().size());

		State ireland = daoTestUtil.findStateByName("Ireland");
		State italy = daoTestUtil.findStateByName("Italy");

		assertEquals("Via borgo di sotto", maxmin.getAddresses().get(0).getDescription());
		assertEquals("Rome", maxmin.getAddresses().get(0).getCity());
		assertEquals(italy.getId(), maxmin.getAddresses().get(0).getStateId());
		assertEquals("Lazio", maxmin.getAddresses().get(0).getRegion());
		assertEquals("30010", maxmin.getAddresses().get(0).getPostalCode());

		assertEquals("Via Roma", maxmin.getAddresses().get(1).getDescription());
		assertEquals("Venice", maxmin.getAddresses().get(1).getCity());
		assertEquals(italy.getId(), maxmin.getAddresses().get(1).getStateId());
		assertEquals("Veneto", maxmin.getAddresses().get(1).getRegion());
		assertEquals("31210", maxmin.getAddresses().get(1).getPostalCode());

		assertEquals("Connolly street", maxmin.getAddresses().get(2).getDescription());
		assertEquals("Dublin", maxmin.getAddresses().get(2).getCity());
		assertEquals(ireland.getId(), maxmin.getAddresses().get(2).getStateId());
		assertEquals("County Dublin", maxmin.getAddresses().get(2).getRegion());
		assertEquals("A65TF12", maxmin.getAddresses().get(2).getPostalCode());
	}

	@Test
	void findByFirstNameNotFound() {

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findByFirstName("art");

		assertEquals(0, users.size());
	}

	@Test
	void findByFirstName() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findByFirstName("art");

		assertEquals(1, users.size());

		User user = users.get(0);

		assertEquals("artur", user.getAccountName());
		assertEquals("art", user.getFirstName());
		assertEquals("artur", user.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), user.getBirthDate());
		assertNotNull(user.getCreatedDate());
		assertNotNull(user.getId());
	}

	@Test
	void nullAssociateThrowsException() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);
		UserAddress userAddress = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.associate(userAddress);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void associate() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		User newUser = daoTestUtil.createUser(user);
		State state = daoTestUtil.findStateByName("Italy");

		Address address = new Address();
		address.setDescription("Via Nuova");
		address.setCity("Venice");
		address.setStateId(state.getId());
		address.setRegion("Veneto");
		address.setPostalCode("30033");

		Address newAddress = daoTestUtil.createAddress(address);

		// run the test
		userDao.associate(UserAddress.newInstance().withUserId(newUser.getId()).withAddressId(newAddress.getId()));

		List<Address> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(1, addresses.size());

		assertEquals("Via Nuova", addresses.get(0).getDescription());
		assertEquals("Venice", addresses.get(0).getCity());
		assertEquals(state.getId(), addresses.get(0).getStateId());
		assertEquals("Veneto", addresses.get(0).getRegion());
		assertEquals("30033", addresses.get(0).getPostalCode());
	}

	@Test
	void nullCreateThrowsException() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create_with_no_address() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		// run the test
		userDao.create(user);

		User newUser = daoTestUtil.findUserByAccountName("franc");

		assertEquals("franc", newUser.getAccountName());
		assertEquals("Franco", newUser.getFirstName());
		assertEquals("Red", newUser.getLastName());
		assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedDate());
		assertNotNull(newUser.getId());

		List<Address> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void create_with_addresses() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = new User();
		user.setAccountName("franc");
		user.setBirthDate(LocalDate.of(1981, 11, 12));
		user.setFirstName("Franco");
		user.setLastName("Red");

		State italy = daoTestUtil.findStateByName("Italy");

		Address address1 = new Address();
		address1.setDescription("Via Nuova");
		address1.setCity("Venice");
		address1.setStateId(italy.getId());
		address1.setRegion("Veneto");
		address1.setPostalCode("30033");
		user.addAddress(address1);

		State ireland = daoTestUtil.findStateByName("Ireland");

		Address address2 = new Address();
		address2.setDescription("Via Vecchia");
		address2.setCity("Padova");
		address2.setStateId(ireland.getId());
		address2.setRegion("Romagna");
		address2.setPostalCode("32133");
		user.addAddress(address2);

		// run the test
		userDao.create(user);

		User newUser = daoTestUtil.findUserByAccountName("franc");

		assertEquals("franc", newUser.getAccountName());
		assertEquals("Franco", newUser.getFirstName());
		assertEquals("Red", newUser.getLastName());
		assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedDate());
		assertNotNull(newUser.getId());

		List<Address> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());

		Address newAddress1 = addresses.get(0);

		assertEquals("Via Nuova", newAddress1.getDescription());
		assertEquals("Venice", newAddress1.getCity());
		assertEquals(italy.getId(), newAddress1.getStateId());
		assertEquals("Veneto", newAddress1.getRegion());
		assertEquals("30033", newAddress1.getPostalCode());

		Address newAddress2 = addresses.get(1);

		assertEquals("Via Vecchia", newAddress2.getDescription());
		assertEquals("Padova", newAddress2.getCity());
		assertEquals(ireland.getId(), newAddress2.getStateId());
		assertEquals("Romagna", newAddress2.getRegion());
		assertEquals("32133", newAddress2.getPostalCode());
	}

	@Test
	void nullUpdateThrowsException() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = new User();
		user.setAccountName("reg");
		user.setBirthDate(LocalDate.of(1983, 12, 13));
		user.setFirstName("Reginald");
		user.setLastName("Regi");

		long userId = daoTestUtil.createUser(user).getId();
		LocalDateTime createdDate = daoTestUtil.findUserByUserId(userId).getCreatedDate();

		user = new User();
		user.setId(userId);
		user.setAccountName("regUpdated");
		user.setBirthDate(LocalDate.of(1984, 9, 1));
		user.setFirstName("ReginaldUpdated");
		user.setLastName("RegiUpdated");

		// run the test
		userDao.update(user);

		User updated = daoTestUtil.findUserByUserId(userId);

		assertEquals("regUpdated", updated.getAccountName());
		assertEquals("ReginaldUpdated", updated.getFirstName());
		assertEquals("RegiUpdated", updated.getLastName());
		assertEquals(LocalDate.of(1984, 9, 1), updated.getBirthDate());
		assertEquals(createdDate, updated.getCreatedDate());
	}
}
