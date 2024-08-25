package it.maxmin.dao.hibernate.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
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

import it.maxmin.dao.hibernate.HibernateDaoTestUtil;
import it.maxmin.dao.hibernate.HibernateTestCfg;
import it.maxmin.dao.hibernate.api.repo.AddressDao;
import it.maxmin.domain.hibernate.entity.Address;
import it.maxmin.domain.hibernate.entity.Department;
import it.maxmin.domain.hibernate.entity.State;
import it.maxmin.domain.hibernate.entity.User;
import it.maxmin.domain.hibernate.entity.UserRole;
import it.maxmin.domain.hibernate.pojo.PojoAddress;
import jakarta.transaction.Transactional;

@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = { "classpath:database/1_create_database.up.sql", "classpath:database/2_userrole.up.sql",
		"classpath:database/2_state.up.sql",
		"classpath:database/2_department.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/2_state.down.sql", "classpath:database/2_department.down.sql",
		"classpath:database/2_userrole.down.sql",
		"classpath:database/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringJUnitConfig(classes = { HibernateTestCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	@Autowired
	HibernateDaoTestUtil daoTestUtil;

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
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Order(1)
	@DisplayName("01. verify eagerly loaded properties")
	void findById1() {

		LOGGER.info("running test findById1");

		PojoAddress pojoAddress = daoTestUtil.findAddressByPostalCode("A65TF12");
		Long addressId = pojoAddress.getId();

		// run the test
		Address address = addressDao.findById(addressId);

		assertNotNull(address);
		assertNotNull(address.getId());
		assertEquals(pojoAddress.getDescription(), address.getDescription());
		assertEquals(pojoAddress.getCity(), address.getCity());
		assertEquals(pojoAddress.getRegion(), address.getRegion());

		State state = address.getState();

		assertNotNull(state.getId());
		assertEquals(ireland.getName(), state.getName());
		assertEquals(ireland.getCode(), state.getCode());

		Set<User> users = address.getUsers();

		assertEquals(2, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		assertNotNull(user1.getId());
		assertEquals("Max", user1.getFirstName());
		assertEquals("Minardi", user1.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), user1.getBirthDate());
		assertNotNull(user1.getCreatedAt());
		assertNotNull(user1.getDepartment());
		assertNotNull(user1.getAddresses());
		assertNotNull(user1.getRoles());

		// department
		Department department = user1.getDepartment();

		assertNotNull(department.getId());
		assertEquals(production.getName(), department.getName());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		assertNotNull(user2.getId());
		assertEquals("Arturo", user2.getFirstName());
		assertEquals("Art", user2.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), user2.getBirthDate());
		assertNotNull(user2.getCreatedAt());
		assertNotNull(user2.getDepartment());
		assertNotNull(user2.getAddresses());
		assertNotNull(user2.getRoles());

		// department
		department = user2.getDepartment();

		assertNotNull(department.getId());
		assertEquals(legal.getName(), department.getName());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Order(2)
	@DisplayName("02. verify lazily loaded properties")
	void findById2() {

		LOGGER.info("running test findById2");

		PojoAddress pojoAddress = daoTestUtil.findAddressByPostalCode("A65TF12");
		Long addressId = pojoAddress.getId();

		// run the test
		Address address = addressDao.findById(addressId);

		Set<User> users = address.getUsers();

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// roles
		Set<UserRole> roles = user1.getRoles();

		assertThrows(LazyInitializationException.class, roles::size);

		// addresses
		Set<Address> addresses = user1.getAddresses();

		assertThrows(LazyInitializationException.class, addresses::size);
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	@Order(3)
	@DisplayName("03. should load lazily loaded collections in an open transaction")
	void findById3() {

		LOGGER.info("running test findById3");

		PojoAddress pojoAddress = daoTestUtil.findAddressByPostalCode("A65TF12");
		Long addressId = pojoAddress.getId();

		// run the test
		Address address = addressDao.findById(addressId);

		Set<User> users = address.getUsers();
		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// roles
		Set<UserRole> roles = user1.getRoles();

		assertEquals(3, roles.size());

		UserRole role1 = user1.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = user1.getRole("User");

		assertNotNull(role2.getId());

		UserRole role3 = user1.getRole("Worker");

		assertNotNull(role3.getId());

		// addresses
		Set<Address> addresses = user1.getAddresses();

		assertEquals(2, addresses.size());

		Address address1 = user1.getAddress("30010");

		assertNotNull(address1.getId());
		assertEquals("Via borgo di sotto", address1.getDescription());
		assertEquals("Rome", address1.getCity());
		assertEquals("Lazio", address1.getRegion());
		assertEquals("30010", address1.getPostalCode());
		assertNotNull(address1.getState().getId());
		assertEquals(italy.getName(), address1.getState().getName());
		assertEquals(italy.getCode(), address1.getState().getCode());

		Address address2 = user1.getAddress("A65TF12");

		assertNotNull(address2.getId());
		assertEquals("Connolly street", address2.getDescription());
		assertNotNull(address2.getState().getId());
		assertEquals(ireland.getName(), address2.getState().getName());
		assertEquals(ireland.getCode(), address2.getState().getCode());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		// roles
		roles = user2.getRoles();

		assertEquals(2, roles.size());

		role1 = user2.getRole("Administrator");

		assertNotNull(role1.getId());

		role2 = user2.getRole("User");

		assertNotNull(role2.getId());

		// addresses
		addresses = user2.getAddresses();

		assertEquals(1, addresses.size());

		Address address3 = user2.getAddress("A65TF12");

		assertNotNull(address1.getId());
		assertEquals("Connolly street", address3.getDescription());
		assertNotNull(address3.getState().getId());
		assertEquals(ireland.getName(), address3.getState().getName());
		assertEquals(ireland.getCode(), address3.getState().getCode());
	}

	@Test
	@Order(4)
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(0, addresses.size());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Order(5)
	@DisplayName("05. should load expected properties")
	void testFindAll() {

		LOGGER.info("running test testFindAll");

		// run the test
		Set<Address> addresses = addressDao.findAll();

		assertEquals(2, addresses.size());

		Address address1 = addresses.stream().filter(address -> address.getPostalCode().equals("30010")).findFirst()
				.get();

		assertNotNull(address1.getId());
		assertNull(address1.getDescription());
		assertNull(address1.getCity());
		assertNull(address1.getRegion());

		State state = address1.getState();

		assertNotNull(state.getId());
		assertNull(state.getName());
		assertEquals(italy.getCode(), state.getCode());

		Set<User> users = address1.getUsers();

		assertEquals(1, users.size());

		User user1 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		assertNotNull(user1.getId());
		assertNull(user1.getFirstName());
		assertNull(user1.getLastName());
		assertNull(user1.getBirthDate());
		assertNull(user1.getCreatedAt());
		assertNull(user1.getDepartment());
		assertEquals(0, user1.getAddresses().size());
		assertEquals(0, user1.getRoles().size());

		Address address2 = addresses.stream().filter(address -> address.getPostalCode().equals("A65TF12")).findFirst()
				.get();

		assertNotNull(address2.getId());
		assertNull(address2.getDescription());
		assertNull(address2.getCity());
		assertNull(address2.getRegion());

		state = address2.getState();

		assertNotNull(state.getId());
		assertNull(state.getName());
		assertEquals(ireland.getCode(), state.getCode());

		users = address2.getUsers();

		assertEquals(2, users.size());

		User user2 = users.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		assertNotNull(user2.getId());
		assertNull(user2.getFirstName());
		assertNull(user2.getLastName());
		assertNull(user2.getBirthDate());
		assertNull(user2.getCreatedAt());
		assertNull(user2.getDepartment());
		assertEquals(0, user2.getAddresses().size());
		assertEquals(0, user2.getRoles().size());

		User user3 = users.stream().filter(u -> u.getAccountName().equals("artur")).findFirst().get();

		assertNotNull(user3.getId());
		assertNull(user3.getFirstName());
		assertNull(user3.getLastName());
		assertNull(user3.getBirthDate());
		assertNull(user3.getCreatedAt());
		assertNull(user3.getDepartment());
		assertEquals(0, user3.getAddresses().size());
		assertEquals(0, user3.getRoles().size());
	}

	@Order(6)
	public void findAddressesByAccountNameNotFound() {

		LOGGER.info("running test findAddressesByAccountNameNotFound");

		// run the test
		List<Address> addresses = addressDao.findAddressesByAccountName("maxmin13");

		assertEquals(0, addresses.size());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Order(7)
	@DisplayName("07. verify eagerly loaded properties")
	void findAddressesByAccountName1() {

		LOGGER.info("running test findAddressesByAccountName1");

		// run the test
		List<Address> addresses = addressDao.findAddressesByAccountName("maxmin13");

		assertEquals(2, addresses.size());

		Address address1 = addresses.stream().filter(a -> a.getPostalCode().equals("30010")).findFirst().get();

		assertNotNull(address1.getId());
		assertEquals("Via borgo di sotto", address1.getDescription());
		assertEquals("Rome", address1.getCity());
		assertEquals("Lazio", address1.getRegion());

		// state
		assertNotNull(address1.getState().getId());
		assertEquals(italy.getName(), address1.getState().getName());
		assertEquals(italy.getCode(), address1.getState().getCode());

		// users
		Set<User> users1 = address1.getUsers();

		assertEquals(1, users1.size());

		User user1 = users1.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		assertNotNull(user1.getId());
		assertNotNull(user1.getDepartment().getId());

		Address address2 = addresses.stream().filter(a -> a.getPostalCode().equals("A65TF12")).findFirst().get();

		assertNotNull(address2.getId());
		assertEquals("Connolly street", address2.getDescription());

		// state
		assertNotNull(address2.getState().getId());
		assertEquals(ireland.getName(), address2.getState().getName());
		assertEquals(ireland.getCode(), address2.getState().getCode());

		// users
		Set<User> users2 = address2.getUsers();

		assertEquals(1, users2.size());

		User user2 = users2.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		assertNotNull(user2.getId());
		assertNotNull(user2.getDepartment().getId());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Order(8)
	@DisplayName("08. verify lazily loaded properties")
	void findAddressesByAccountName2() {

		LOGGER.info("running test findAddressesByAccountName2");

		// run the test
		List<Address> addresses = addressDao.findAddressesByAccountName("maxmin13");

		Address address1 = addresses.stream().filter(a -> a.getPostalCode().equals("30010")).findFirst().get();

		Set<User> users1 = address1.getUsers();

		User user1 = users1.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// addresses
		assertThrows(LazyInitializationException.class, user1.getAddresses()::size);

		// roles
		assertThrows(LazyInitializationException.class, user1.getRoles()::size);

		Address address2 = addresses.stream().filter(a -> a.getPostalCode().equals("A65TF12")).findFirst().get();

		Set<User> users2 = address2.getUsers();

		User user2 = users2.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// addresses
		assertThrows(LazyInitializationException.class, user2.getAddresses()::size);

		// roles
		assertThrows(LazyInitializationException.class, user2.getRoles()::size);
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@Transactional
	@Order(9)
	@DisplayName("09. should load lazily loaded collections in an open transaction")
	void findAddressesByAccountName3() {

		LOGGER.info("running test findAddressesByAccountName3");

		// run the test
		List<Address> addresses = addressDao.findAddressesByAccountName("maxmin13");

		Address address1 = addresses.stream().filter(a -> a.getPostalCode().equals("30010")).findFirst().get();

		Set<User> users1 = address1.getUsers();

		User user1 = users1.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// addresses
		assertThrows(LazyInitializationException.class, user1.getAddresses()::size);

		// roles
		assertThrows(LazyInitializationException.class, user1.getRoles()::size);

		Address address2 = addresses.stream().filter(a -> a.getPostalCode().equals("A65TF12")).findFirst().get();

		Set<User> users2 = address2.getUsers();

		User user2 = users2.stream().filter(u -> u.getAccountName().equals("maxmin13")).findFirst().get();

		// addresses
		assertThrows(LazyInitializationException.class, user2.getAddresses()::size);

		// roles
		assertThrows(LazyInitializationException.class, user2.getRoles()::size);
	}
}
