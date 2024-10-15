package it.maxmin.dao.jpa.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.LazyInitializationException;
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

import it.maxmin.dao.jpa.QueryTestUtil;
import it.maxmin.dao.jpa.UnitTestContextCfg;
import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.domain.jpa.entity.Address;
import it.maxmin.domain.jpa.entity.Department;
import it.maxmin.domain.jpa.entity.State;
import it.maxmin.domain.jpa.entity.User;
import it.maxmin.domain.jpa.entity.UserRole;
import it.maxmin.domain.jpa.pojo.PojoAddress;
import it.maxmin.domain.jpa.pojo.PojoDepartment;
import it.maxmin.domain.jpa.pojo.PojoState;
import it.maxmin.domain.jpa.pojo.PojoUser;
import it.maxmin.domain.jpa.pojo.PojoUserRole;

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
class UserDaoTest extends BaseTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	@Autowired
	QueryTestUtil queryTestUtil;

	@Autowired
	UserDao userDao;

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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
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
		verifyDepartment(production.getName(), department);

		User artur = users.get(1);

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// department
		department = artur.getDepartment();
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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("04. verify lazily loaded properties in an open transaction")
	void testFindAll3() {

		LOGGER.info("running test testFindAll3");

		// run the test
		List<User> users = userDao.findAll();

		assertEquals(2, users.size());

		User maxmin = users.get(0);

		// roles
		Set<UserRole> roles = maxmin.getRoles();

		assertEquals(3, roles.size());

		UserRole role1 = maxmin.getRole(administrator.getRoleName());
		verifyRole(administrator.getRoleName(), role1);

		UserRole role2 = maxmin.getRole(user.getRoleName());
		verifyRole(user.getRoleName(), role2);

		UserRole role3 = maxmin.getRole(worker.getRoleName());
		verifyRole(worker.getRoleName(), role3);

		// addresses
		Set<Address> addresses = maxmin.getAddresses();

		assertEquals(2, addresses.size());

		Address address = maxmin.getAddress("30010");

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address);
		
		State state1 = address.getState();
		
		verifyState(italy.getName(), italy.getCode(), state1);

		Address address2 = maxmin.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		
		State state2 = address2.getState();
		
		verifyState(ireland.getName(), ireland.getCode(), state2);

		User artur = users.get(1);

		// roles
		roles = artur.getRoles();

		assertEquals(2, roles.size());

		assertEquals(2, roles.size());

		UserRole role4 = artur.getRole(administrator.getRoleName());
		verifyRole(administrator.getRoleName(), role4);

		UserRole role5 = artur.getRole(user.getRoleName());
		verifyRole(user.getRoleName(), role5);

		// addresses
		addresses = artur.getAddresses();

		assertEquals(1, addresses.size());

		address = artur.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address);
		
		State state = address.getState();
		
		verifyState(ireland.getName(), ireland.getCode(), state);
	}	

	@Test
	@Order(5)
	@Sql(scripts = { "classpath:database/2_address.up.sql",
			"classpath:database/2_user.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
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
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("06. should find a user")
	void findByAccountName() {

		LOGGER.info("running test findByAccountName");

		// run the test
		User artur = userDao.findByAccountName("artur").get();

		verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), artur);

		// roles
		Set<UserRole> roles = artur.getRoles();

		assertEquals(2, roles.size());

		UserRole role1 = artur.getRole(administrator.getRoleName());
		verifyRole(administrator.getRoleName(), role1);

		UserRole role2 = artur.getRole(user.getRoleName());
		verifyRole(user.getRoleName(), role2);

		// department
		Department department = artur.getDepartment();
		verifyDepartment(legal.getName(), department);

		// addresses
		Set<Address> addresses = artur.getAddresses();

		assertEquals(1, addresses.size());

		Address address = artur.getAddress("A65TF12");

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address);
		
		State state = address.getState();
		
		verifyState(ireland.getName(), ireland.getCode(), state);
	}

	@Test
	@Order(7)
	void nullCreateThrowsException() {

		LOGGER.info("running test nullCreateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.save(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@Order(8)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("08. should create a user without addresses and roles")
	void createWithNoAddressAndWithNoRole() {

		LOGGER.info("running test createWithNoAddressAndWithNoRole");

		PojoDepartment department = queryTestUtil.findDepartmentByName(legal.getName());

		User franco = User.newInstance().withAccountName("franc123").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red")
				.withDepartment(Department.newInstance().withId(department.getId()));

		// run the test
		userDao.save(franco);

		PojoUser newUser = queryTestUtil.findUserByAccountName("franc123");

		verifyUser("franc123", "Franco", "Red", LocalDate.of(1981, 11, 12), newUser);

		PojoDepartment newDepartment = queryTestUtil.findDepartmentByUserAccountName("franc123");

		verifyDepartment(legal.getName(), newDepartment);

		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(0, addresses.size());

		List<PojoUserRole> roles = queryTestUtil.findRolesByUserId(newUser.getId());

		assertEquals(0, roles.size());
	}

	@Test
	@Order(9)
	@Sql(scripts = { "classpath:database/2_useruserrole.down.sql", "classpath:database/2_useraddress.down.sql",
			"classpath:database/2_user.down.sql",
			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("09. should create a user with addresses and roles")
	void createWithAddressAndWithRole() {

		LOGGER.info("running test createWithAddressAndWithRole");

		PojoDepartment department = queryTestUtil.findDepartmentByName(accounts.getName());

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(department.getId()));

		PojoUserRole role = queryTestUtil.findRoleByName(administrator.getRoleName());

		carl.addRole(UserRole.newInstance().withId(role.getId()));

		PojoState state1 = queryTestUtil.findStateByName(italy.getName());

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(state1.getId())).withRegion("Emilia Romagna")
				.withPostalCode("33456");
		carl.addAddress(address1);

		PojoState state2 = queryTestUtil.findStateByName(ireland.getName());

		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withState(State.newInstance().withId(state2.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF14");
		carl.addAddress(address2);

		// run the test
		userDao.save(carl);

		PojoUser newUser = queryTestUtil.findUserByAccountName("carl23");

		verifyUser("carl23", "Carlo", "Rossi", LocalDate.of(1982, 9, 1), newUser);

		PojoDepartment userDepartment = queryTestUtil.findDepartmentByUserAccountName("carl23");

		verifyDepartment(accounts.getName(), userDepartment);

		List<PojoUserRole> userRoles = queryTestUtil.findRolesByUserAccountName("carl23");

		assertEquals(1, userRoles.size());

		verifyRole(administrator.getRoleName(), userRoles.get(0));

		List<PojoAddress> addresses = queryTestUtil.findAddressesByUserId(newUser.getId());

		assertEquals(2, addresses.size());

		PojoAddress newAddress1 = addresses.get(0);

		verifyAddress("A65TF14", "Via Vecchia", "Dublin", "County Dublin", newAddress1);

		PojoState newState1 = queryTestUtil.findStateByAddressPostalCode("33456");

		verifyState(italy.getName(), italy.getCode(), newState1);

		PojoAddress newAddress2 = addresses.get(1);

		verifyAddress("33456", "Via Nuova", "Venice", "Emilia Romagna", newAddress2);

		PojoState newState2 = queryTestUtil.findStateByAddressPostalCode("A65TF14");

		verifyState(ireland.getName(), ireland.getCode(), newState2);
	}

	@Test
	@Order(10)
	void nullUpdateThrowsException() {

		LOGGER.info("running test nullUpdateThrowsException");

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.save(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	void update_user() {
		// verify version field
	}

	void update_user_address() {
	}

	void delete_user_address() {
	}

	void update_user_role() {
	}

	void delete_user_role() {
	}

	// // @Test
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
