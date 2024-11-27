package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.dao.jpa.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jpa.constant.Department.LEGAL;
import static it.maxmin.dao.jpa.constant.Department.PRODUCTION;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jpa.constant.Role.USER;
import static it.maxmin.dao.jpa.constant.Role.WORKER;
import static it.maxmin.dao.jpa.constant.State.IRELAND;
import static it.maxmin.dao.jpa.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.LazyInitializationException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.JpaBaseTestDao;
import it.maxmin.dao.jpa.JpaDaoSpringContextTestCfg;
import it.maxmin.dao.jpa.JpaDaoTestException;
import it.maxmin.dao.jpa.JpaQueryTestUtil;
import it.maxmin.dao.jpa.JpaUserTestUtil;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.model.jpa.dao.entity.Address;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.Role;
import it.maxmin.model.jpa.dao.entity.State;
import it.maxmin.model.jpa.dao.entity.User;
import it.maxmin.model.jpa.dao.pojo.PojoAddress;
import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoRole;
import it.maxmin.model.jpa.dao.pojo.PojoState;
import it.maxmin.model.jpa.dao.pojo.PojoUser;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = { "classpath:database/1_create_database.up.sql", "classpath:database/2_role.up.sql",
		"classpath:database/2_state.up.sql",
		"classpath:database/2_department.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/2_state.down.sql", "classpath:database/2_department.down.sql",
		"classpath:database/2_role.down.sql",
		"classpath:database/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
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
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// department
		Department department = maxmin.getDepartment();
		userTestUtil.verifyDepartment(PRODUCTION.getName(), department);

		User artur = users.get(1);

		userTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// department
		department = artur.getDepartment();
		userTestUtil.verifyDepartment(LEGAL.getName(), department);
	}

	@Test
	@Order(3)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("03. verify lazily loaded properties, should thorow LazyInitializationException")
	void testFindAll2() {

		LOGGER.info("running test testFindAll2");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		assertThrows(LazyInitializationException.class, maxmin.getRoles()::size);

		assertThrows(LazyInitializationException.class, maxmin.getAddresses()::size);

		User artur = users.get(1);

		userTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		assertThrows(LazyInitializationException.class, artur.getRoles()::size);

		assertThrows(LazyInitializationException.class, artur.getAddresses()::size);
	}

	@Test
	@Transactional(readOnly = true)
	@Order(4)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("04. verify lazily loaded properties in the User entity: roles and addresses")
	public void testFindAll3() {

		LOGGER.info("running test testFindAll3");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		// roles
		Set<Role> roles = maxmin.getRoles();

		assertEquals(3, roles.size());

		Optional<Role> role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), r1);

		Optional<Role> role2 = maxmin.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(USER.getRoleName(), r2);

		Optional<Role> role3 = maxmin.getRole(WORKER.getRoleName());
		Role r3 = role3.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(WORKER.getRoleName(), r3);

		// addresses
		Set<Address> addresses1 = maxmin.getAddresses();

		assertEquals(2, addresses1.size());

		Optional<Address> address1 = maxmin.getAddress("30010");
		Address a1 = address1.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", a1);

		State state1 = a1.getState();

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state1);

		Optional<Address> address2 = maxmin.getAddress("A65TF12");
		Address a2 = address2.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a2);

		State state2 = a2.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state2);

		User artur = users.get(1);

		// roles
		roles = artur.getRoles();

		assertEquals(2, roles.size());

		Optional<Role> role4 = artur.getRole(ADMINISTRATOR.getRoleName());
		Role r4 = role4.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), r4);

		Optional<Role> role5 = artur.getRole(USER.getRoleName());
		Role r5 = role5.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(USER.getRoleName(), r5);

		// addresses
		Set<Address> addresses2 = artur.getAddresses();

		assertEquals(1, addresses2.size());

		Optional<Address> address3 = artur.getAddress("A65TF12");
		Address a3 = address3.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a3);

		State state3 = a3.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state3);
	}

	@Test
	@Order(5)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("05. should not find any user")
	void findByAccountNameNotFound() {

		LOGGER.info("running test findByAccountNameNotFound");

		// run the test
		Optional<User> none = userDao.findByAccountName("none");

		assertTrue(none.isEmpty());
	}

	@Test
	@Order(6)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("06. should find a user")
	void findByAccountName() {

		LOGGER.info("running test findByAccountName");

		// run the test
		Optional<User> artur = userDao.findByAccountName("artur");
		User ar = artur.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), ar);

		// roles
		Set<Role> roles = ar.getRoles();

		assertEquals(2, roles.size());

		Optional<Role> role1 = ar.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), r1);

		Optional<Role> role2 = ar.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(USER.getRoleName(), r2);

		// department
		Department department = ar.getDepartment();
		userTestUtil.verifyDepartment(LEGAL.getName(), department);

		// addresses
		Set<Address> addresses = ar.getAddresses();

		assertEquals(1, addresses.size());

		Optional<Address> address = ar.getAddress("A65TF12");
		Address a = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a);

		State state = a.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state);
	}

	@Test
	@Order(7)
	void createWithNoUserThrowsException() {

		LOGGER.info("running test createWithNoUserThrowsException");

		assertThrows(IllegalArgumentException.class, () -> userDao.create(null));
	}

	@Test
	@Order(8)
	void createWithIdentifierThrowsException() {

		LOGGER.info("running test createWithIdentifierThrowsException");
		final
		User user = User.newInstance().withId(1l);

		assertThrows(IllegalArgumentException.class, () -> userDao.create(user));
	}

	@Test
	@Order(9)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("09. should create a user associated to a department, with a new address, with an existing role")
	void create() {

		LOGGER.info("running test create");

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

		carl.addRole(Role.newInstance().withId(administrator.getId()));

		// run the test
		User user = userDao.create(carl);

		assertNotNull(user);
		assertNotNull(user.getId());

		Optional<PojoUser> carlo = jdbcQueryTestUtil.findUserByAccountName("carl23");
		PojoUser car = carlo.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), car);

		Optional<PojoDepartment> department = jdbcQueryTestUtil.findDepartmentByUserAccountName("carl23");
		PojoDepartment dep = department.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		userTestUtil.verifyDepartment(ACCOUNTS.getName(), dep);

		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserAccountName("carl23");

		assertEquals(1, roles.size());
		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), roles.get(0));

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(car.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		userTestUtil.verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);

		Optional<PojoState> state1 = jdbcQueryTestUtil.findStateByAddressPostalCode("33456");
		PojoState st1 = state1.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), st1);

		PojoAddress newAddress2 = addresses.get(1);

		userTestUtil.verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);

		Optional<PojoState> state2 = jdbcQueryTestUtil.findStateByAddressPostalCode("A65TF14");
		PojoState st2 = state2.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), st2);
	}

	@Test
	@Order(10)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("10. should create a user without addresses and roles")
	void createWithNoAddressAndWithNoRole() {

		LOGGER.info("running test createWithNoAddressAndWithNoRole");

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red")
				.withDepartment(Department.newInstance().withId(legal.getId()));

		// run the test
		User user = userDao.create(franco);

		assertNotNull(user.getId());

		Optional<PojoUser> newUser = jdbcQueryTestUtil.findUserByAccountName("franc123");
		PojoUser ne = newUser.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("franc123", "Franco", "Red", LocalDate.of(1981, 11, 12), ne);

		Optional<PojoDepartment> department = jdbcQueryTestUtil.findDepartmentByUserAccountName("franc123");
		PojoDepartment dep = department.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		userTestUtil.verifyDepartment(LEGAL.getName(), dep);

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(ne.getId());

		assertEquals(0, addresses.size());

		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserId(ne.getId());

		assertEquals(0, roles.size());
	}

	@Test
	@Order(11)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("11. should throw EntityExistsException")
	void createWithExistingAddressThrowsException() {

		LOGGER.info("running test createWithExistingAddressThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()));

		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("A65TF12");
		PojoAddress ad = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		Address existingAddress = Address.newInstance().withId(ad.getId());

		carl.addAddress(existingAddress);

		// run the test
		assertThrows(EntityExistsException.class, () -> userDao.create(carl));
	}

	@Test
	@Order(12)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("12. should throw ConstraintViolationException")
	void createWithNoDepartmentThrowsException() {

		LOGGER.info("running test createWithNoDepartmentThrowsException");

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red");

		// run the test
		assertThrows(ConstraintViolationException.class, () -> userDao.create(franco));
	}

	@Test
	@Order(13)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("13. should throw ConstraintViolationException")
	void createWithNotExistingDepartmentThrowsException() {

		LOGGER.info("running test createWithNotExistingDepartmentThrowsException");

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red")
				.withDepartment(Department.newInstance().withId(0l).withName("Agricolture"));

		// run the test
		assertThrows(ConstraintViolationException.class, () -> userDao.create(franco));
	}

	@Test
	@Order(14)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("14. should throw DataIntegrityViolationException")
	void createWithNotExistingRoleThrowsException() {

		LOGGER.info("running test createWithNotExistingRoleThrowsException");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(accounts.getId()));

		carl.addRole(Role.newInstance().withId(23l).withRoleName("Mechanic"));

		// run the test
		assertThrows(DataIntegrityViolationException.class, () -> userDao.create(carl));
	}

	@Test
	@Order(15)
	void updateWithNoUserThrowsException() {

		LOGGER.info("running test updateWithNoUserThrowsException");

		assertThrows(IllegalArgumentException.class, () -> userDao.update(null));
	}

	@Test
	@Order(16)
	void updateWithoutIdentifierThrowsException() {

		LOGGER.info("running test updateWithIdentifierThrowsException");

		User user = User.newInstance();

		assertThrows(IllegalArgumentException.class, () -> userDao.update(user));
	}

	@Test
	@Order(17)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("17. should update user, department, addresses and roles")
	void update() {

		LOGGER.info("running test update");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		Optional<PojoDepartment> department = jdbcQueryTestUtil.findDepartmentById(max.getDepartmentId());
		PojoDepartment dep = department.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(max.getId());
		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserId(max.getId());

		// Assert the user's initial status
		assertEquals(0, max.getVersion());
		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), max);
		userTestUtil.verifyDepartment(PRODUCTION.getName(), dep);
		assertEquals(3, roles.size());
		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), roles.get(0));
		userTestUtil.verifyRole(USER.getRoleName(), roles.get(1));
		userTestUtil.verifyRole(WORKER.getRoleName(), roles.get(2));
		assertEquals(2, addresses.size());
		userTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		Optional<PojoState> state1 = jdbcQueryTestUtil.findStateByAddressPostalCode("30010");
		PojoState st1 = state1.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), st1);
		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		Optional<PojoState> state2 = jdbcQueryTestUtil.findStateByAddressPostalCode("A65TF12");
		PojoState st2 = state2.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), st2);

		// Change the user
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin1313").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(legal.getId()));

		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ireland.getId()));
		newUser.addAddress(newAddress);

		newUser.addRole(Role.newInstance().withId(administrator.getId()));

		// run the test
		userDao.update(newUser);

		Optional<PojoUser> user = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		assertEquals(false, user.isPresent());

		// Verify the data
		Optional<PojoUser> upatedUser = jdbcQueryTestUtil.findUserByAccountName("maxmin1313");
		PojoUser upd = upatedUser.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		assertEquals(1, upd.getVersion());

		userTestUtil.verifyUser("maxmin1313", "Maxi", "Miliano", LocalDate.of(1982, 9, 1), upd);

		Optional<PojoDepartment> deparment = jdbcQueryTestUtil.findDepartmentByUserAccountName("maxmin1313");
		PojoDepartment de = deparment.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		userTestUtil.verifyDepartment(LEGAL.getName(), de);

		List<PojoAddress> updatedAddresses = jdbcQueryTestUtil.findAddressesByUserId(upd.getId());

		assertEquals(1, updatedAddresses.size());

		userTestUtil.verifyAddress("A89TF33", "Romolo street", "Cork", "County Cork", updatedAddresses.get(0));

		Optional<PojoState> state = jdbcQueryTestUtil.findStateByAddressPostalCode("A89TF33");
		PojoState st = state.orElseThrow(() -> new JpaDaoTestException(ERROR_STATE_NOT_FOUND_MSG));

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), st);

		List<PojoRole> updatedRoles = jdbcQueryTestUtil.findRolesByUserId(upd.getId());

		assertEquals(1, updatedRoles.size());

		userTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), updatedRoles.get(0));
	}

	@Test
	@Order(18)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("18. should remove addresses and roles")
	void updateRemoveRoleAndAddress() {

		LOGGER.info("running test updateRemoveRoleAndAddress");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		List<PojoAddress> addresses = jdbcQueryTestUtil.findAddressesByUserId(max.getId());
		List<PojoRole> roles = jdbcQueryTestUtil.findRolesByUserId(max.getId());

		// Assert the user's initial status
		assertEquals(0, max.getVersion());
		assertEquals(3, roles.size());
		assertEquals(2, addresses.size());

		// Update the user without roles and addresses
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(legal.getId()));

		// run the test
		userDao.update(newUser);

		// Verify the data
		Optional<PojoUser> updatedUser = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser upd = updatedUser.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		assertEquals(1, upd.getVersion());

		List<PojoAddress> updatedAddresses = jdbcQueryTestUtil.findAddressesByUserId(upd.getId());

		assertEquals(0, updatedAddresses.size());

		List<PojoRole> updatedRoles = jdbcQueryTestUtil.findRolesByUserId(upd.getId());

		assertEquals(0, updatedRoles.size());
	}

	@Test
	@Order(19)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("19. should throw DataIntegrityViolationException")
	void updateWithNoDepartmentThrowsException() {

		LOGGER.info("running test updateWithNoDepartmentThrowsException");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// Update the user without setting a department
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1));

		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ireland.getId()));
		newUser.addAddress(newAddress);

		Optional<PojoRole> newRole = jdbcQueryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole r1 = newRole.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));
		newUser.addRole(Role.newInstance().withId(r1.getId()));

		// run the test
		assertThrows(DataIntegrityViolationException.class, () -> userDao.update(newUser));
	}

	@Test
	@Order(20)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("20. should throw EntityNotFoundException")
	void updateWithNotExistingDepartmentThrowsException() {

		LOGGER.info("running test updateWithNotExistingDepartmentThrowsException");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// Update the user a department that doesn't exist
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1));

		Department newDepartment = Department.newInstance().withId(0l).withName("Agricolture");
		newUser.setDepartment(newDepartment);

		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ireland.getId()));
		newUser.addAddress(newAddress);

		newUser.addRole(Role.newInstance().withId(administrator.getId()));

		// run the test
		assertThrows(EntityNotFoundException.class, () -> userDao.update(newUser));
	}

	@Test
	@Order(21)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("21. should throw EntityNotFoundException")
	void updateWithNotExistingRoleThrowsException() {

		LOGGER.info("running test updateWithNotExistingRoleThrowsException");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// Update the user with a role that doesn't exist
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(legal.getId()));

		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ireland.getId()));
		newUser.addAddress(newAddress);

		Role role = Role.newInstance().withId(0l).withRoleName("Inspector");
		newUser.addRole(role);

		// run the test
		assertThrows(EntityNotFoundException.class, () -> userDao.update(newUser));
	}

	@Test
	@Order(22)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("22. should throw OptimisticLockException")
	/**
	 * I would have expected an EntityNotFoundException, but an
	 * OptimisticLockException is thrown. Found this explanation:
	 * https://stackoverflow.com/questions/24827576/what-does-mean-hibernates-unsaved-value-mapping-was-incorrect
	 * 
	 * If an entity is determined by means of your unsaved-value as detached, but is
	 * instead new, then hibernate can't compare the version numbers (because the
	 * entity just doesn't exist in the database). But Hibernate can't know if your
	 * unsaved-value mapping is not correct or the entity has been deleted in
	 * another transaction. This is described in org.hibernate.StaleStateException
	 * as well: Thrown when a version number or timestamp check failed, indicating
	 * that the Session contained stale data (when using long transactions with
	 * versioning). Also occurs if we try delete or update a row that does not
	 * exist.
	 */
	void updateWithNotExistingAddressThrowsException() {

		LOGGER.info("running test updateWithNotExistingAddressThrowsException");

		// Get an existing user
		Optional<PojoUser> maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// Update the user with an address that doesn't exist.
		User newUser = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(legal.getId()));

		Address newAddress = Address.newInstance().withId(0l).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A11TF22").withRegion("County Cork")
				.withState(State.newInstance().withId(ireland.getId()));
		newUser.addAddress(newAddress);

		newUser.addRole(Role.newInstance().withId(administrator.getId()));

		// run the test
		assertThrows(OptimisticLockException.class, () -> userDao.update(newUser));
	}

}
