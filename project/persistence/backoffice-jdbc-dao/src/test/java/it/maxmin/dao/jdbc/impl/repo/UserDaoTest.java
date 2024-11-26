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

import it.maxmin.dao.jdbc.DaoTestException;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.impl.operation.role.SelectRoleByRoleName;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUser;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserRole;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
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
	void testselectAll() {

		LOGGER.info("running test testselectAll");

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
		List<User> usersFound = userDao.selectAll();

		verify(selectAllUsers, times(1)).execute();

		assertEquals(2, usersFound.size());

		User user1 = usersFound.get(0);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// roles
		assertEquals(1, user1.getRoles().size());

		Optional<Role> role1 = user1.getRole(ADMINISTRATOR.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), r1);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user1.getDepartment());

		// addresses
		assertEquals(1, user1.getAddresses().size());

		Optional<Address> address1 = user1.getAddress("30010");
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", a1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), a1.getState());

		User user2 = usersFound.get(1);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// roles
		assertEquals(1, user2.getRoles().size());

		Optional<Role> role2 = user2.getRole(USER.getRoleName());
		Role r2 = role2.orElseThrow(() -> new DaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), r2);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user2.getDepartment());

		// addresses
		assertEquals(1, user2.getAddresses().size());

		Optional<Address> address2 = user2.getAddress("A65TF12");
		Address a2 = address2.orElseThrow(() -> new DaoTestException("Error address not found"));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a2);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), a2.getState());
	}

	@Test
	void selectByAccountNameWithNoAccountNameThrowsException() {

		LOGGER.info("running test selectByAccountNameWithNoAccountNameThrowsException");

		String accountName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> userDao.selectByAccountName(accountName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectByAccountName() {

		LOGGER.info("running test selectByAccountName");

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()));
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withRoleName(USER.getRoleName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal);
		artur.addRole(user);
		artur.addAddress(dublin);

		when(selectUserByAccountName.execute("artur")).thenReturn(Optional.of(artur));

		// run the test
		Optional<User> userFound = userDao.selectByAccountName("artur");
		User u = userFound.orElseThrow(() -> new DaoTestException("Error user not found"));

		verify(selectUserByAccountName, times(1)).execute("artur");

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), u);

		// roles
		assertEquals(1, u.getRoles().size());

		Optional<Role> role1 = u.getRole(USER.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), r1);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), u.getDepartment());

		// addresses
		assertEquals(1, u.getAddresses().size());

		Optional<Address> address1 = u.getAddress("A65TF12");
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a1);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), a1.getState());
	}

	@Test
	void selectByFirstNameWithNoFirstNameThrowsException() {

		LOGGER.info("running test selectByFirstNameWithNoFirstNameThrowsException");

		String firstName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> userDao.selectByAccountName(firstName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectByFirstName() {

		LOGGER.info("running test selectByFirstName");

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
		List<User> usersFound = userDao.selectByFirstName("Arturo");

		verify(selectUserByFirstName, times(1)).execute("Arturo");

		assertEquals(1, usersFound.size());

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12),
				usersFound.get(0));

		// roles
		assertEquals(1, usersFound.get(0).getRoles().size());

		Optional<Role> role1 = usersFound.get(0).getRole(USER.getRoleName());
		Role r1 = role1.orElseThrow(() -> new DaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), r1);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), usersFound.get(0).getDepartment());

		// addresses
		assertEquals(1, usersFound.get(0).getAddresses().size());

		Optional<Address> address1 = usersFound.get(0).getAddress("A65TF12");
		Address a1 = address1.orElseThrow(() -> new DaoTestException("Error address not found"));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", a1);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), a1.getState());
	}

	@Test
	void insertWithNoUserThrowsException() {

		LOGGER.info("running test insertWithNoUserThrowsException");

		User user = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.insert(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void insertWithUserIdThrowsException() {

		LOGGER.info("running test insertWithUserIdThrowsException");

		User user = User.newInstance().withId(1l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi")
				.withDepartment(Department.newInstance().withId(1l).withName(ACCOUNTS.getName()));

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.insert(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void insert() {

		LOGGER.info("running test insertWithAddresses");

		User carl = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance().withId(1l));

		// run the test
		userDao.insert(carl);

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
