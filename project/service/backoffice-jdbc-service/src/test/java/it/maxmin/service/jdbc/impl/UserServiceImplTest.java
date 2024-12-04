package it.maxmin.service.jdbc.impl;

import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ADDRESS_ALREADY_CREATED;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.*;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_ALREADY_CREATED;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.JdbcDaoTestException;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
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
import it.maxmin.service.jdbc.exception.ServiceException;

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

//	@Test TODO RESTORE
	void createUserTest() {

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

//	@Test TODO RESTORE
	@DisplayName("An exception should roll back all the changes in transaction.")
	void createUserTestError() {

		LOGGER.info("running test createUserTestError");

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

		assertEquals(ServiceException.class, throwable.getClass());

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

	@Test
	void updateUserTest() {

		LOGGER.info("running test updateUserTest");

		// find an existing user
		PojoUser existingUser = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcServiceTestException(ERROR_USER_NOT_FOUND_MSG));

		// assess the state of the user
		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), existingUser);

		Long userId = existingUser.getId();
		Long initialVersion = existingUser.getVersion();
		assertEquals(0, initialVersion);

		assertEquals(productionDepartment.getId(), existingUser.getDepartmentId());

		List<PojoRole> roles = jdbcQueryTestUtil.selectRolesByUserId(userId);

		assertEquals(3, roles.size());
		jdbcUserTestUtil.verifyRole(administratorRole.getName(), roles.get(0));
		jdbcUserTestUtil.verifyRole(userRole.getName(), roles.get(1));
		jdbcUserTestUtil.verifyRole(workerRole.getName(), roles.get(2));

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(userId);

		assertEquals(2, addresses.size());
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		assertEquals(addresses.get(0).getStateId(), italyState.getId());
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		assertEquals(addresses.get(1).getStateId(), irelandState.getId());

		// prepare the user for the update

		// existing address
		PojoAddress address1 = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException("Error address not found"));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		PojoState state1 = jdbcQueryTestUtil.selectStateByAddressPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException("Error state not found"));
		jdbcUserTestUtil.verifyState("Italy", "IT", state1);
		AddressDto addressDto1 = AddressDto.newInstance(address1.getDescription(), address1.getCity(),
				address1.getRegion(), address1.getPostalCode(),
				StateDto.newInstance(state1.getName(), state1.getCode()));

		// new address
		jdbcQueryTestUtil.selectAddressByPostalCode("50033").ifPresent(address -> {
			throw new JdbcServiceTestException(ERROR_USER_ALREADY_CREATED);
		});
		AddressDto addressDto2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italyState.getName(), italyState.getCode()));

		// prepare the user for update
		UserDto franco = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Franco", "Franchi"),
				LocalDate.of(1940, 9, 11), DepartmentDto.newInstance(legalDepartment.getName()),
				Set.of(addressDto1, addressDto2),
				Set.of(RoleDto.newInstance(administratorRole.getName()), RoleDto.newInstance(userRole.getName())));

		// run the test
		userService.updateUser(franco);

		// check the user
		PojoUser user = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		
		assertEquals(userId, user.getId());
		jdbcUserTestUtil.verifyUser("maxmin13", "Franco", "Franchi", LocalDate.of(1940, 9, 11), user);
		
		assertEquals(initialVersion + 1, user.getVersion());
		
		//TODO CONTINUE WITH DEPARTMENT, ADDRESSES, ROLES ......
	}

}
