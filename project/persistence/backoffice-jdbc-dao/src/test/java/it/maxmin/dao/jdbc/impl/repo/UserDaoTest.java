package it.maxmin.dao.jdbc.impl.repo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUser;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertRole;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByAccountName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByFirstName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectRoleByRoleName;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);
	private UserDao userDao;

	private JdbcUserTestUtil jdbcUserTestUtil;
	
	@Mock
	private InsertUser insertUser;
	
	@Mock
	private InsertUserAddress insertUserAddress;
	
	@Mock
	private InsertRole insertRole;
	
	@Mock
	private SelectAllUsers selectAllUsers;
	
	@Mock
	private SelectUserByAccountName selectUserByAccountName;
	
	@Mock
	private SelectUserByFirstName selectUserByFirstName;
	
	@Mock
	private SelectRoleByRoleName selectRoleByRoleName;
	
	@Mock
	private UpdateUser updateUser;

	@InjectMocks
	private AddressDaoImpl addressDao;

	UserDaoTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void testFindAll() {

		LOGGER.info("running test testFindAll");

		// run the test
//		List<User> users = userDao.findAll();
//
//		assertEquals(2, users.size());
//
//		User maxmin = users.get(0);
//
//		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);
//
//		// roles
//		assertEquals(3, maxmin.getRoles().size());
//
//		Role role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1);
//
//		Role role2 = maxmin.getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2);
//
//		Role role3 = maxmin.getRole(WORKER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(WORKER.getRoleName(), role3);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), maxmin.getDepartment());
//
//		// addresses
//		assertEquals(2, maxmin.getAddresses().size());
//
//		Address address1 = maxmin.getAddress("30010");
//
//		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
//		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());
//
//		Address address2 = maxmin.getAddress("A65TF12");
//
//		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
//		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.getState());
//
//		User artur = users.get(1);
//
//		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);
//
//		// roles
//		assertEquals(2, artur.getRoles().size());
//
//		Role role4 = artur.getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);
//
//		Role role5 = artur.getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());
//
//		// addresses
//		assertEquals(1, artur.getAddresses().size());
//
//		Address address3 = artur.getAddress("A65TF12");
//
//		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
//		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
	}

	@Test
	void testFindAllWithNoAddress() {

		LOGGER.info("running test testFindAllWithNoAddress");


		// run the test
//		List<User> users = userDao.findAll();
//
//		assertEquals(2, users.size());
//
//		User maxmin = users.get(0);
//
//		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);
//
//		// roles
//		assertEquals(3, maxmin.getRoles().size());
//
//		Role role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1);
//
//		Role role2 = maxmin.getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2);
//
//		Role role3 = maxmin.getRole(WORKER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(WORKER.getRoleName(), role3);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), maxmin.getDepartment());
//
//		// addresses
//		assertEquals(0, maxmin.getAddresses().size());
//
//		User artur = users.get(1);
//
//		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);
//
//		// roles
//		assertEquals(2, artur.getRoles().size());
//
//		Role role4 = artur.getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);
//
//		Role role5 = artur.getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());
//
//		// addresses
//		assertEquals(0, artur.getAddresses().size());
	}

	@Test
	void findByAccountNameNotFound() {

		LOGGER.info("running test findByAccountNameNotFound");


		// run the test
//		Optional<User> maxmin = userDao.findByAccountName("maxmin13");
//
//		assertTrue(maxmin.isEmpty());
	}

	@Test
	void findByAccountName() {

		LOGGER.info("running test findByAccountName");

		// run the test
//		Optional<User> artur = userDao.findByAccountName("artur");
//
//		if (!artur.isPresent()) {
//			throw new DaoTestException("User not found!");
//		}
//
//		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur.get());
//
//		// roles
//		assertEquals(2, artur.get().getRoles().size());
//
//		Role role4 = artur.get().getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);
//
//		Role role5 = artur.get().getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.get().getDepartment());
//
//		// addresses
//		assertEquals(1, artur.get().getAddresses().size());
//
//		Address address3 = artur.get().getAddress("A65TF12");
//
//		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
//		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
	}

	@Test
	void findByFirstNameNotFound() {

		LOGGER.info("running test findByFirstNameNotFound");

		// run the test
//		List<User> users = userDao.findByFirstName("art");
//
//		assertEquals(0, users.size());
	}

	@Test
	void findByFirstName() {

		LOGGER.info("running test findByFirstName");

		// run the test
//		List<User> users = userDao.findByFirstName("Arturo");
//
//		assertEquals(1, users.size());
//
//		User artur = users.get(0);
//
//		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);
//
//		// roles
//		assertEquals(2, artur.getRoles().size());
//
//		Role role4 = artur.getRole(ADMINISTRATOR.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);
//
//		Role role5 = artur.getRole(USER.getRoleName());
//
//		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);
//
//		// department
//		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());
//
//		// addresses
//		assertEquals(1, artur.getAddresses().size());
//
//		Address address3 = artur.getAddress("A65TF12");
//
//		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
//		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
	}


	@Test
	void createWithNoUserThrowsException() {

		LOGGER.info("running test createWithNoUserThrowsException");

//		Throwable throwable = assertThrows(Throwable.class, () -> {
//			userDao.create(null);
//		});
//
//		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	

	@Test
	void createWithAddresses() {

		LOGGER.info("running test createWithAddresses");

//		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
//				.withFirstName("Carlo").withLastName("Rossi")
//				.withDepartment(Department.newInstance().withId(1l));
//
//		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
//				.withState(State.newInstance().withId(2l)).withRegion("Emilia Romagna")
//				.withPostalCode("33456");
//		carl.addAddress(address1);
//
//		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
//				.withState(State.newInstance().withId(3l)).withRegion("County Dublin")
//				.withPostalCode("A65TF14");
//		carl.addAddress(address2);
//
//		// run the test
//		userDao.create(carl);
//
//		PojoUser newUser = jdbcQueryTestUtil.findUserByAccountName("carl23");
//
//		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), newUser);
//
//		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());
//
//		assertEquals(2, addresses.size());
//
//		PojoAddress newAddress1 = addresses.get(0);
//
//		jdbcUserTestUtil.verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);
//
//		PojoAddress newAddress2 = addresses.get(1);
//
//		jdbcUserTestUtil.verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);
	}

	@Test
	void updateWithNoUserThrowsException() {

		LOGGER.info("running test updateWithNoUserThrowsException");

//		Throwable throwable = assertThrows(Throwable.class, () -> {
//			userDao.update(null);
//		});
//
//		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		LOGGER.info("running test update");

		

	
	}
}
