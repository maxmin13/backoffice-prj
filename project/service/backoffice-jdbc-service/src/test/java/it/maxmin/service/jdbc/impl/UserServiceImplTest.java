package it.maxmin.service.jdbc.impl;

import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static it.maxmin.dao.jdbc.impl.constant.Role.WORKER;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ADDRESS_ALREADY_CREATED;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ADDRESS_NOT_FOUND;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_ALREADY_CREATED;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_NOT_ROLLED_BACK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoDepartment;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoState;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;
import it.maxmin.model.jdbc.service.dto.AddressDto;
import it.maxmin.model.jdbc.service.dto.CredentialsDto;
import it.maxmin.model.jdbc.service.dto.DepartmentDto;
import it.maxmin.model.jdbc.service.dto.RoleDto;
import it.maxmin.model.jdbc.service.dto.StateDto;
import it.maxmin.model.jdbc.service.dto.UserDto;
import it.maxmin.service.jdbc.JdbcServiceSpringContextTestCfg;
import it.maxmin.service.jdbc.api.UserService;
import it.maxmin.service.jdbc.exception.JdbcServiceException;
import it.maxmin.service.jdbc.exception.JdbcServiceTestException;

@SpringJUnitConfig(classes = { JdbcServiceSpringContextTestCfg.class })
class UserServiceImplTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

	private UserService userService;

	@Autowired
	protected UserServiceImplTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			UserService userService) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.userService = userService;
	}
	

	@Test
	@DisplayName("01. should throw an exception")
	@Order(1)
	void updateUserWithNoCredentialsThrowsError() {

		LOGGER.info("running test updateUserWithNoCredentialsThrowsError");

		UserDto maxmin = UserDto.newInstance(null, LocalDate.of(1940, 9, 11),
				DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("02. should throw an exception")
	@Order(2)
	void updateUserWithNoAccountNameThrowsError() {

		LOGGER.info("running test updateUserWithNoAccountNameThrowsError");

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance(null, "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("03. should throw an exception")
	@Order(3)
	void updateUserWithNoFirstNameThrowsError() {

		LOGGER.info("running test updateUserWithNoFirstNameThrowsError");

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("john123", null, "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("04. should throw an exception")
	@Order(4)
	void updateUserWithNoLastNameThrowsError() {

		LOGGER.info("running test updateUserWithNoLastNameThrowsError");

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("john123", "Franco", null),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("05. should throw an exception")
	@Order(5)
	void updateUserWithNoBirthDateThrowsError() {

		LOGGER.info("running test updateUserWithNoBirthDateThrowsError");

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("john123", "Franco", "Franchi"), null,
				DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("06. should throw an exception")
	@Order(6)
	void updateUserWithNoDepartmentThrowsError() {

		LOGGER.info("running test updateUserWithNoDepartmentThrowsError");

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("john123", "Franco", "Franchi"), null, null,
				null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@DisplayName("07. should throw an exception")
	@Order(7)
	void updateUserWithNoDepartmentNameThrowsError() {

		LOGGER.info("running test updateUserWithNoDepartmentNameThrowsError");

		String departmentName = null;

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("john123", "Franco", "Franchi"), null,
				DepartmentDto.newInstance(departmentName), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	@Order(8)
	@DisplayName("08. should throw an exception")
	void updateUserNotFoundThrowsError() {

		LOGGER.info("running test updateUserNotFoundThrowsError");

		jdbcQueryTestUtil.selectUserByAccountName("maxmin99").ifPresent(u -> {
			throw new JdbcServiceTestException(ERROR_USER_ALREADY_CREATED);
		});

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("maxmin99", "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		Throwable throwable = assertThrows(Throwable.class, () -> userService.updateUser(maxmin));

		assertEquals(JdbcServiceException.class, throwable.getClass());
	}

	@Test
	@Order(9)
	@DisplayName("09. should update the user")
	void updateUser1() {

		LOGGER.info("running test updateUserTest1");

		// find an existing user
		PojoUser existingUser = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_USER_NOT_FOUND_MSG));

		// assess the state of the user
		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), existingUser);

		Long userId = existingUser.getId();
		Long initialVersion = existingUser.getVersion();

		assertEquals(0, initialVersion);
		assertEquals(productionDepartment.getId(), existingUser.getDepartmentId());

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(2, addresses.size());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(3, roles.size());

		// prepare the user for the update
		UserDto franco = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null, null);

		// run the test
		userService.updateUser(franco);

		// check the user
		PojoUser user = jdbcQueryTestUtil.selectUserByUserId(userId)
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		assertEquals(initialVersion + 1, user.getVersion());

		jdbcUserTestUtil.verifyUser("maxmin13", "Franco", "Franchi", LocalDate.of(1940, 9, 11), user);

		assertEquals(administratorRole.getId(), user.getDepartmentId());

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(0, addresses.size());

		roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(0, roles.size());
	}

	@Test
	@Order(10)
	@DisplayName("10. should update an address and replace the other")
	void updateUser2() {

		LOGGER.info("running test updateUser2");

		// find an existing user
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_USER_NOT_FOUND_MSG));

		// assess the state of the user
		Long userId = maxmin.getId();

		assertEquals(0, maxmin.getVersion());

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(2, addresses.size());

		PojoAddress address1 = addresses.stream().filter(each -> each.getPostalCode().equals("30010")).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ADDRESS_NOT_FOUND));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		assertEquals(0, address1.getVersion());
		assertEquals(address1.getStateId(), italyState.getId());

		PojoAddress address2 = addresses.stream().filter(each -> each.getPostalCode().equals("A65TF12"))
				.findFirst().orElseThrow(() -> new JdbcServiceTestException(ERROR_ADDRESS_NOT_FOUND));
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		assertEquals(0, address2.getVersion());
		assertEquals(address2.getStateId(), irelandState.getId());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(3, roles.size());

		// prepare the user for the update
		// update an existing address
		AddressDto addressDto1 = AddressDto.newInstance("Via borgo di sopra", "Torino", "County Piemonte",
				address1.getPostalCode(), StateDto.newInstance(italyState.getName(), italyState.getCode()));

		// create a new address
		jdbcQueryTestUtil.selectAddressByPostalCode("50033").ifPresent(address -> {
			throw new JdbcServiceTestException(ERROR_ADDRESS_ALREADY_CREATED);
		});
		AddressDto addressDto2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italyState.getName(), italyState.getCode()));

		UserDto franco = UserDto.newInstance(CredentialsDto.newInstance(maxmin.getAccountName(), "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()),
				Set.of(addressDto1, addressDto2), null);

		// run the test
		userService.updateUser(franco);

		// check the user
		PojoUser updatedUser = jdbcQueryTestUtil.selectUserByUserId(userId)
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		assertEquals(maxmin.getVersion() + 1, updatedUser.getVersion());

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(2, addresses.size());

		assertEquals(0, addresses.stream().filter(each -> each.getPostalCode().equals("A65TF12")).count());

		PojoAddress address3 = addresses.stream().filter(each -> each.getPostalCode().equals("30010")).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ADDRESS_NOT_FOUND));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sopra", "Torino", "County Piemonte", address3);
		assertEquals(1, address3.getVersion());
		assertEquals(address3.getStateId(), italyState.getId());

		PojoAddress address4 = addresses.stream().filter(each -> each.getPostalCode().equals("50033")).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ADDRESS_NOT_FOUND));

		jdbcUserTestUtil.verifyAddress("50033", "Via della resistenza", "Florence", "County Liguria", address4);
		assertEquals(0, address4.getVersion());
		assertEquals(address4.getStateId(), irelandState.getId());

		roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(0, roles.size());
	}

	@Test
	@Order(10)
	@DisplayName("10. should delete two roles and leave the user with the role of administrator")
	void updateUser3() {

		LOGGER.info("running test updateUser3");

		// find an existing user
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_USER_NOT_FOUND_MSG));

		// assess the state of the user
		Long userId = maxmin.getId();

		assertEquals(0, maxmin.getVersion());

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(2, addresses.size());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(3, roles.size());

		roles.stream().filter(each -> each.getName().equals(ADMINISTRATOR.getName())).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ROLE_NOT_FOUND_MSG));
		roles.stream().filter(each -> each.getName().equals(WORKER.getName())).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ROLE_NOT_FOUND_MSG));
		roles.stream().filter(each -> each.getName().equals(USER.getName())).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ROLE_NOT_FOUND_MSG));

		// prepare the user for the update
		UserDto franco = UserDto.newInstance(CredentialsDto.newInstance(maxmin.getAccountName(), "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()), null,
				Set.of(RoleDto.newInstance(ADMINISTRATOR.getName())));

		// run the test
		userService.updateUser(franco);

		// check the user
		PojoUser updatedUser = jdbcQueryTestUtil.selectUserByUserId(userId)
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		assertEquals(maxmin.getVersion() + 1, updatedUser.getVersion());

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(0, addresses.size());

		roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(1, roles.size());

		roles.stream().filter(each -> each.getName().equals(ADMINISTRATOR.getName())).findFirst()
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_ROLE_NOT_FOUND_MSG));
	}	
	
	
	
	
	

