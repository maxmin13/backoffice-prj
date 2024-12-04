package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.dao.jpa.constant.Department.LEGAL;
import static it.maxmin.dao.jpa.constant.Department.PRODUCTION;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.JpaBaseTestDao;
import it.maxmin.dao.jpa.JpaDaoSpringContextTestCfg;
import it.maxmin.dao.jpa.JpaQueryTestUtil;
import it.maxmin.dao.jpa.JpaUserTestUtil;
import it.maxmin.dao.jpa.api.repo.AddressDao;
import it.maxmin.dao.jpa.exception.JpaDaoTestException;
import it.maxmin.model.jpa.dao.entity.Address;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.Role;
import it.maxmin.model.jpa.dao.entity.State;
import it.maxmin.model.jpa.dao.entity.User;
import it.maxmin.model.jpa.dao.pojo.PojoAddress;
import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoUser;
import jakarta.persistence.EntityExistsException;

@SpringJUnitConfig(classes = { JpaDaoSpringContextTestCfg.class })
class AddressDaoTest extends JpaBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	AddressDao addressDao;

	@Autowired
	public AddressDaoTest(JpaQueryTestUtil jdbcQueryTestUtil, JpaUserTestUtil jdbcUserTestUtil, AddressDao addressDao) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.addressDao = addressDao;
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

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByPostalCode("A65TF12")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// run the test
		Address address = addressDao.findById(pojoAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address);

		State state = address.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		Set<User> users = address.getUsers();

		assertEquals(2, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// department
		Department department1 = user1.getDepartment();

		userTestUtil.verifyDepartment(PRODUCTION.getName(), department1);

		User user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// department
		Department department2 = user2.getDepartment();

		userTestUtil.verifyDepartment(LEGAL.getName(), department2);
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

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByPostalCode("A65TF12")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// run the test
		Address address = addressDao.findById(pojoAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		Set<User> users = address.getUsers();

		User user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		Department department = user.getDepartment();

		assertThrows(LazyInitializationException.class, department.getUsers()::size);

		// roles
		Set<Role> roles = user.getRoles();

		assertThrows(LazyInitializationException.class, roles::size);

		// addresses
		Set<Address> addresses = user.getAddresses();

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
	void findById3() {

		LOGGER.info("running test findById3");

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByPostalCode("A65TF12")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// run the test
		Address address = addressDao.findById(pojoAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		Set<User> users = address.getUsers();

		assertEquals(2, users.size());

		User user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		Department department = user.getDepartment();

		assertEquals(1, department.getUsers().size());

		// lazily loaded
		User maxmin = department.getUsers().stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		Set<Role> roles = maxmin.getRoles();

		assertEquals(3, roles.size());

		Role role1 = maxmin.getRole(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(ADMINISTRATOR.getName(), role1);

		Role role2 = maxmin.getRole(USER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(USER.getName(), role2);

		Role role3 = maxmin.getRole(WORKER.getName())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		userTestUtil.verifyRole(WORKER.getName(), role3);

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(2, addresses.size());

		Address address1 = maxmin.getAddress("30010")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		State state1 = address1.getState();

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state1);

		Address address2 = maxmin.getAddress("A65TF12")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);

		State state2 = address2.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state2);
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

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByPostalCode("31210")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// run the test
		Address address = addressDao.findById(pojoAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address);

		State state = address.getState();

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address.getUsers();

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

		Address address1 = addresses.stream().filter(address -> address.getPostalCode().equals("30010")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		State state = address1.getState();

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address1.getUsers();

		assertEquals(1, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		assertEquals(0, user1.getAddresses().size());
		assertEquals(0, user1.getRoles().size());

		// department
		Department department1 = user1.getDepartment();

		userTestUtil.verifyDepartment(PRODUCTION.getName(), department1);

		Address address2 = addresses.stream().filter(address -> address.getPostalCode().equals("A65TF12")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);

		state = address2.getState();

		userTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		users = address2.getUsers();

		assertEquals(2, users.size());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user2);

		assertEquals(0, user2.getAddresses().size());
		assertEquals(0, user2.getRoles().size());

		// department
		Department department2 = user2.getDepartment();

		userTestUtil.verifyDepartment(PRODUCTION.getName(), department2);

		User user3 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user3);

		assertEquals(0, user3.getAddresses().size());
		assertEquals(0, user3.getRoles().size());

		// department
		Department department3 = user3.getDepartment();

		userTestUtil.verifyDepartment(LEGAL.getName(), department3);
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

		Address address = addresses.stream().filter(a -> a.getPostalCode().equals("31210")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address);

		State state = address.getState();

		userTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address.getUsers();

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

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italyState.getId()));
		User franco = User.newInstance().withAccountName("franco123").withBirthDate(LocalDate.of(1977, 10, 16))
				.withFirstName("Franco").withLastName("Franchi")
				.withDepartment(Department.newInstance().withId(productionDepartment.getId()));
		address.addUser(franco);
		User carlo = User.newInstance().withAccountName("carlo123").withBirthDate(LocalDate.of(1977, 10, 16))
				.withFirstName("Carlo").withLastName("Carli")
				.withDepartment(Department.newInstance().withId(productionDepartment.getId()));
		address.addUser(carlo);

		// run the test
		Address newAddress = this.addressDao.create(address);

		assertNotNull(newAddress);
		assertNotNull(newAddress.getId());

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByAddressId(newAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("33322", "Via borgo di sotto", "Rome", "County Lazio", pojoAddress);

		assertEquals(italyState.getId(), pojoAddress.getStateId());

		List<PojoUser> users = jdbcQueryTestUtil.selectUsersByAddressId(newAddress.getId());

		assertEquals(2, users.size());

		PojoUser user1 = users.stream().filter(u -> u.getAccountName().equals("franco123")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("franco123", "Franco", "Franchi", LocalDate.of(1977, 10, 16), user1);

		assertEquals(productionDepartment.getId(), user1.getDepartmentId());

		assertEquals(0, jdbcQueryTestUtil.selectRolesByUserId(user1.getId()).size());
		assertEquals(1, jdbcQueryTestUtil.selectAddressesByUserId(user1.getId()).size());

		PojoUser user2 = users.stream().filter(u -> u.getAccountName().equals("carlo123")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("carlo123", "Carlo", "Carli", LocalDate.of(1977, 10, 16), user2);

		assertEquals(productionDepartment.getId(), user2.getDepartmentId());

		assertEquals(0, jdbcQueryTestUtil.selectRolesByUserId(user2.getId()).size());
		assertEquals(1, jdbcQueryTestUtil.selectAddressesByUserId(user2.getId()).size());
	}

	@Test
	@Order(12)
	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("12. should create the new address associated to an existing state")
	void createWithNoUser() {

		LOGGER.info("running test createWithNoUser");

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italyState.getId()));

		// run the test
		Address newAddress = this.addressDao.create(address);

		assertNotNull(newAddress);
		assertNotNull(newAddress.getId());

		PojoAddress pojoAddress = jdbcQueryTestUtil.selectAddressByAddressId(newAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("33322", "Via borgo di sotto", "Rome", "County Lazio", pojoAddress);

		assertEquals(italyState.getId(), pojoAddress.getStateId());

		List<PojoUser> users = jdbcQueryTestUtil.selectUsersByAddressId(newAddress.getId());

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

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(italyState.getId()));

		// find an existing user
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		PojoDepartment department = jdbcQueryTestUtil.selectDepartmentById(maxmin.getDepartmentId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

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

		// Find an existing address
		PojoAddress address1 = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// TODO assert address before changing it?????????
		// Assert the address's initial status
		// TODO
		// TODO //TODO //TODO //TODO //TODO //TODO //TODO

		// Change the address
		Address address = Address.newInstance().withId(address1.getId()).withCity("Padova")
				.withDescription("Via dei mille").withPostalCode("333222").withRegion("County Veneto")
				.withState(State.newInstance().withId(irelandState.getId()).withCode(irelandState.getCode())
						.withName(irelandState.getName()));

		// Find an existing user
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// Change the user
		User user = User.newInstance().withId(maxmin.getId()).withAccountName("carlo123")
				.withBirthDate(LocalDate.of(1911, 10, 16)).withFirstName("Carlo").withLastName("Carli")
				.withDepartment(Department.newInstance().withId(productionDepartment.getId()));

		user.addRole(Role.newInstance().withId(workerRole.getId()).withName(workerRole.getName()));

		address.addUser(user);

		// run the test
		Address updatedAddress = this.addressDao.update(address);

		assertNotNull(updatedAddress);
		assertEquals(address.getId(), updatedAddress.getId());

		PojoAddress newAddress = jdbcQueryTestUtil.selectAddressByAddressId(updatedAddress.getId())
				.orElseThrow(() -> new JpaDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		userTestUtil.verifyAddress("333222", "Via dei mille", "Padova", "County Veneto", newAddress);

		assertEquals(irelandState.getId(), newAddress.getStateId());

		List<PojoUser> users = jdbcQueryTestUtil.selectUsersByAddressId(updatedAddress.getId());

		assertEquals(1, users.size());

		PojoUser carlo = users.stream().filter(u -> u.getAccountName().equals("carlo123")).findFirst()
				.orElseThrow(() -> new JpaDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		userTestUtil.verifyUser("carlo123", "Carlo", "Carli", LocalDate.of(1911, 10, 16), carlo);

		assertEquals(productionDepartment.getId(), carlo.getDepartmentId());

		assertEquals(1, jdbcQueryTestUtil.selectRolesByUserId(carlo.getId()).size());
		assertEquals(1, jdbcQueryTestUtil.selectAddressesByUserId(carlo.getId()).size());
	}

	void updateRemoveUser() {

	}

	void updateWithNoStateThrowsException() {

	}

	void updateWithNotExistingStateThrowsException() {

	}

}
