package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.impl.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jdbc.impl.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUser;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserRole;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.impl.operation.user.SelectRoleByRoleName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByAccountName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByFirstName;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;

	@Mock
	private InsertUser insertUser;

	@Mock
	private InsertUserAddress insertUserAddress;

	@Mock
	private InsertUserRole insertUserRole;

	@Mock
	private SelectAllUsers selectAllUsers;

	@Mock
	private SelectUserByAccountName selectUserByAccountName;

	@Mock
	private SelectUserByFirstName selectUserByFirstName;

	@Mock
	private SelectRoleByRoleName selectRoleByRoleName;

	@Mock
	private UpdateUser updateUser;

	@InjectMocks
	private UserDaoImpl userDao;

	UserDaoTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void testFindAll() {

		LOGGER.info("running test testFindAll");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()));
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withRoleName(ADMINISTRATOR.getRoleName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production);
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()));
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withRoleName(USER.getRoleName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal);
		artur.addRole(user);
		artur.addAddress(dublin);

		List<User> users = List.of(maxmin, artur);

		when(selectAllUsers.execute()).thenReturn(users);

		// run the test
		List<User> usersFound = userDao.findAll();

		verify(selectAllUsers, times(1)).execute();

		assertEquals(2, usersFound.size());

		User user1 = usersFound.get(0);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// roles
		assertEquals(1, user1.getRoles().size());

		Optional<Role> role1 = user1.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1.get());

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user1.getDepartment());

		// addresses
		assertEquals(1, user1.getAddresses().size());

		Optional<Address> address1 = user1.getAddress("30010");

		assertEquals(true, address1.isPresent());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1.get());
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.get().getState());

		User user2 = usersFound.get(1);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// roles
		assertEquals(1, user2.getRoles().size());

		Optional<Role> role2 = user2.getRole(USER.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2.get());

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user2.getDepartment());

		// addresses
		assertEquals(1, user2.getAddresses().size());

		Optional<Address> address2 = user2.getAddress("A65TF12");

		assertEquals(true, address2.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2.get());
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.get().getState());
	}

	@Test
	void findByAccountNameWithNoAccountNameThrowsException() {

		LOGGER.info("running test findByAccountNameWithNoAccountNameThrowsException");

		String accountName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> userDao.findByAccountName(accountName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void findByAccountName() {

		LOGGER.info("running test findByAccountName");

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()));
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withRoleName(USER.getRoleName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal);
		artur.addRole(user);
		artur.addAddress(dublin);

		when(selectUserByAccountName.execute("artur")).thenReturn(artur);

		// run the test
		Optional<User> userFound = userDao.findByAccountName("artur");

		verify(selectUserByAccountName, times(1)).execute("artur");

		assertEquals(true, userFound.isPresent());

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12),
				userFound.get());

		// roles
		assertEquals(1, userFound.get().getRoles().size());

		Optional<Role> role2 = userFound.get().getRole(USER.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2.get());

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), userFound.get().getDepartment());

		// addresses
		assertEquals(1, userFound.get().getAddresses().size());

		Optional<Address> address2 = userFound.get().getAddress("A65TF12");

		assertEquals(true, address2.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2.get());
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.get().getState());
	}

	@Test
	void findByFirstNameWithNoFirstNameThrowsException() {

		LOGGER.info("running test findByFirstNameWithNoFirstNameThrowsException");

		String firstName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> userDao.findByAccountName(firstName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void findByFirstName() {

		LOGGER.info("running test findByFirstName");

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()));
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withRoleName(USER.getRoleName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal);
		artur.addRole(user);
		artur.addAddress(dublin);

		List<User> users = List.of(artur);

		when(selectUserByFirstName.execute("Arturo")).thenReturn(users);

		// run the test
		List<User> usersFound = userDao.findByFirstName("Arturo");

		verify(selectUserByFirstName, times(1)).execute("Arturo");

		assertEquals(1, usersFound.size());

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12),
				usersFound.get(0));

		// roles
		assertEquals(1, usersFound.get(0).getRoles().size());

		Optional<Role> role2 = usersFound.get(0).getRole(USER.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2.get());

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), usersFound.get(0).getDepartment());

		// addresses
		assertEquals(1, usersFound.get(0).getAddresses().size());

		Optional<Address> address2 = usersFound.get(0).getAddress("A65TF12");

		assertEquals(true, address2.isPresent());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2.get());
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.get().getState());
	}

	@Test
	void createWithNoUserThrowsException() {

		LOGGER.info("running test createWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void createWithUserIdThrowsException() {

		LOGGER.info("running test createWithUserIdThrowsException");

		User user = User.newInstance().withId(1l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(1l).withName(ACCOUNTS.getName()));

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.create(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create() {

		LOGGER.info("running test createWithAddresses");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance().withId(1l));

		// run the test
		userDao.create(carl);

		verify(insertUser, times(1)).execute(carl);
	}

	@Test
	void associateAddressWithNoUserIdThrowsException() {

		LOGGER.info("running test associateAddressWithNoUserIdThrowsException");

		Long userId = null;
		Long addressId = 2l;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.associateAddress(userId, addressId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void associateAddressWithNoAddressIdThrowsException() {

		LOGGER.info("running test associateAddressWithNoAddressIdThrowsException");

		Long userId = 2l;
		Long addressId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.associateAddress(userId, addressId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void associateAddress() {

		LOGGER.info("running test associateAddress");

		userDao.associateAddress(1l, 2l);

		verify(insertUserAddress, times(1)).execute(1l, 2l);
	}

	@Test
	void associateRoleWithNoUserIdThrowsException() {

		LOGGER.info("running test associateAddressWithNoUserIdThrowsException");

		Long userId = null;
		Long roleId = 2l;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.associateRole(userId, roleId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void associateRoleWithNoRoleIdThrowsException() {

		LOGGER.info("running test associateRoleWithNoRoleIdThrowsException");

		Long userId = 2l;
		Long roleId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.associateRole(userId, roleId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void associateRole() {

		LOGGER.info("running test associateRole");

		userDao.associateRole(1l, 2l);

		verify(insertUserRole, times(1)).execute(1l, 2l);
	}

	@Test
	void updateWithNoUserThrowsException() {

		LOGGER.info("running test updateWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void updateWithNoUserIdThrowsException() {

		LOGGER.info("running test updateWithNoUserIdThrowsException");

		User user = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(1l).withName(ACCOUNTS.getName()));

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		LOGGER.info("running test update");

		User carl = User.newInstance().withId(2l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance().withId(1l));

		// run the test
		userDao.update(carl);

		verify(updateUser, times(1)).execute(carl);
	}
}
