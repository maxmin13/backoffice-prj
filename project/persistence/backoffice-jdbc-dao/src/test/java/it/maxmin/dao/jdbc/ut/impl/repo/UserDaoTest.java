package it.maxmin.dao.jdbc.ut.impl.repo;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.Department.ACCOUNTING;
import static it.maxmin.dao.jdbc.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.constant.Role.USER;
import static it.maxmin.dao.jdbc.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.dao.jdbc.impl.operation.role.SelectRoleByName;
import it.maxmin.dao.jdbc.impl.operation.user.DeleteUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.DeleteUserAddresses;
import it.maxmin.dao.jdbc.impl.operation.user.DeleteUserRole;
import it.maxmin.dao.jdbc.impl.operation.user.DeleteUserRoles;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUser;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserAddress;
import it.maxmin.dao.jdbc.impl.operation.user.InsertUserRole;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByAccountName;
import it.maxmin.dao.jdbc.impl.operation.user.SelectUserByFirstName;
import it.maxmin.dao.jdbc.impl.operation.user.UpdateUser;
import it.maxmin.dao.jdbc.impl.repo.UserDaoImpl;
import it.maxmin.dao.jdbc.util.JdbcUserTestUtil;
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
	private DeleteUserAddress deleteUserAddress;
	@Mock
	private DeleteUserAddresses deleteUserAddresses;
	@Mock
	private InsertUserRole insertUserRole;
	@Mock
	private DeleteUserRole deleteUserRole;
	@Mock
	private DeleteUserRoles deleteUserRoles;
	@Mock
	private SelectAllUsers selectAllUsers;

	@Mock
	private SelectUserByAccountName selectUserByAccountName;

	@Mock
	private SelectUserByFirstName selectUserByFirstName;

	@Mock
	private SelectRoleByName selectRoleByName;

	@Mock
	private UpdateUser updateUser;

	@InjectMocks
	private UserDaoImpl userDao;

	UserDaoTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void selectAll() {

		LOGGER.info("running test selectAll");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withVersion(1);
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withName(ADMINISTRATOR.getName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production)
				.withVersion(2).withCreatedAt(LocalDateTime.now());
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withVersion(3);
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withName(USER.getName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal).withVersion(4)
				.withCreatedAt(LocalDateTime.now());
		artur.addRole(user);
		artur.addAddress(dublin);

		List<User> users = List.of(maxmin, artur);

		when(selectAllUsers.execute()).thenReturn(users);

		// run the test
		List<User> usersFound = userDao.selectAll();

		verify(selectAllUsers, times(1)).execute();

		assertEquals(2, usersFound.size());

		User user1 = usersFound.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// roles
		assertEquals(1, user1.getRoles().size());

		Role role1 = user1.getRole(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JdbcDaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user1.getDepartment());

		// addresses
		assertEquals(1, user1.getAddresses().size());

		Address address1 = user1.getAddress("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());

		User user2 = usersFound.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// roles
		assertEquals(1, user2.getRoles().size());

		Role role2 = user2.getRole(USER.getName()).orElseThrow(() -> new JdbcDaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(USER.getName(), role2);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user2.getDepartment());

		// addresses
		assertEquals(1, user2.getAddresses().size());

		Address address2 = user2.getAddress("A65TF12")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.getState());
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

		// prepare a user
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12))
				.withDepartment(Department.newInstance().withId(3l).withName(LEGAL.getName())).withVersion(1)
				.withCreatedAt(LocalDateTime.now());
		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withVersion(2);
		artur.addRole(Role.newInstance().withId(2l).withName(USER.getName()));
		artur.addAddress(dublin);

		when(selectUserByAccountName.execute("artur")).thenReturn(Optional.of(artur));

		// run the test
		User user = userDao.selectByAccountName("artur")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		verify(selectUserByAccountName, times(1)).execute("artur");

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user);

		// roles
		assertEquals(1, user.getRoles().size());

		Role role1 = user.getRole(USER.getName()).orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(USER.getName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user.getDepartment());

		// addresses
		assertEquals(1, user.getAddresses().size());

		Address address1 = user.getAddress("A65TF12")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address1);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address1.getState());
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
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withVersion(1);
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withName(USER.getName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal).withVersion(2)
				.withCreatedAt(LocalDateTime.now());
		artur.addRole(user);
		artur.addAddress(dublin);

		List<User> users = List.of(artur);

		when(selectUserByFirstName.execute("Arturo")).thenReturn(users);

		// run the test
		List<User> usersFound = userDao.selectByFirstName("Arturo");

		verify(selectUserByFirstName, times(1)).execute("Arturo");

		assertEquals(1, usersFound.size());

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), usersFound.get(0));

		// roles
		assertEquals(1, usersFound.get(0).getRoles().size());

		Role role1 = usersFound.get(0).getRole(USER.getName())
				.orElseThrow(() -> new JdbcDaoTestException("Error role not found"));

		jdbcUserTestUtil.verifyRole(USER.getName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), usersFound.get(0).getDepartment());

		// addresses
		assertEquals(1, usersFound.get(0).getAddresses().size());

		Address address1 = usersFound.get(0).getAddress("A65TF12")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address1);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address1.getState());
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
	void removeAddressWithNoUserIdThrowsException() {

		LOGGER.info("running test removeAddressWithNoUserIdThrowsException");

		Long userId = null;
		Long addressId = 2l;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeAddress(userId, addressId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeAddressWithNoAddressIdThrowsException() {

		LOGGER.info("running test removeAddressWithNoAddressIdThrowsException");

		Long userId = 2l;
		Long addressId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeAddress(userId, addressId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeAddress() {

		LOGGER.info("running test removeAddress");

		userDao.removeAddress(1l, 2l);

		verify(deleteUserAddress, times(1)).execute(1l, 2l);
	}

	@Test
	void removeAllAddressesWithNoUserIdThrowsException() {

		LOGGER.info("running test removeAllAddressesWithNoUserIdThrowsException");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeAllAddresses(userId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeAllAddresses() {

		LOGGER.info("running test removeAllAddresses");

		userDao.removeAllAddresses(1l);

		verify(deleteUserAddresses, times(1)).execute(1l);
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
	void removeRoleWithNoUserIdThrowsException() {

		LOGGER.info("running test removeRoleWithNoUserIdThrowsException");

		Long userId = null;
		Long roleId = 2l;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeRole(userId, roleId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeRoleWithNoRoleIdThrowsException() {

		LOGGER.info("running test removeRoleWithNoRoleIdThrowsException");

		Long userId = 2l;
		Long roleId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeRole(userId, roleId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeRole() {

		LOGGER.info("running test removeRole");

		userDao.removeRole(1l, 2l);

		verify(deleteUserRole, times(1)).execute(1l, 2l);
	}

	@Test
	void removeAllRolesWithNoUserIdThrowsException() {

		LOGGER.info("running test removeAllRolesWithNoUserIdThrowsException");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.removeAllRoles(userId);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void removeAllRoles() {

		LOGGER.info("running test removeAllRoles");

		userDao.removeAllRoles(1l);

		verify(deleteUserRoles, times(1)).execute(1l);
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
				.withDepartment(Department.newInstance().withId(1l).withName(ACCOUNTING.getName()));

		Throwable throwable = assertThrows(Throwable.class, () -> {
			userDao.update(user);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		LOGGER.info("running test update");

		User carl = User.newInstance().withId(2l).withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(Department.newInstance().withId(1l))
				.withVersion(3);

		// run the test
		userDao.update(carl);

		verify(updateUser, times(1)).execute(carl);
	}
}
