package it.maxmin.dao.hibernate.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.hibernate.QueryTestUtil;
import it.maxmin.dao.hibernate.UnitTestContextCfg;
import it.maxmin.dao.hibernate.api.repo.AddressDao;
import it.maxmin.domain.hibernate.entity.Address;
import it.maxmin.domain.hibernate.entity.Department;
import it.maxmin.domain.hibernate.entity.State;
import it.maxmin.domain.hibernate.entity.User;
import it.maxmin.domain.hibernate.entity.UserRole;
import it.maxmin.domain.hibernate.pojo.PojoAddress;

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
class AddressDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

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
	
	@Autowired
	QueryTestUtil queryTestUtil;

	@Autowired
	AddressDao addressDao;
	
	@BeforeEach
	void init() {
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
		
		verifyState(ireland.getName(), ireland.getCode(), state);

		Set<User> users = address.get().getUsers();

		assertEquals(2, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();
		
		verifyUser("Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// department
		Department department = user1.getDepartment();

		verifyDepartment(production.getName(), department);

		User user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();
		
		verifyUser("Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// department
		department = user2.getDepartment();

		verifyDepartment(legal.getName(), department);
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

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();
		
		Department department = user1.getDepartment();
		
		assertThrows(LazyInitializationException.class, department.getUsers()::size);

		// roles
		Set<UserRole> roles = user1.getRoles();

		assertThrows(LazyInitializationException.class, roles::size);

		// addresses
		Set<Address> addresses = user1.getAddresses();

		assertThrows(LazyInitializationException.class, addresses::size);
	}
	
	@Test
	@Order(4)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("04. should load an address without user")
	void findById3() {

		LOGGER.info("running test findById3");
		
		PojoAddress pojoAddress = queryTestUtil.findAddressByPostalCode("31210");
	
		// run the test
		Optional<Address> address = addressDao.findById(pojoAddress.getId());

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address.get());

		State state = address.get().getState();
		
		verifyState(italy.getName(), italy.getCode(), state);

		Set<User> users = address.get().getUsers();

		assertEquals(0, users.size());
	}

	@Test
	@Order(5)	
	@DisplayName("05. should find no address")
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(0, addresses.size());
	}
	
	@Test
	@Order(6)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("06. should load addresses with the associated users")
	void testFindAll1() {

		LOGGER.info("running test testFindAll1");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(3, addresses.size());

		Address address1 = addresses.stream().filter(address -> address.getPostalCode().equals("30010")).findFirst()
				.get();

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		State state = address1.getState();

		verifyState(italy.getName(), italy.getCode(), state);

		Set<User> users = address1.getUsers();

		assertEquals(1, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		verifyUser("Max", "Minardi", LocalDate.of(1977, 10, 16), user1);
		assertEquals(0, user1.getAddresses().size());
		assertEquals(0, user1.getRoles().size());
		
		// department
		Department department = user1.getDepartment();

		verifyDepartment(production.getName(), department);

		Address address2 = addresses.stream().filter(address -> address.getPostalCode().equals("A65TF12")).findFirst()
				.get();
		
		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);

		state = address2.getState();

		verifyState(ireland.getName(), ireland.getCode(), state);

		users = address2.getUsers();

		assertEquals(2, users.size());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		verifyUser("Max", "Minardi", LocalDate.of(1977, 10, 16), user2);
		assertEquals(0, user2.getAddresses().size());
		assertEquals(0, user2.getRoles().size());
		
		// department
		department = user2.getDepartment();
		
		verifyDepartment(production.getName(), department);

		User user3 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		verifyUser("Arturo", "Art", LocalDate.of(1923, 10, 12), user3);
		assertEquals(0, user3.getAddresses().size());
		assertEquals(0, user3.getRoles().size());
		
		// department
		department = user3.getDepartment();

		verifyDepartment(legal.getName(), department);
	}
	
	@Test
	@Order(7)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("07. should load addresses without users")
	void testFindAll2() {

		LOGGER.info("running test testFindAll2");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(3, addresses.size());

		Address address = addresses.stream().filter(a -> a.getPostalCode().equals("31210")).findFirst()
				.get();

		verifyAddress("31210", "Via Roma", "Venice", "County Veneto", address);

		State state = address.getState();

		verifyState(italy.getName(), italy.getCode(), state);

		Set<User> users = address.getUsers();

		assertEquals(0, users.size());
	}
	
	void verifyDepartment(String name, Department actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertNotNull(actual.getUsers());
	}
	
	void verifyState(String name, String code, State actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(code, actual.getCode());
	}
	
	void verifyAddress(String postalCode, String description, String city, String region, Address actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
	}
		
	void verifyUser(String firstName, String lastName, LocalDate birthDate, User actual) {
		assertNotNull(actual.getId());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getDepartment());
		assertNotNull(actual.getAddresses());
		assertNotNull(actual.getRoles());
	}

}
