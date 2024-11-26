package it.maxmin.dao.jpa.impl.repo;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.DaoTestException;
import it.maxmin.dao.jpa.QueryTestUtil;
import it.maxmin.dao.jpa.UnitTestContextCfg;
import it.maxmin.dao.jpa.api.repo.AddressDao;
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
class AddressDaoTest extends TestAbstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	QueryTestUtil queryTestUtil;
	AddressDao addressDao;
	
	@Autowired
	public AddressDaoTest(AddressDao addressDao, QueryTestUtil queryTestUtil) {
		this.addressDao = addressDao;
		this.queryTestUtil = queryTestUtil;
	}

	@Test
	@Order(1)
	@DisplayName("01. should find no address")
	void findByIdNotFound() {

		LOGGER.info("running test findByIdNotFound");

		// run the test
		Optional<Address> address = addressDao.findById(0);

		assertTrue(address.isEmpty());
	}

	@Test
	@Order(2)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void findById1() {

		LOGGER.info("running test findById1");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());
		Address ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", ad);

		State state = ad.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		Set<User> users = ad.getUsers();

		assertEquals(2, users.size());

		Optional<User> user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User us1 = user1.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), us1);

		// department
		Department department1 = us1.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department1);

		Optional<User> user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst();
		User us2 = user2.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), us2);

		// department
		Department department2 = us2.getDepartment();

		verifyDepartment(LEGAL.getName(), department2);
	}

	@Test
	@Order(3)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("03. verify lazily loaded properties")
	void findById2() {

		LOGGER.info("running test findById2");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());
		Address ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		Set<User> users = ad.getUsers();

		Optional<User> user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User us = user.orElseThrow(()-> new DaoTestException("Error user not found"));

		Department department = us.getDepartment();

		assertThrows(LazyInitializationException.class, department.getUsers()::size);

		// roles
		Set<Role> roles = us.getRoles();

		assertThrows(LazyInitializationException.class, roles::size);

		// addresses
		Set<Address> addresses = us.getAddresses();

		assertThrows(LazyInitializationException.class, addresses::size);
	}

	@Test
	@Transactional(readOnly = true)
	@Order(4)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("04. verify lazily loaded properties in the Address entity: address.users.user.department.users")
	public void findById3() {

		LOGGER.info("running test findById3");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());
		Address ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		Set<User> users = ad.getUsers();

		assertEquals(2, users.size());

		Optional<User> user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User us = user.orElseThrow(()-> new DaoTestException("Error user not found"));

		Department department = us.getDepartment();

		assertEquals(1, department.getUsers().size());

		// lazily loaded
		Optional<User> maxmin = department.getUsers().stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User max = maxmin.orElseThrow(()-> new DaoTestException("Error user not found"));

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), max);

		// roles
		Set<Role> roles = max.getRoles();

		assertEquals(3, roles.size());

		Optional<Role> role1 = max.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(ADMINISTRATOR.getRoleName(), r1);

		Optional<Role> role2 = max.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(USER.getRoleName(), r2);

		Optional<Role> role3 = max.getRole(WORKER.getRoleName());
		Role r3 = role3.orElseThrow(() -> new DaoTestException("Error role not found"));
		
		verifyRole(WORKER.getRoleName(), r3);

		// addresses
		Set<Address> addresses = max.getAddresses();

		assertEquals(2, addresses.size());

		Optional<Address> address1 = max.getAddress("30010");
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", a1);

		State state1 = a1.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state1);

		Optional<Address> address2 = max.getAddress("A65TF12");
		Address a2 = address2.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a2);

		State state2 = a2.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state2);
	}

	@Test
	@Order(5)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("05. should load an address without user")
	void findById4() {

		LOGGER.info("running test findById4");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("31210");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());
		Address ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", ad);

		State state = ad.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = ad.getUsers();

		assertEquals(0, users.size());
	}

	@Test
	@Order(6)
	@DisplayName("06. should find no address")
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(0, addresses.size());
	}

	@Test
	@Order(7)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("07. should load addresses with the associated users")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertNotNull(addresses);
		assertEquals(3, addresses.size());

		Optional<Address> address1 = addresses.stream().filter(address -> address.getPostalCode().equals("30010")).findFirst();
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", a1);

		State state = a1.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = a1.getUsers();

		assertEquals(1, users.size());

		Optional<User> user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User u1 = user1.orElseThrow(() -> new DaoTestException("Error user not found"));
		
		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), u1);

		assertEquals(0, u1.getAddresses().size());
		assertEquals(0, u1.getRoles().size());

		// department
		Department department = u1.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department);

		Optional<Address> address2 = addresses.stream().filter(address -> address.getPostalCode().equals("A65TF12")).findFirst();
		Address a2 = address2.orElseThrow(() -> new DaoTestException("Error address not found"));
		
		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a2);

		state = a2.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		users = a2.getUsers();

		assertEquals(2, users.size());

		Optional<User> user2 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst();
		User u2 = user2.orElseThrow(() -> new DaoTestException("Error user not found"));
		
		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), u2);

		assertEquals(0, u2.getAddresses().size());
		assertEquals(0, u2.getRoles().size());

		// department
		department = u2.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department);

		Optional<User> user3 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst();
		User u3 = user3.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), u3);

		assertEquals(0, u3.getAddresses().size());
		assertEquals(0, u3.getRoles().size());

		// department
		department = u3.getDepartment();

		verifyDepartment(LEGAL.getName(), department);
	}

	@Test
	@Order(8)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("08. should load addresses without users")
	void testFindAll2() {

		LOGGER.info("running test testFindAll2");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(3, addresses.size());

		Optional<Address> address = addresses.stream().filter(a -> a.getPostalCode().equals("31210")).findFirst();
		Address a = address.orElseThrow(() -> new DaoTestException("Error address not found"));

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", a);

		State state = a.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = a.getUsers();

		assertEquals(0, users.size());
	}

	@Test
	@Order(9)
	void createWithNoAddressThrowsException() {

		LOGGER.info("running test createWithNoAddressThrowsException");

		assertThrows(IllegalArgumentException.class, () -> addressDao.create(null));
	}

	@Test
	@Order(10)
	void createWithIdentifierThrowsException() {

		LOGGER.info("running test createWithIdentifierThrowsException");

		Address address = Address.newInstance().withId(1l);

		assertThrows(IllegalArgumentException.class, () -> addressDao.create(address));
	}

	@Test
	@Order(11)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("11. should create the new address with a new user and associated to an existing state")
	void create() {

		LOGGER.info("running test create");

		PojoState italy = queryTestUtil.findStateByName(ITALY.getName());
		PojoDepartment production = queryTestUtil.findDepartmentByName(PRODUCTION.getName());

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italy.getId()));
		User franco = User.newInstance().withAccountName("franco123").withBirthDate(LocalDate.of(1977, 10, 16))
				.withFirstName("Franco").withLastName("Franchi")
				.withDepartment(Department.newInstance().withId(production.getId()));
		address.addUser(franco);
		User carlo = User.newInstance().withAccountName("carlo123").withBirthDate(LocalDate.of(1977, 10, 16))
				.withFirstName("Carlo").withLastName("Carli")
				.withDepartment(Department.newInstance().withId(production.getId()));
		address.addUser(carlo);

		// run the test
		Address newAddress = this.addressDao.create(address);

		assertNotNull(newAddress);
		assertNotNull(newAddress.getId());

		PojoAddress pojoAddress = this.queryTestUtil.findAddressByPostalCode("33322");

		verifyAddress("33322", "Via borgo di sotto", "Rome", "County Lazio", pojoAddress);

		assertEquals(italy.getId(), pojoAddress.getStateId());

		List<PojoUser> users = queryTestUtil.findUsersByPostalCode("33322");

		assertEquals(2, users.size());

		Optional<PojoUser> user1 = users.stream().filter(u -> u.getAccountName().equals("franco123")).findFirst();
		PojoUser u1 = user1.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("franco123", "Franco", "Franchi", LocalDate.of(1977, 10, 16), u1);

		assertEquals(production.getId(), u1.getDepartmentId());

		assertEquals(0, queryTestUtil.findRolesByUserAccountName("franco123").size());
		assertEquals(1, queryTestUtil.findAddressesByUserAccountName("franco123").size());

		Optional<PojoUser> user2 = users.stream().filter(u -> u.getAccountName().equals("carlo123")).findFirst();
		PojoUser u2 = user2.orElseThrow(() -> new DaoTestException("Error user not found"));

		verifyUser("carlo123", "Carlo", "Carli", LocalDate.of(1977, 10, 16), u2);

		assertEquals(production.getId(), u2.getDepartmentId());

		assertEquals(0, queryTestUtil.findRolesByUserAccountName("carlo123").size());
		assertEquals(1, queryTestUtil.findAddressesByUserAccountName("carlo123").size());
	}

	@Test
	@Order(12)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("12. should create the new address associated to an existing state")
	void createWithNoUser() {

		LOGGER.info("running test createWithNoUser");

		PojoState italy = queryTestUtil.findStateByName(ITALY.getName());

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italy.getId()));

		// run the test
		Address newAddress = this.addressDao.create(address);

		assertNotNull(newAddress);
		assertNotNull(newAddress.getId());

		PojoAddress pojoAddress = this.queryTestUtil.findAddressByPostalCode("33322");

		verifyAddress("33322", "Via borgo di sotto", "Rome", "County Lazio", pojoAddress);

		assertEquals(italy.getId(), pojoAddress.getStateId());

		List<PojoUser> users = queryTestUtil.findUsersByPostalCode("33322");

		assertEquals(0, users.size());
	}

	@Test
	@Order(13)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("13. should throw EntityExistsException")
	void createWithExistingUserThrowsException() {

		LOGGER.info("running test createWithExistingUserThrowsException");

		PojoState italy = queryTestUtil.findStateByName(ITALY.getName());

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italy.getId()));

		PojoUser maxmin = queryTestUtil.findUserByAccountName("maxmin13");
		PojoDepartment department = queryTestUtil.findDepartmentById(maxmin.getDepartmentId());

		address.addUser(User.newInstance().withId(maxmin.getId()).withAccountName(maxmin.getAccountName())
				.withBirthDate(maxmin.getBirthDate())
				.withDepartment(Department.newInstance().withId(department.getId()).withName(department.getName())));

		// run the test
		assertThrows(EntityExistsException.class, () -> this.addressDao.create(address));
	}

	@Test
	@Order(14)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("14. should throw ConstraintViolationException")
	void createWithNoStateThrowsException() {

		LOGGER.info("running test createWithNoStateThrowsException");

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio");

		// run the test
		assertThrows(ConstraintViolationException.class, () -> this.addressDao.create(address));
	}

	@Test
	@Order(15)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("15. should throw ConstraintViolationException")
	void createWithNotExistingStateThrowsException() {

		LOGGER.info("running test createWithNotExistingStateThrowsException");

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(0l).withCode("FR").withName("France"));

		// run the test
		assertThrows(ConstraintViolationException.class, () -> this.addressDao.create(address));
	}

	@Test
	@Order(16)
	void updateWithNoAddressThrowsException() {

		LOGGER.info("running test updateWithNoAddressThrowsException");

		assertThrows(IllegalArgumentException.class, () -> addressDao.update(null));
	}

	@Test
	@Order(17)
	void updateWithoutIdentifierThrowsException() {

		LOGGER.info("running test updateWithIdentifierThrowsException");

		Address address = Address.newInstance();

		assertThrows(IllegalArgumentException.class, () -> addressDao.update(address));
	}

	@Test
	@Order(17)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("17. should update an address and the user associated")
	void update() {

		LOGGER.info("running test update");

		PojoState ireland = queryTestUtil.findStateByName(IRELAND.getName());

		// Find an existing address
		PojoAddress address30010 = queryTestUtil.findAddressByPostalCode("30010");
		
		//TODO assert address before changing it?????????
		// Assert the address's initial status
		//TODO 
		//TODO //TODO //TODO //TODO //TODO //TODO //TODO 

		// Change the address
		Address address = Address.newInstance().withId(address30010.getId()).withCity("Padova")
				.withDescription("Via dei mille").withPostalCode("333222").withRegion("County Veneto").withState(State
						.newInstance().withId(ireland.getId()).withCode(ireland.getCode()).withName(ireland.getName()));

		// Find an existing user
		PojoUser maxmin = queryTestUtil.findUserByAccountName("maxmin13");

		PojoDepartment production = queryTestUtil.findDepartmentByName(PRODUCTION.getName());

		// Change the user
		User user = User.newInstance().withId(maxmin.getId()).withAccountName("carlo123")
				.withBirthDate(LocalDate.of(1911, 10, 16)).withFirstName("Carlo").withLastName("Carli")
				.withDepartment(Department.newInstance().withId(production.getId()));
		
		PojoRole role = queryTestUtil.findRoleByName(WORKER.getRoleName());
		user.addRole(Role.newInstance().withId(role.getId()).withRoleName(role.getRoleName()));

		address.addUser(user);

		// run the test
		Address updatedAddress = this.addressDao.update(address);

		assertNotNull(updatedAddress);
		assertEquals(address.getId(), updatedAddress.getId());

		PojoAddress newAddress = this.queryTestUtil.findAddressByAddressId(updatedAddress.getId());
		
		verifyAddress("333222", "Via dei mille", "Padova", "County Veneto", newAddress);

		assertEquals(ireland.getId(), newAddress.getStateId());

		List<PojoUser> users = queryTestUtil.findUsersByPostalCode("333222");

		assertEquals(1, users.size());

		Optional<PojoUser> carlo = users.stream().filter(u -> u.getAccountName().equals("carlo123")).findFirst();
		PojoUser cl = carlo.orElseThrow(() -> new DaoTestException("Error user not found"));
		
		verifyUser("carlo123", "Carlo", "Carli", LocalDate.of(1911, 10, 16), cl);

		assertEquals(production.getId(), cl.getDepartmentId());

		assertEquals(1, queryTestUtil.findRolesByUserAccountName("carlo123").size());
		assertEquals(1, queryTestUtil.findAddressesByUserAccountName("carlo123").size());
	}

	void updateRemoveUser() {

	}

	void updateWithNoStateThrowsException() {

	}

	void updateWithNotExistingStateThrowsException() {

	}

}
