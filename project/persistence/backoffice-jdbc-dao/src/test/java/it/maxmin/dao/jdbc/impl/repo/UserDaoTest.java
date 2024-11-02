package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.impl.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static it.maxmin.dao.jdbc.impl.constant.Role.WORKER;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.DaoTestException;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.UserRole;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

class UserDaoTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);
	UserDao userDao;

	@Autowired
	UserDaoTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, UserDao userDao) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.userDao = userDao;
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

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1);

		UserRole role2 = maxmin.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2);

		UserRole role3 = maxmin.getRole(WORKER.getRoleName());

		jdbcUserTestUtil.verifyRole(WORKER.getRoleName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(2, maxmin.getAddresses().size());

		Address address1 = maxmin.getAddress("30010");

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());

		Address address2 = maxmin.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.getState());

		User artur = users.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);

		UserRole role5 = artur.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
	}

	@Test
	void testFindAllWithNoAddress() {

		LOGGER.info("running test testFindAllWithNoAddress");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		UserRole role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1);

		UserRole role2 = maxmin.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2);

		UserRole role3 = maxmin.getRole(WORKER.getRoleName());

		jdbcUserTestUtil.verifyRole(WORKER.getRoleName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());

		User artur = users.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);

		UserRole role5 = artur.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());

		// addresses
		assertEquals(0, artur.getAddresses().size());
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
		Optional<User> artur = userDao.findByAccountName("artur");

		if (!artur.isPresent()) {
			throw new DaoTestException("User not found!");
		}

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur.get());

		// roles
		assertEquals(2, artur.get().getRoles().size());

		UserRole role4 = artur.get().getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);

		UserRole role5 = artur.get().getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.get().getDepartment());

		// addresses
		assertEquals(1, artur.get().getAddresses().size());

		Address address3 = artur.get().getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
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

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		assertEquals(2, artur.getRoles().size());

		UserRole role4 = artur.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role4);

		UserRole role5 = artur.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role5);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), artur.getDepartment());

		// addresses
		assertEquals(1, artur.getAddresses().size());

		Address address3 = artur.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address3);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address3.getState());
	}

	@Test
	void associate() {

		LOGGER.info("running test associate");

		PojoUser franco = PojoUser.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartmentId(legal.getId());

		PojoUser newUser = jdbcQueryTestUtil.createUser(franco);

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withStateId(italy.getId()).withRegion("Veneto").withPostalCode("30033");

		PojoAddress newAddress = jdbcQueryTestUtil.createAddress(address);

		// run the test
		userDao.associate(newUser.getId(), newAddress.getId());

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "Veneto", addresses.get(0));

		assertEquals(italy.getId(), addresses.get(0).getStateId());
	}

	@Test
	void createWithNoUserThrowsException() {

		LOGGER.info("running test createWithNoUserThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void createWithNoAddress() {

		LOGGER.info("running test create_with_no_address");

		User franco = User.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red")
				.withDepartment(Department.newInstance().withId(accounts.getId()));

		// run the test
		userDao.create(franco);

		PojoUser newUser = jdbcQueryTestUtil.findUserByAccountName("franc");

		jdbcUserTestUtil.verifyUser("franc", "Franco", "Red", LocalDate.of(1981, 11, 12), newUser);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void createWithAddresses() {

		LOGGER.info("running test createWithAddresses");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()));

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

		jdbcUserTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), newUser);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		jdbcUserTestUtil.verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);

		PojoAddress newAddress2 = addresses.get(1);

		jdbcUserTestUtil.verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);
	}

	@Test
	void updateWithNoUserThrowsException() {

		LOGGER.info("running test updateWithNoUserThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		LOGGER.info("running test update");

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		long maxminId = maxmin.getId();

		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS

		// Update the user
		User maxminUpdated = User.newInstance().withId(maxminId).withAccountName("maxmin13")
				.withBirthDate(LocalDate.of(1980, 12, 4)).withFirstName("Max13").withLastName("Min13")
				.withDepartment(Department.newInstance().withId(legal.getId()));

		// run the test
		userDao.update(maxminUpdated);

		PojoUser updated = jdbcQueryTestUtil.findUserByUserId(maxminId);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max13", "Min13", LocalDate.of(1980, 12, 4), updated);
	}
}
