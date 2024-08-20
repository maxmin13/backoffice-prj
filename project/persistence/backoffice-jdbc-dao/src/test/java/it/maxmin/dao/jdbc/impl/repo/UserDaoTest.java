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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.JdbcDaoTestUtil;
import it.maxmin.dao.jdbc.JdbcTestCfg;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.UserRole;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DataSource dataSource;
	private static JdbcDaoTestUtil daoTestUtil;
	private static State ireland;
	private static State italy;
	private static Department accounts;
	private static Department legal;
	private static Department production;

	@BeforeAll
	static void setup() {

		springJdbcCtx = new AnnotationConfigApplicationContext(JdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", JdbcDaoTestUtil.class);

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		daoTestUtil.runDBScripts(scripts);

		ireland = daoTestUtil.findStateByName("Ireland");
		italy = daoTestUtil.findStateByName("Italy");

		accounts = daoTestUtil.findDepartmentByName("Accounts");
		legal = daoTestUtil.findDepartmentByName("Legal");
		production = daoTestUtil.findDepartmentByName("Production");
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useruserrole.down.sql", "2_useraddress.down.sql", "2_user.down.sql",
				"2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	static void clear() {
		String[] scripts = { "2_state.down.sql", "2_department.down.sql", "2_userrole.down.sql",
				"1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
		daoTestUtil.stopTestDB();
	}

	//@Test
	void testFindAllNotFound() {
		
		LOGGER.info("running test testFindAllNotFound");

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
		
		LOGGER.info("running test testFindAll");

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
		assertNotNull(maxmin.getCreatedAt());
		assertNotNull(maxmin.getDepartment());
		assertNotNull(maxmin.getAddresses());
		assertNotNull(maxmin.getId());

		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = maxmin.getRole("User");

		assertNotNull(role2.getId());

		UserRole role3 = maxmin.getRole("Worker");

		assertNotNull(role3.getId());

		// department
		assertEquals(production.getId(), maxmin.getDepartment().getId());
		assertEquals(production.getName(), maxmin.getDepartment().getName());

		// addresses
		assertEquals(2, maxmin.getAddresses().size());

		Address address1 = maxmin.getAddress("30010");

		assertNotNull(address1.getId());
		assertEquals("Via borgo di sotto", address1.getDescription());
		assertEquals("Rome", address1.getCity());
		assertEquals("Lazio", address1.getRegion());
		assertEquals("30010", address1.getPostalCode());
		assertEquals(italy.getId(), address1.getState().getId());
		assertEquals(italy.getName(), address1.getState().getName());
		assertEquals(italy.getCode(), address1.getState().getCode());

		Address address2 = maxmin.getAddress("A65TF12");

		assertNotNull(address2.getId());
		assertEquals("Connolly street", address2.getDescription());
		assertEquals(ireland.getId(), address2.getState().getId());
		assertEquals(ireland.getName(), address2.getState().getName());
		assertEquals(ireland.getCode(), address2.getState().getCode());

		User artur = users.get(1);

		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getId());

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");

		assertNotNull(role4.getId());

		UserRole role5 = artur.getRole("User");

		assertNotNull(role5.getId());

		// department
		assertEquals(legal.getId(), artur.getDepartment().getId());
		assertEquals(legal.getName(), artur.getDepartment().getName());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		assertNotNull(address3.getId());
		assertEquals("Connolly street", address3.getDescription());
		assertEquals("Dublin", address3.getCity());
		assertEquals("County Dublin", address3.getRegion());
		assertEquals(ireland.getId(), address3.getState().getId());
		assertEquals(ireland.getName(), address3.getState().getName());
		assertEquals(ireland.getCode(), address3.getState().getCode());

		User reginald = users.get(2);

		assertEquals("reginald123", reginald.getAccountName());
		assertEquals(accounts.getName(), reginald.getDepartment().getName());
		assertEquals(0, reginald.getAddresses().size());

		assertEquals(1, reginald.getRoles().size());

		UserRole role6 = artur.getRole("User");

		assertNotNull(role6.getId());
	}

	//@Test
	void testFindAll_with_no_address() {
		
		LOGGER.info("running test testFindAll_with_no_address");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

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
		assertNotNull(maxmin.getCreatedAt());
		assertNotNull(maxmin.getDepartment());
		assertNotNull(maxmin.getAddresses());
		assertNotNull(maxmin.getId());
		
		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = maxmin.getRole("User");

		assertNotNull(role2.getId());

		UserRole role3 = maxmin.getRole("Worker");

		assertNotNull(role3.getId());

		// department
		assertEquals(production.getId(), maxmin.getDepartment().getId());
		assertEquals(production.getName(), maxmin.getDepartment().getName());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());

		User artur = users.get(1);

		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getId());

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");

		assertNotNull(role4.getId());

		UserRole role5 = artur.getRole("User");

		assertNotNull(role5.getId());

		// department
		assertEquals(legal.getId(), artur.getDepartment().getId());
		assertEquals(legal.getName(), artur.getDepartment().getName());

		// addresses
		assertEquals(0, artur.getAddresses().size());

		User reginald = users.get(2);

		assertEquals("reginald123", reginald.getAccountName());
		assertEquals(accounts.getName(), reginald.getDepartment().getName());
		assertEquals(0, reginald.getAddresses().size());

		assertEquals(1, reginald.getRoles().size());

		UserRole role6 = artur.getRole("User");

		assertNotNull(role6.getId());
	}

	//@Test
	void findByAccountNameNotFound() {
		
		LOGGER.info("running test findByAccountNameNotFound");

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		Optional<User> user = userDao.findByAccountName("maxmin13");

		assertTrue(user.isEmpty());
	}

	//@Test
	void findByAccountName() {
		
		LOGGER.info("running test findByAccountName");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		User artur = userDao.findByAccountName("artur").get();

		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getId());

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");
		
		assertNotNull(role4.getId());

		UserRole role5 = artur.getRole("User");
		
		assertNotNull(role5.getId());

		// department
		assertEquals(legal.getId(), artur.getDepartment().getId());
		assertEquals(legal.getName(), artur.getDepartment().getName());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		assertNotNull(address3.getId());
		assertEquals("Connolly street", address3.getDescription());
		assertEquals("Dublin", address3.getCity());
		assertEquals("County Dublin", address3.getRegion());
		assertEquals(ireland.getId(), address3.getState().getId());
		assertEquals(ireland.getName(), address3.getState().getName());
		assertEquals(ireland.getCode(), address3.getState().getCode());
	}

	//@Test
	void findByFirstNameNotFound() {
		
		LOGGER.info("running test findByFirstNameNotFound");

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findByFirstName("art");

		assertEquals(0, users.size());
	}

	//@Test
	void findByFirstName() {
		
		LOGGER.info("running test findByFirstName");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		// run the test
		List<User> users = userDao.findByFirstName("Arturo");

		assertEquals(1, users.size());

		User artur = users.get(0);

		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getId());

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");
		
		assertNotNull(role4.getId());

		UserRole role5 = artur.getRole("User");
		
		assertNotNull(role5.getId());

		// department
		assertEquals(legal.getId(), artur.getDepartment().getId());
		assertEquals(legal.getName(), artur.getDepartment().getName());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		assertNotNull(address3.getId());
		assertEquals("Connolly street", address3.getDescription());
		assertEquals("Dublin", address3.getCity());
		assertEquals("County Dublin", address3.getRegion());
		assertEquals(ireland.getId(), address3.getState().getId());
		assertEquals(ireland.getName(), address3.getState().getName());
		assertEquals(ireland.getCode(), address3.getState().getCode());
	}

	//@Test
	void associate() {
		
		LOGGER.info("running test associate");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		PojoUser franco = PojoUser.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartmentId(legal.getId());

		PojoUser newUser = daoTestUtil.createUser(franco);
		State state = daoTestUtil.findStateByName("Italy");

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withStateId(italy.getId()).withRegion("Veneto").withPostalCode("30033");

		PojoAddress newAddress = daoTestUtil.createAddress(address);

		// run the test
		userDao.associate(newUser.getId(), newAddress.getId());

		List<PojoAddress> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(1, addresses.size());

		assertEquals("Via Nuova", addresses.get(0).getDescription());
		assertEquals("Venice", addresses.get(0).getCity());
		assertEquals("Veneto", addresses.get(0).getRegion());
		assertEquals("30033", addresses.get(0).getPostalCode());

		assertEquals(state.getId(), addresses.get(0).getStateId());
	}

	//@Test
	void nullCreateThrowsException() {
		
		LOGGER.info("running test nullCreateThrowsException");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	//@Test
	void create_with_no_address() {
		
		LOGGER.info("running test create_with_no_address");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User franco = User.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartment(legal).withId(accounts.getId());

		// run the test
		userDao.create(franco);

		PojoUser newUser = daoTestUtil.findUserByAccountName("franc");

		assertEquals("franc", newUser.getAccountName());
		assertEquals("Franco", newUser.getFirstName());
		assertEquals("Red", newUser.getLastName());
		assertEquals(legal.getId(), newUser.getDepartmentId());
		assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedAt());
		assertNotNull(newUser.getId());

		List<PojoAddress> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());
	}

	//@Test
	void create_with_addresses() {
		
		LOGGER.info("running test create_with_addresses");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(accounts);

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Emilia Romagna")
				.withPostalCode("33456");
		carl.addAddress(address1);

		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address2);

		// run the test
		userDao.create(carl);

		PojoUser newUser = daoTestUtil.findUserByAccountName("carl23");

		assertEquals("carl23", newUser.getAccountName());
		assertEquals("Carlo", newUser.getFirstName());
		assertEquals("Rossi", newUser.getLastName());
		assertEquals(LocalDate.of(1982, 9, 1), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedAt());
		assertNotNull(newUser.getId());

		List<PojoAddress> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		assertEquals("Via Nuova", newAddress1.getDescription());
		assertEquals("Venice", newAddress1.getCity());
		assertEquals(italy.getId(), newAddress1.getStateId());
		assertEquals("Emilia Romagna", newAddress1.getRegion());
		assertEquals("33456", newAddress1.getPostalCode());

		PojoAddress newAddress2 = addresses.get(1);

		assertEquals("Via Vecchia", newAddress2.getDescription());
		assertEquals("Dublin", newAddress2.getCity());
		assertEquals(ireland.getId(), newAddress2.getStateId());
		assertEquals("County Dublin", newAddress2.getRegion());
		assertEquals("A65TF14", newAddress2.getPostalCode());
	}

	//@Test
	void nullUpdateThrowsException() {
		
		LOGGER.info("running test nullUpdateThrowsException");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	//@Test
	void update() {
		
		LOGGER.info("running test update");

		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setJdbcTemplate(dataSource, jdbcTemplate);

		PojoUser stephan = PojoUser.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1970, 2, 3))
				.withFirstName("Stephano").withLastName("Regi").withDepartmentId(accounts.getId());

		long stephanId = daoTestUtil.createUser(stephan).getId();
		LocalDateTime createdAt = daoTestUtil.findUserByUserId(stephanId).getCreatedAt();

		User stephanUpdated = User.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1980, 12, 4))
				.withFirstName("Stephano juniur").withLastName("Reginaldo").withDepartment(legal).withId(stephanId);

		// run the test
		userDao.update(stephanUpdated);

		PojoUser updated = daoTestUtil.findUserByUserId(stephanId);

		assertEquals("stephan123", updated.getAccountName());
		assertEquals("Stephano juniur", updated.getFirstName());
		assertEquals("Reginaldo", updated.getLastName());
		assertEquals(LocalDate.of(1980, 12, 4), updated.getBirthDate());
		assertEquals(createdAt, updated.getCreatedAt());
	}
}