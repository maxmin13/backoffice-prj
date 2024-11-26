package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.dao.jpa.impl.repo.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jpa.impl.repo.constant.Department.LEGAL;
import static it.maxmin.dao.jpa.impl.repo.constant.Department.PRODUCTION;
import static it.maxmin.dao.jpa.impl.repo.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jpa.impl.repo.constant.Role.USER;
import static it.maxmin.dao.jpa.impl.repo.constant.Role.WORKER;
import static it.maxmin.dao.jpa.impl.repo.constant.State.IRELAND;
import static it.maxmin.dao.jpa.impl.repo.constant.State.ITALY;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.DaoTestException;
import it.maxmin.dao.jpa.QueryTestUtil;
import it.maxmin.dao.jpa.UnitTestContextCfg;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringJUnitConfig(classes = { UnitTestContextCfg.class })
class UserDaoTest extends TestAbstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	QueryTestUtil queryTestUtil;
	UserDao userDao;
	
	@Autowired
	public UserDaoTest(UserDao userDao, QueryTestUtil queryTestUtil) {
		this.userDao = userDao;
		this.queryTestUtil = queryTestUtil;
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

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// department
		Department department = maxmin.getDepartment();
		verifyDepartment(PRODUCTION.getName(), department);

		User artur = users.get(1);

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// department
		department = artur.getDepartment();
		verifyDepartment(LEGAL.getName(), department);
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

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

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

		Optional<Role>  role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(ADMINISTRATOR.getRoleName(), r1);

		Optional<Role>  role2 = maxmin.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(USER.getRoleName(), r2);

		Optional<Role> role3 = maxmin.getRole(WORKER.getRoleName());
		Role r3 = role3.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(WORKER.getRoleName(),r3);

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(2, addresses.size());

		Optional<Address> address1 = maxmin.getAddress("30010");
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", a1);

		State state1 = a1.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state1);

		Optional<Address> address2 = maxmin.getAddress("A65TF12");
		Address a2 = address2.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a2);

		State state2 = a2.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state2);

		User artur = users.get(1);

		// roles
		roles = artur.getRoles();

		assertEquals(2, roles.size());

		Optional<Role> role4 = artur.getRole(ADMINISTRATOR.getRoleName());
		Role r4 = role4.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(ADMINISTRATOR.getRoleName(), r4);

		Optional<Role> role5 = artur.getRole(USER.getRoleName());
		Role r5 = role5.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(USER.getRoleName(), r5);

		// addresses
		addresses = artur.getAddresses();

		assertEquals(1, addresses.size());

		Optional<Address> address3 = artur.getAddress("A65TF12");
		Address a3 = address3.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a3);

		State state = a3.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);
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
		User ar = artur.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), ar);

		// roles
		Set<Role> roles = ar.getRoles();

		assertEquals(2, roles.size());

		Optional<Role> role1 = ar.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(ADMINISTRATOR.getRoleName(), r1);

		Optional<Role> role2 = ar.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(USER.getRoleName(), r2);

		// department
		Department department = ar.getDepartment();
		verifyDepartment(LEGAL.getName(), department);

		// addresses
		Set<Address> addresses = ar.getAddresses();

		assertEquals(1, addresses.size());

		Optional<Address> address = ar.getAddress("A65TF12");
		Address a = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a);

		State state = a.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);
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

		Optional<PojoDepartment> accounts = queryTestUtil.findDepartmentByName(ACCOUNTS.getName());
		PojoDepartment acc = accounts.orElseThrow(() -> new DaoTestException("Error department not found"));

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(acc.getId()));

		Optional<PojoState> italy = queryTestUtil.findStateByName(ITALY.getName());
		PojoState it = italy.orElseThrow(() -> new DaoTestException("Error state not found"));

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(it.getId())).withRegion("Emilia Romagna")
				.withPostalCode("33456");
		carl.addAddress(address1);

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		
		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ir.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address2);

		Optional<PojoRole> role = queryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole r = role.orElseThrow(() -> new DaoTestException("Error role not found"));

		carl.addRole(Role.newInstance().withId(r.getId()));

		// run the test
		User user = userDao.create(carl);

		assertNotNull(user);
		assertNotNull(user.getId());

		Optional<PojoUser> carlo = queryTestUtil.findUserByAccountName("carl23");
		PojoUser car = carlo.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), car);

		Optional<PojoDepartment> department = queryTestUtil.findDepartmentByUserAccountName("carl23");
		PojoDepartment dep = department.orElseThrow(() -> new DaoTestException("Error department not found"));

		verifyDepartment(ACCOUNTS.getName(), dep);

		List<PojoRole> roles = queryTestUtil.findRolesByUserAccountName("carl23");

		assertEquals(1, roles.size());
		verifyRole(ADMINISTRATOR.getRoleName(), roles.get(0));

		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(car.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);

		Optional<PojoState> state1 = queryTestUtil.findStateByAddressPostalCode("33456");
		PojoState st1 = state1.orElseThrow(() -> new DaoTestException("Error state not found"));

		verifyState(ITALY.getName(), ITALY.getCode(), st1);

		PojoAddress newAddress2 = addresses.get(1);

		verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);

		Optional<PojoState> state2 = queryTestUtil.findStateByAddressPostalCode("A65TF14");
		PojoState st2 = state2.orElseThrow(() -> new DaoTestException("Error state not found"));

		verifyState(IRELAND.getName(), IRELAND.getCode(), st2);
	}

	@Test
	@Order(10)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("10. should create a user without addresses and roles")
	void createWithNoAddressAndWithNoRole() {

		LOGGER.info("running test createWithNoAddressAndWithNoRole");

		Optional<PojoDepartment> legal = queryTestUtil.findDepartmentByName(LEGAL.getName());
		PojoDepartment leg = legal.orElseThrow(() -> new DaoTestException("Error department not found"));

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red")
				.withDepartment(Department.newInstance().withId(leg.getId()));

		// run the test
		User user = userDao.create(franco);

		assertNotNull(user.getId());

		Optional<PojoUser> newUser = queryTestUtil.findUserByAccountName("franc123");
		PojoUser ne = newUser.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("franc123", "Franco", "Red", LocalDate.of(1981, 11, 12), ne);

		Optional<PojoDepartment> department = queryTestUtil.findDepartmentByUserAccountName("franc123");
		PojoDepartment dep = department.orElseThrow(() -> new DaoTestException("Error department not found"));

		verifyDepartment(LEGAL.getName(), dep);

		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(ne.getId());

		assertEquals(0, addresses.size());

		List<PojoRole> roles = queryTestUtil.findRolesByUserId(ne.getId());

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

		Optional<PojoDepartment> accounts = queryTestUtil.findDepartmentByName(ACCOUNTS.getName());
		PojoDepartment acc = accounts.orElseThrow(() -> new DaoTestException("Error department not found"));

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(acc.getId()));

		Optional<PojoAddress> address = queryTestUtil.findAddressByPostalCode("A65TF12");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		assertNotNull(address);

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

		Optional<PojoDepartment> department = queryTestUtil.findDepartmentByName(ACCOUNTS.getName());
		PojoDepartment dep = department.orElseThrow(() -> new DaoTestException("Error department not found"));

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(dep.getId()));

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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));
		
		Optional<PojoDepartment> department = queryTestUtil.findDepartmentById(max.getDepartmentId());
		PojoDepartment dep = department.orElseThrow(() -> new DaoTestException("Error department not found"));
		
		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(max.getId());
		List<PojoRole> roles = queryTestUtil.findRolesByUserId(max.getId());

		// Assert the user's initial status
		assertEquals(0, max.getVersion());
		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), max);
		verifyDepartment(PRODUCTION.getName(), dep);
		assertEquals(3, roles.size());
		verifyRole(ADMINISTRATOR.getRoleName(), roles.get(0));
		verifyRole(USER.getRoleName(), roles.get(1));
		verifyRole(WORKER.getRoleName(), roles.get(2));
		assertEquals(2, addresses.size());
		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		Optional<PojoState> state1 = queryTestUtil.findStateByAddressPostalCode("30010");
		PojoState st1 = state1.orElseThrow(() -> new DaoTestException("Error state not found"));
		verifyState(ITALY.getName(), ITALY.getCode(), st1);
		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		Optional<PojoState> state2 = queryTestUtil.findStateByAddressPostalCode("A65TF12");
		PojoState st2 = state2.orElseThrow(() -> new DaoTestException("Error state not found"));
		verifyState(IRELAND.getName(), IRELAND.getCode(), st2);

		// Update the user
		Optional<PojoDepartment> legal = queryTestUtil.findDepartmentByName(LEGAL.getName());
		PojoDepartment leg = legal.orElseThrow(() -> new DaoTestException("Error department not found"));
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin1313").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(leg.getId()));

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		Optional<PojoAddress> address = queryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork")
				.withDescription("Romolo street").withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ir.getId()));
		newMaxmin.addAddress(newAddress);

		Optional<PojoRole> administrator = queryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole adm = administrator.orElseThrow(() -> new DaoTestException("Error role not found"));
		newMaxmin.addRole(Role.newInstance().withId(adm.getId()));

		// run the test
		userDao.update(newMaxmin);

		Optional<PojoUser> user = queryTestUtil.findUserByAccountName("maxmin13");
			
		assertEquals(false, user.isPresent());

		// Verify the data
		Optional<PojoUser> newUser = queryTestUtil.findUserByAccountName("maxmin1313");
		PojoUser ne = newUser.orElseThrow(() -> new DaoTestException("Error user not found"));

		assertEquals(1, ne.getVersion());

		verifyUser("maxmin1313", "Maxi", "Miliano", LocalDate.of(1982, 9, 1), ne);

		Optional<PojoDepartment> deparment = queryTestUtil.findDepartmentByUserAccountName("maxmin1313");
		PojoDepartment de = deparment.orElseThrow(() -> new DaoTestException("Error department not found"));
		
		verifyDepartment(LEGAL.getName(), de);

		List<PojoAddress> updatedAddresses = queryTestUtil.findAddressesByUserId(ne.getId());

		assertEquals(1, updatedAddresses.size());

		verifyAddress("A89TF33", "Romolo street", "Cork", "County Cork", updatedAddresses.get(0));

		Optional<PojoState> state = queryTestUtil.findStateByAddressPostalCode("A89TF33");
		PojoState st = state.orElseThrow(() -> new DaoTestException("Error state not found"));
		
		verifyState(IRELAND.getName(), IRELAND.getCode(), st);

		List<PojoRole> updatedRoles = queryTestUtil.findRolesByUserId(ne.getId());

		assertEquals(1, updatedRoles.size());

		verifyRole(ADMINISTRATOR.getRoleName(), updatedRoles.get(0));
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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));
		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(max.getId());
		List<PojoRole> roles = queryTestUtil.findRolesByUserId(max.getId());

		// Assert the user's initial status
		assertEquals(0, max.getVersion());
		assertEquals(3, roles.size());
		assertEquals(2, addresses.size());

		// Update the user without roles and addresses
		Optional<PojoDepartment> legal = queryTestUtil.findDepartmentByName(LEGAL.getName());
		PojoDepartment leg = legal.orElseThrow(() -> new DaoTestException("Error department not found"));
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(leg.getId()));

		// run the test
		userDao.update(newMaxmin);

		// Verify the data
		Optional<PojoUser> newUser = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser ne = newUser.orElseThrow(() -> new DaoTestException("Error user not found"));

		assertEquals(1, ne.getVersion());

		List<PojoAddress> updatedAddresses = queryTestUtil.findAddressesByUserId(ne.getId());

		assertEquals(0, updatedAddresses.size());

		List<PojoRole> updatedRoles = queryTestUtil.findRolesByUserId(ne.getId());

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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));

		// Update the user without setting a department
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1));

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		Optional<PojoAddress> address = queryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork")
				.withDescription("Romolo street").withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ir.getId()));
		newMaxmin.addAddress(newAddress);

		Optional<PojoRole> newRole = queryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole r1 = newRole.orElseThrow(() -> new DaoTestException("Error role not found"));
		newMaxmin.addRole(Role.newInstance().withId(r1.getId()));

		// run the test
		assertThrows(DataIntegrityViolationException.class, () -> userDao.update(newMaxmin));
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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));

		// Update the user a department that doesn't exist
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1));

		Department newDepartment = Department.newInstance().withId(0l).withName("Agricolture");
		newMaxmin.setDepartment(newDepartment);

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		Optional<PojoAddress> address = queryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork")
				.withDescription("Romolo street").withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ir.getId()));
		newMaxmin.addAddress(newAddress);

		Optional<PojoRole> administrator = queryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole adm = administrator.orElseThrow(() -> new DaoTestException("Error role not found"));
		newMaxmin.addRole(Role.newInstance().withId(adm.getId()));

		// run the test
		assertThrows(EntityNotFoundException.class, () -> userDao.update(newMaxmin));
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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));

		// Update the user with a role that doesn't exist
		Optional<PojoDepartment> legal = queryTestUtil.findDepartmentByName(LEGAL.getName());
		PojoDepartment leg = legal.orElseThrow(() -> new DaoTestException("Error department not found"));
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(leg.getId()));

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		Optional<PojoAddress> address = queryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		Address newAddress = Address.newInstance().withId(ad.getId()).withCity("Cork")
				.withDescription("Romolo street").withPostalCode("A89TF33").withRegion("County Cork")
				.withState(State.newInstance().withId(ir.getId()));
		newMaxmin.addAddress(newAddress);

		Role role = Role.newInstance().withId(0l).withRoleName("Inspector");
		newMaxmin.addRole(role);

		// run the test
		assertThrows(EntityNotFoundException.class, () -> userDao.update(newMaxmin));
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
		Optional<PojoUser> maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoUser max = maxmin.orElseThrow(() -> new DaoTestException("Error user not found"));

		// Update the user with an address that doesn't exist.
		Optional<PojoDepartment> legal = queryTestUtil.findDepartmentByName(LEGAL.getName());
		PojoDepartment leg = legal.orElseThrow(() -> new DaoTestException("Error department not found"));
		User newMaxmin = User.newInstance().withId(max.getId()).withAccountName("maxmin13").withFirstName("Maxi")
				.withLastName("Miliano").withBirthDate(LocalDate.of(1982, 9, 1))
				.withDepartment(Department.newInstance().withId(leg.getId()));

		Optional<PojoState> ireland = queryTestUtil.findStateByName(IRELAND.getName());
		PojoState ir = ireland.orElseThrow(() -> new DaoTestException("Error state not found"));
		Address newAddress = Address.newInstance().withId(0l).withCity("Cork").withDescription("Romolo street")
				.withPostalCode("A11TF22").withRegion("County Cork")
				.withState(State.newInstance().withId(ir.getId()));
		newMaxmin.addAddress(newAddress);

		Optional<PojoRole> administrator = queryTestUtil.findRoleByRoleName(ADMINISTRATOR.getRoleName());
		PojoRole adm = administrator.orElseThrow(() -> new DaoTestException("Error role not found"));
		newMaxmin.addRole(Role.newInstance().withId(adm.getId()));

		// run the test
		assertThrows(OptimisticLockException.class, () -> userDao.update(newMaxmin));
	}

}