//	@Test TODO RESTORE
	@Order(1)
	void createUser() {

		LOGGER.info("running test createUserTest");

		// delete all the users
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_useraddress.down.sql",
				"2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// find an existing address
		PojoAddress address1 = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		PojoState state1 = jdbcQueryTestUtil.selectStateByAddressId(address1.getId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		jdbcUserTestUtil.verifyState("Italy", "IT", state1);

		AddressDto addressDto1 = AddressDto.newInstance(address1.getDescription(), address1.getCity(),
				address1.getRegion(), address1.getPostalCode(),
				StateDto.newInstance(state1.getName(), state1.getCode()));

		// prepare a new address
		jdbcQueryTestUtil.selectAddressByPostalCode("50033").ifPresent(user -> {
			throw new JdbcServiceTestException(ERROR_ADDRESS_ALREADY_CREATED);
		});

		AddressDto addressDto2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italyState.getName(), italyState.getCode()));

		Set<AddressDto> addresses = Set.of(addressDto1, addressDto2);
		Set<RoleDto> roles = Set.of(RoleDto.newInstance(administratorRole.getName()),
				RoleDto.newInstance(userRole.getName()));

		// check the user doesn't exist
		jdbcQueryTestUtil.selectUserByAccountName("maxmin13").ifPresent(user -> {
			throw new JdbcServiceTestException(ERROR_ADDRESS_ALREADY_CREATED);
		});

		// prepare the new user
		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Max", "Minardi"),
				LocalDate.of(1977, 10, 16), DepartmentDto.newInstance(productionDepartment.getName()), addresses,
				roles);

		// running the test
		userService.createUser(maxmin);

		// check the user
		PojoUser user = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user);
		PojoDepartment department = jdbcQueryTestUtil.selectDepartmentById(user.getDepartmentId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_DEPARTMENT_NOT_FOUND_MSG));
		jdbcUserTestUtil.verifyDepartment(productionDepartment.getName(), department);

		// check the addresses
		List<PojoAddress> userAddresses = jdbcQueryTestUtil.selectAddressesByUserId(user.getId());
		assertEquals(2, userAddresses.size());
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", userAddresses.get(0));
		jdbcUserTestUtil.verifyAddress("50033", "Via della resistenza", "Florence", "County Liguria",
				userAddresses.get(1));

		// check the roles
		List<PojoRole> userRoles = jdbcQueryTestUtil.selectRolesByUserId(user.getId());
		assertEquals(2, userRoles.size());
		jdbcUserTestUtil.verifyRole(administratorRole.getName(), userRoles.get(0));
		jdbcUserTestUtil.verifyRole(userRole.getName(), userRoles.get(1));
	}

	// TODO existing user error

