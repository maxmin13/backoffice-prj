package it.maxmin.dao.hibernate.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.hibernate.HibernateDaoTestUtil;
import it.maxmin.dao.hibernate.HibernateTestCfg;
import it.maxmin.dao.hibernate.api.repo.UserDao;
import it.maxmin.domain.hibernate.entity.Address;
import it.maxmin.domain.hibernate.entity.Department;
import it.maxmin.domain.hibernate.entity.State;
import it.maxmin.domain.hibernate.entity.User;
import it.maxmin.domain.hibernate.entity.UserRole;
import it.maxmin.domain.hibernate.pojo.PojoAddress;
import it.maxmin.domain.hibernate.pojo.PojoUser;
import it.maxmin.domain.hibernate.pojo.PojoUserRole;

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
class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

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
	@Mock
	UserRole administrator;
	@Mock
	UserRole user;
	@Mock
	UserRole worker;

	@Autowired
	UserDao userDao;

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
		when(administrator.getId()).thenReturn(1l);
		when(administrator.getRoleName()).thenReturn("Administrator");
		when(user.getId()).thenReturn(2l);
		when(user.getRoleName()).thenReturn("User");
		when(worker.getId()).thenReturn(3l);
		when(worker.getRoleName()).thenReturn("Worker");
	}

	@Test
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(0, users.size());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void testFindAll() {

		LOGGER.info("running test testFindAll");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		assertNotNull(maxmin.getId());
		assertEquals("maxmin13", maxmin.getAccountName());
		assertEquals("Max", maxmin.getFirstName());
		assertEquals("Minardi", maxmin.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), maxmin.getBirthDate());
		assertNotNull(maxmin.getCreatedAt());
		assertNotNull(maxmin.getDepartment());
		assertNotNull(maxmin.getAddresses());
		assertNotNull(maxmin.getRoles());

		// roles
		Set<UserRole> roles = maxmin.getRoles();

		assertEquals(3, roles.size());

		UserRole role1 = maxmin.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = maxmin.getRole("User");

		assertNotNull(role2.getId());

		UserRole role3 = maxmin.getRole("Worker");

		assertNotNull(role3.getId());

		// department
		Department department = maxmin.getDepartment();

		assertNotNull(department.getId());
		assertEquals(production.getName(), department.getName());

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(2, addresses.size());

		Address address1 = maxmin.getAddress("30010");

		assertNotNull(address1.getId());
		assertEquals("Via borgo di sotto", address1.getDescription());
		assertEquals("Rome", address1.getCity());
		assertEquals("Lazio", address1.getRegion());
		assertEquals("30010", address1.getPostalCode());
		assertNotNull(address1.getState().getId());
		assertEquals(italy.getName(), address1.getState().getName());
		assertEquals(italy.getCode(), address1.getState().getCode());

		Address address2 = maxmin.getAddress("A65TF12");

		assertNotNull(address2.getId());
		assertEquals("Connolly street", address2.getDescription());
		assertNotNull(address2.getState().getId());
		assertEquals(ireland.getName(), address2.getState().getName());
		assertEquals(ireland.getCode(), address2.getState().getCode());
		
		User artur = users.get(1);

		assertNotNull(artur.getId());
		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getRoles());

		// roles
		roles = artur.getRoles();

		assertEquals(2, roles.size());

		role1 = artur.getRole("Administrator");

		assertNotNull(role1.getId());

		role2 = artur.getRole("User");

		assertNotNull(role2.getId());

		// department
		department = artur.getDepartment();

		assertNotNull(department.getId());
		assertEquals(legal.getName(), department.getName());

		// addresses
		addresses = artur.getAddresses();

		assertEquals(1, addresses.size());

		address1 = artur.getAddress("A65TF12");

		assertNotNull(address1.getId());
		assertEquals("Connolly street", address1.getDescription());
		assertNotNull(address1.getState().getId());
		assertEquals(ireland.getName(), address1.getState().getName());
		assertEquals(ireland.getCode(), address1.getState().getCode());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql", "classpath:database/2_useraddress.down.sql"   }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void testFindAll_with_no_address() {

		LOGGER.info("running test testFindAll");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		assertNotNull(maxmin.getId());
		assertEquals("maxmin13", maxmin.getAccountName());
		assertEquals("Max", maxmin.getFirstName());
		assertEquals("Minardi", maxmin.getLastName());
		assertEquals(LocalDate.of(1977, 10, 16), maxmin.getBirthDate());
		assertNotNull(maxmin.getCreatedAt());
		assertNotNull(maxmin.getDepartment());
		assertNotNull(maxmin.getAddresses());
		assertNotNull(maxmin.getRoles());

		// roles
		Set<UserRole> roles = maxmin.getRoles();

		assertEquals(3, roles.size());

		UserRole role1 = maxmin.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = maxmin.getRole("User");

		assertNotNull(role2.getId());

		UserRole role3 = maxmin.getRole("Worker");

		assertNotNull(role3.getId());

		// department
		Department department = maxmin.getDepartment();

		assertNotNull(department.getId());
		assertEquals(production.getName(), department.getName());

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(0, addresses.size());
		
		User artur = users.get(1);

		assertNotNull(artur.getId());
		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getRoles());

		// roles
		roles = artur.getRoles();

		assertEquals(2, roles.size());

		role1 = artur.getRole("Administrator");

		assertNotNull(role1.getId());

		role2 = artur.getRole("User");

		assertNotNull(role2.getId());

		// department
		department = artur.getDepartment();

		assertNotNull(department.getId());
		assertEquals(legal.getName(), department.getName());

		// addresses
		addresses = artur.getAddresses();

		assertEquals(0, addresses.size());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql"   }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void findByAccountNameNotFound() {

		LOGGER.info("running test findByAccountNameNotFound");

		// run the test
		Optional<User> user = userDao.findByAccountName("none");

		assertTrue(user.isEmpty());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql"   }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void findByAccountName() {

		LOGGER.info("running test findByAccountName");

		// run the test
		User artur =  userDao.findByAccountName("artur").get();

		assertNotNull(artur.getId());
		assertEquals("artur", artur.getAccountName());
		assertEquals("Arturo", artur.getFirstName());
		assertEquals("Art", artur.getLastName());
		assertEquals(LocalDate.of(1923, 10, 12), artur.getBirthDate());
		assertNotNull(artur.getCreatedAt());
		assertNotNull(artur.getDepartment());
		assertNotNull(artur.getAddresses());
		assertNotNull(artur.getRoles());

		// roles
		Set<UserRole> roles = artur.getRoles();

		assertEquals(2, roles.size());

		UserRole role1 = artur.getRole("Administrator");

		assertNotNull(role1.getId());

		UserRole role2 = artur.getRole("User");

		assertNotNull(role2.getId());

		// department
		Department department = artur.getDepartment();

		assertNotNull(department.getId());
		assertEquals(legal.getName(), department.getName());

		// addresses
		Set<Address> addresses = artur.getAddresses();

		assertEquals(1, addresses.size());

		Address address1 = artur.getAddress("A65TF12");

		assertNotNull(address1.getId());
		assertEquals("Connolly street", address1.getDescription());
		assertNotNull(address1.getState().getId());
		assertEquals(ireland.getName(), address1.getState().getName());
		assertEquals(ireland.getCode(), address1.getState().getCode());
	}

	@Test
	void nullCreateThrowsException() {

		LOGGER.info("running test nullCreateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.save(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void create_with_no_address_and_no_role() {

		LOGGER.info("running test create_with_no_address_and_no_role");

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartment(Department.newInstance().withId(legal.getId()));

		// run the test
		userDao.save(franco);

		PojoUser newUser = daoTestUtil.findUserByAccountName("franc123");

		assertEquals("Franco", newUser.getFirstName());
		assertEquals("Red", newUser.getLastName());
		assertEquals(legal.getId(), newUser.getDepartmentId());
		assertEquals(LocalDate.of(1981, 11, 12), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedAt());
		assertNotNull(newUser.getId());

		List<PojoAddress> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());
		
		List<PojoUserRole> roles = daoTestUtil.findRolesByUserId(newUser.getId());

		assertEquals(0, roles.size());
	}

	@Test
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void create_with_address_and_role() {

		LOGGER.info("running test create_with_address_and_role");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance().withId(accounts.getId()));

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Emilia Romagna")
				.withPostalCode("33456");
		carl.addAddress(address1);

		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address2);
		
		UserRole role1 = UserRole.newInstance().withId(worker.getId()).withRoleName(worker.getRoleName());
		carl.addRole(role1);
		UserRole role2 = UserRole.newInstance().withId(administrator.getId()).withRoleName(administrator.getRoleName());
		carl.addRole(role2);
		
		// run the test
		userDao.save(carl);

		PojoUser newUser = daoTestUtil.findUserByAccountName("carl23");

		assertEquals("carl23", newUser.getAccountName());
		assertEquals("Carlo", newUser.getFirstName());
		assertEquals("Rossi", newUser.getLastName());
		assertEquals(LocalDate.of(1982, 9, 1), newUser.getBirthDate());
		assertNotNull(newUser.getCreatedAt());
		assertNotNull(newUser.getId());
		
		List<PojoAddress> addresses = daoTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		assertEquals("Via Vecchia", newAddress1.getDescription());
		assertEquals("Dublin", newAddress1.getCity());
		assertEquals(ireland.getId(), newAddress1.getStateId());
		assertEquals("County Dublin", newAddress1.getRegion());
		assertEquals("A65TF14", newAddress1.getPostalCode());

		PojoAddress newAddress2 = addresses.get(1);

		assertEquals("Via Nuova", newAddress2.getDescription());
		assertEquals("Venice", newAddress2.getCity());
		assertEquals(italy.getId(), newAddress2.getStateId());
		assertEquals("Emilia Romagna", newAddress2.getRegion());
		assertEquals("33456", newAddress2.getPostalCode());
		
		List<PojoUserRole> roles = daoTestUtil.findRolesByUserId(newUser.getId());
		
		assertEquals(2, roles.size());
		
		PojoUserRole newRole1 = roles.get(0);
		assertEquals(administrator.getRoleName(), newRole1.getRoleName());
		
		PojoUserRole newRole2 = roles.get(1);
		assertEquals(worker.getRoleName(), newRole2.getRoleName());
	}


	// @Test
	void nullUpdateThrowsException() {

		LOGGER.info("running test nullUpdateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.save(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	void update_user() {}
	
	void update_user_address() {}
	
	void delete_user_address() {}

	void update_user_role() {}
	
	void delete_user_role() {}
	
	
	//	// @Test
//	void update() {
//
//		LOGGER.info("running test update");
//
//		PojoUser stephan = PojoUser.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1970, 2, 3))
//				.withFirstName("Stephano").withLastName("Regi").withDepartmentId(accounts.getId());
//
//		long stephanId = daoTestUtil.createUser(stephan).getId();
//		LocalDateTime createdAt = daoTestUtil.findUserByUserId(stephanId).getCreatedAt();
//
//		User stephanUpdated = User.newInstance().withAccountName("stephan123").withBirthDate(LocalDate.of(1980, 12, 4))
//				.withFirstName("Stephano juniur").withLastName("Reginaldo").withDepartment(legal).withId(stephanId);
//
//		// run the test
//		userDao.update(stephanUpdated);
//
//		PojoUser updated = daoTestUtil.findUserByUserId(stephanId);
//
//		assertEquals("stephan123", updated.getAccountName());
//		assertEquals("Stephano juniur", updated.getFirstName());
//		assertEquals("Reginaldo", updated.getLastName());
//		assertEquals(LocalDate.of(1980, 12, 4), updated.getBirthDate());
//		assertEquals(createdAt, updated.getCreatedAt());
//	}
}
