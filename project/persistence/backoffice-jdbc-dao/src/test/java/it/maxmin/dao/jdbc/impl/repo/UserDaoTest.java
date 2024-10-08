package it.maxmin.dao.jdbc.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUnitTestContextCfg;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.UserRole;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcUnitTestContextCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserDaoTest extends JdbcBaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	@Mock
	State ireland;
	@Mock
	State italy;
	@Mock
	Department accounts;
	@Mock
	Department legal;
	@Mock
	Department production;
	@Mock
	UserRole administrator;
	@Mock
	UserRole user;
	@Mock
	UserRole worker;
	
	JdbcQueryTestUtil jdbcQueryTestUtil;
	UserDao userDao;
	
	@Autowired
	UserDaoTest(JdbcQueryTestUtil jdbcQueryTestUtil, UserDao userDao) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.userDao = userDao;

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
		
		when(italy.getId()).thenReturn(1l);
		when(italy.getName()).thenReturn("Italy");
		when(italy.getCode()).thenReturn("IT");
		when(ireland.getId()).thenReturn(2l);
		when(ireland.getName()).thenReturn("Ireland");
		when(ireland.getCode()).thenReturn("IE");
		when(accounts.getId()).thenReturn(1l);
		when(accounts.getName()).thenReturn("Accounts");
		when(legal.getId()).thenReturn(2l);
		when(legal.getName()).thenReturn("Legal");
		when(production.getId()).thenReturn(3l);
		when(production.getName()).thenReturn("Production");
		when(administrator.getId()).thenReturn(1l);
		when(administrator.getRoleName()).thenReturn("Administrator");
		when(user.getId()).thenReturn(2l);
		when(user.getRoleName()).thenReturn("User");
		when(worker.getId()).thenReturn(3l);
		when(worker.getRoleName()).thenReturn("Worker");
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_department.down.sql", "2_userrole.down.sql", "1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@Test
	void testFindAllNotFound() {
		
		LOGGER.info("running test testFindAllNotFound");

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(0, users.size());
	}

	@Test
	void testFindAll() {
		
		LOGGER.info("running test testFindAll");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(3, users.size());

		User maxmin = users.get(0);

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole("Administrator");
		
		verifyRole(administrator.getRoleName(), role1);

		UserRole role2 = maxmin.getRole("User");

		verifyRole(user.getRoleName(), role2);

		UserRole role3 = maxmin.getRole("Worker");

		verifyRole(worker.getRoleName(), role3);

		// department
		verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(2, maxmin.getAddresses().size());

		Address address1 = maxmin.getAddress("30010");

		verifyAddress("30010", "Via borgo di sotto", "Rome", "Lazio", address1);
		verifyState(italy.getName(), italy.getCode(), address1.getState());

		Address address2 = maxmin.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		verifyState(ireland.getName(), ireland.getCode(), address2.getState());

		User artur = users.get(1);

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");

		verifyRole(administrator.getRoleName(), role4);

		UserRole role5 = artur.getRole("User");

		verifyRole(user.getRoleName(), role5);

		// department
		verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		verifyState(ireland.getName(), ireland.getCode(), address3.getState());

		User reginald = users.get(2);

		verifyUser("reginald123", "reginald", "reinold", LocalDate.of(1944, 12, 23), reginald);

		// roles
		assertEquals(1, reginald.getRoles().size());

		UserRole role6 = reginald.getRole("User");

		verifyRole(user.getRoleName(), role6);

		// department
		verifyDepartment(accounts.getName(), reginald.getDepartment());

		// addresses
		assertEquals(0, reginald.getAddresses().size());
	}

	@Test
	void testFindAllWithNoAddress() {
		
		LOGGER.info("running test testFindAll_with_no_address");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(3, users.size());

		User maxmin = users.get(0);

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);
		
		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole("Administrator");

		verifyRole(administrator.getRoleName(), role1);

		UserRole role2 = maxmin.getRole("User");

		verifyRole(user.getRoleName(), role2);

		UserRole role3 = maxmin.getRole("Worker");

		verifyRole(worker.getRoleName(), role3);

		// department
		verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());

		User artur = users.get(1);

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");

		verifyRole(administrator.getRoleName(), role4);

		UserRole role5 = artur.getRole("User");

		verifyRole(user.getRoleName(), role5);

		// department
		verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(0, artur.getAddresses().size());

		User reginald = users.get(2);

		verifyUser("reginald123", "reginald", "reinold", LocalDate.of(1944, 12, 23), reginald);

		// roles
		assertEquals(1, reginald.getRoles().size());

		UserRole role6 = reginald.getRole("User");

		verifyRole(user.getRoleName(), role6);

		// department
		verifyDepartment(accounts.getName(), reginald.getDepartment());

		// addresses
		assertEquals(0, reginald.getAddresses().size());
	}

	@Test
	void findByAccountNameNotFound() {
		
		LOGGER.info("running test findByAccountNameNotFound");

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		Optional<User> maxmin = userDao.findByAccountName("maxmin13");

		assertTrue(maxmin.isEmpty());
	}

	@Test
	void findByAccountName() {
		
		LOGGER.info("running test findByAccountName");

		// run the test
		User artur = userDao.findByAccountName("artur").get();

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");
		
		verifyRole(administrator.getRoleName(), role4);

		UserRole role5 = artur.getRole("User");
		
		verifyRole(user.getRoleName(), role5);

		// department
		verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		verifyState(ireland.getName(), ireland.getCode(), address3.getState());
	}

	@Test
	void findByFirstNameNotFound() {
		
		LOGGER.info("running test findByFirstNameNotFound");

		// delete all users
		String[] scripts = { "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		List<User> users = userDao.findByFirstName("art");

		assertEquals(0, users.size());
	}

	@Test
	void findByFirstName() {
		
		LOGGER.info("running test findByFirstName");

		// run the test
		List<User> users = userDao.findByFirstName("Arturo");

		assertEquals(1, users.size());

		User artur = users.get(0);

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole("Administrator");
		
		verifyRole(administrator.getRoleName(), role4);

		UserRole role5 = artur.getRole("User");
		
		verifyRole(user.getRoleName(), role5);

		// department
		verifyDepartment(legal.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		verifyState(ireland.getName(), ireland.getCode(), address3.getState());
	}

	@Test
	void associate() {
		
		LOGGER.info("running test associate");

		PojoUser franco = PojoUser.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartmentId(legal.getId());

		PojoUser newUser = jdbcQueryTestUtil.createUser(franco);
		State state = jdbcQueryTestUtil.findStateByName("Italy");

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withStateId(italy.getId()).withRegion("Veneto").withPostalCode("30033");

		PojoAddress newAddress = jdbcQueryTestUtil.createAddress(address);

		// run the test
		userDao.associate(newUser.getId(), newAddress.getId());

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(1, addresses.size());

		verifyAddress("30033", "Via Nuova", "Venice", "Veneto", addresses.get(0));

		assertEquals(state.getId(), addresses.get(0).getStateId());
	}

	@Test
	void nullCreateThrowsException() {
		
		LOGGER.info("running test nullCreateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void createWithNoAddress() {
		
		LOGGER.info("running test create_with_no_address");

		User franco = User.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartment(legal).withId(accounts.getId());

		// run the test
		userDao.create(franco);

		PojoUser newUser = jdbcQueryTestUtil.findUserByAccountName("franc");
		
		verifyUser("franc", "Franco", "Red", LocalDate.of(1981, 11, 12), newUser);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void createWithAddresses() {
		
		LOGGER.info("running test create_with_addresses");

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

		PojoUser newUser = jdbcQueryTestUtil.findUserByAccountName("carl23");
		
		verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), newUser);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());
		
		PojoAddress newAddress1 = addresses.get(0);

		verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);

		PojoAddress newAddress2 = addresses.get(1);

		verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);
	}

	@Test
	void nullUpdateThrowsException() {
		
		LOGGER.info("running test nullUpdateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {
		
		LOGGER.info("running test update");

		PojoUser stephan = PojoUser.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1970, 2, 3))
				.withFirstName("Stephano").withLastName("Regi").withDepartmentId(accounts.getId());

		long stephanId = jdbcQueryTestUtil.createUser(stephan).getId();

		User stephanUpdated = User.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1980, 12, 4))
				.withFirstName("Stephano juniur").withLastName("Reginaldo").withDepartment(legal).withId(stephanId);

		// run the test
		userDao.update(stephanUpdated);

		PojoUser updated = jdbcQueryTestUtil.findUserByUserId(stephanId);
		
		verifyUser("stephan123", "Stephano juniur", "Reginaldo", LocalDate.of(1980, 12, 4), updated);
	}
}