//	@Test TODO RESTORE
	@Order(2)
	@DisplayName("An exception should roll back all the changes in transaction.")
	void createUserWithError() {

		LOGGER.info("running test createUserWithError");

		// delete all the users
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_useraddress.down.sql",
				"2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// find an existing address
		PojoAddress address1 = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);

		PojoState state1 = jdbcQueryTestUtil.selectStateByAddressId(address1.getId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_STATE_NOT_FOUND_MSG));
		jdbcUserTestUtil.verifyState("Italy", "IT", state1);

		AddressDto addressDto1 = AddressDto.newInstance(address1.getDescription(), address1.getCity(),
				address1.getRegion(), address1.getPostalCode(),
				StateDto.newInstance(state1.getName(), state1.getCode()));

		// prepare a new address
		jdbcQueryTestUtil.selectAddressByPostalCode("50033").ifPresent(user -> {
			throw new JdbcServiceTestException(ERROR_ADDRESS_ALREADY_CREATED);
		});

		AddressDto addressDto2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italyState.getName(), italyState.getCode()));

		Set<AddressDto> addresses = Set.of(addressDto1, addressDto2);

		// a non existing role should cause an error
		Set<RoleDto> roles = Set.of(RoleDto.newInstance("Supervisor"), RoleDto.newInstance(userRole.getName()));

		// check the user doesn't exist
		jdbcQueryTestUtil.selectUserByAccountName("maxmin13").ifPresent(user -> {
			throw new JdbcServiceTestException(ERROR_ADDRESS_ALREADY_CREATED);
		});

		// prepare the new user
		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Max", "Minardi"),
				LocalDate.of(1977, 10, 16), DepartmentDto.newInstance(productionDepartment.getName()), addresses,
				roles);

		// running the test
		Throwable throwable = assertThrows(Throwable.class, () -> userService.createUser(maxmin));

		assertEquals(JdbcServiceException.class, throwable.getClass());

		// check everything has been rolled back
		jdbcQueryTestUtil.selectUserByAccountName("maxmin13").ifPresent(user -> {
			throw new JdbcServiceTestException(ERROR_USER_NOT_ROLLED_BACK);
		});

		// check the addresses
		List<PojoAddress> userAddresses = jdbcQueryTestUtil.selectAddressesByUserAccountName("maxmin13");
		assertEquals(0, userAddresses.size());

		// check the role
		List<PojoRole> userRoles = jdbcQueryTestUtil.selectRolesByUserAccountName("maxmin13");
		assertEquals(0, userRoles.size());
	}


}
