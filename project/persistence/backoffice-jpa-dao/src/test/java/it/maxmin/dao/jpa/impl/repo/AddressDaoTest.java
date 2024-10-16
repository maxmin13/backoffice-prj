package it.maxmin.dao.jpa.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.QueryTestUtil;
import it.maxmin.dao.jpa.UnitTestContextCfg;
import it.maxmin.dao.jpa.api.repo.AddressDao;
import it.maxmin.domain.jpa.entity.Address;
import it.maxmin.domain.jpa.entity.Department;
import it.maxmin.domain.jpa.entity.State;
import it.maxmin.domain.jpa.entity.User;
import it.maxmin.domain.jpa.entity.UserRole;
import it.maxmin.domain.jpa.pojo.PojoAddress;
import it.maxmin.domain.jpa.pojo.PojoDepartment;
import it.maxmin.domain.jpa.pojo.PojoState;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = { "classpath:database/1_create_database.up.sql", "classpath:database/2_userrole.up.sql",
		"classpath:database/2_state.up.sql",
		"classpath:database/2_department.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/2_state.down.sql", "classpath:database/2_department.down.sql",
		"classpath:database/2_userrole.down.sql",
		"classpath:database/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringJUnitConfig(classes = { UnitTestContextCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressDaoTest extends BaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	@Autowired
	QueryTestUtil queryTestUtil;

	@Autowired
	AddressDao addressDao;

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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void findById1() {

		LOGGER.info("running test findById1");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address.get());

		State state = address.get().getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		Set<User> users = address.get().getUsers();

		assertEquals(2, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// department
		Department department = user1.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department);

		User user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// department
		department = user2.getDepartment();

		verifyDepartment(LEGAL.getName(), department);
	}

	@Test
	@Order(3)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("03. verify lazily loaded properties")
	void findById2() {

		LOGGER.info("running test findById2");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());

		Set<User> users = address.get().getUsers();

		User user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		Department department = user.getDepartment();

		assertThrows(LazyInitializationException.class, department.getUsers()::size);

		// roles
		Set<UserRole> roles = user.getRoles();

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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("04. verify lazily loaded properties in the Address entity: address.users.user.department.users")
	void findById3() {

		LOGGER.info("running test findById3");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("A65TF12");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());

		Set<User> users = address.get().getUsers();
		
		assertEquals(2, users.size());

		User user = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		Department department = user.getDepartment();

		assertEquals(1, department.getUsers().size());
		
		// lazily loaded
		User maxmin = department.getUsers().stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();
		
		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), maxmin);

		// roles
		Set<UserRole> roles = maxmin.getRoles();
		
		assertEquals(3, roles.size());
		
		UserRole role1 = maxmin.getRole(ADMINISTRATOR.getRoleName());
		verifyRole(ADMINISTRATOR.getRoleName(), role1);

		UserRole role2 = maxmin.getRole(USER.getRoleName());
		verifyRole(USER.getRoleName(), role2);

		UserRole role3 = maxmin.getRole(WORKER.getRoleName());
		verifyRole(WORKER.getRoleName(), role3);

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(2, addresses.size());
		
		Address address1 = maxmin.getAddress("30010");

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		
		State state1 = address1.getState();
		
		verifyState(ITALY.getName(), ITALY.getCode(), state1);

		Address address2 = maxmin.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		
		State state2 = address2.getState();
		
		verifyState(IRELAND.getName(), IRELAND.getCode(), state2);
	}

	@Test
	@Order(5)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("05. should load an address without user")
	void findById4() {

		LOGGER.info("running test findById4");

		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("31210");

		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address.get());

		State state = address.get().getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address.get().getUsers();

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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("07. should load addresses with the associated users")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(3, addresses.size());

		Address address1 = addresses.stream().filter(address -> address.getPostalCode().equals("30010")).findFirst()
				.get();

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		State state = address1.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address1.getUsers();

		assertEquals(1, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);
		
		assertEquals(0, user1.getAddresses().size());
		assertEquals(0, user1.getRoles().size());

		// department
		Department department = user1.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department);

		Address address2 = addresses.stream().filter(address -> address.getPostalCode().equals("A65TF12")).findFirst()
				.get();

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);

		state = address2.getState();

		verifyState(IRELAND.getName(), IRELAND.getCode(), state);

		users = address2.getUsers();

		assertEquals(2, users.size());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user2);
		
		assertEquals(0, user2.getAddresses().size());
		assertEquals(0, user2.getRoles().size());

		// department
		department = user2.getDepartment();

		verifyDepartment(PRODUCTION.getName(), department);

		User user3 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user3);
		
		assertEquals(0, user3.getAddresses().size());
		assertEquals(0, user3.getRoles().size());

		// department
		department = user3.getDepartment();

		verifyDepartment(LEGAL.getName(), department);
	}

	@Test
	@Order(8)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("08. should load addresses without users")
	void testFindAll2() {

		LOGGER.info("running test testFindAll2");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(3, addresses.size());

		Address address = addresses.stream().filter(a -> a.getPostalCode().equals("31210")).findFirst().get();

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address);

		State state = address.getState();

		verifyState(ITALY.getName(), ITALY.getCode(), state);

		Set<User> users = address.getUsers();

		assertEquals(0, users.size());
	}

	@Test
	@Order(9)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("09. should insert the new address")
	void testSaveWithExistingState() {

		LOGGER.info("running test testSaveWithExistingState");

		PojoState state = queryTestUtil.findStateByName(ITALY.getName());

		Address address = Address.newInstance().withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(state.getId()));

		// run the test
		this.addressDao.save(address);

		List<PojoAddress> addresses = this.queryTestUtil.findAllAddresses();

		assertEquals(1, addresses.size());

		PojoAddress newAddress = addresses.get(0);

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", newAddress);

		Long stateId = newAddress.getStateId();

		assertEquals(state.getId(), stateId);
	}

	@Test
	@Order(10)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("10. should throw an ConstraintViolationException exception")
	void testSaveWithNonExistingState() {

		LOGGER.info("running test testSaveWithNonExistingState");

		State state = State.newInstance().withId(3l).withCode("FR").withName("France");

		Address address = Address.newInstance().withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(state);

		// run the test
		assertThrows(ConstraintViolationException.class, () -> {
			addressDao.save(address);
		});
	}

	@Test
	@Order(11)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("11. should throw a InvalidDataAccessApiUsageException exception")
	void testSaveWithUser() {

		LOGGER.info("running test testSaveWithUser");

		PojoState state = queryTestUtil.findStateByName(ITALY.getName());

		Address address = Address.newInstance().withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(state.getId()));

		PojoDepartment department = queryTestUtil.findDepartmentByName(PRODUCTION.getName());

		User franco = User.newInstance().withAccountName("maxmin13").withBirthDate(LocalDate.of(1977, 10, 16))
				.withFirstName("Max").withLastName("Minardi").withDepartment(Department.newInstance().withId(department.getId()));

		address.addUser(franco);

		// run the test
		assertThrows(InvalidDataAccessApiUsageException.class, () -> {
			addressDao.save(address);
		});
	}

	// TODO test update, check version field
}
