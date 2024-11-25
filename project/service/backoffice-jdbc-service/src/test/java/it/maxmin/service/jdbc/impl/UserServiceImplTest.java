package it.maxmin.service.jdbc.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.BaseDaoTest;
import it.maxmin.dao.jdbc.DaoTestException;
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
import it.maxmin.service.jdbc.DatabaseUnitTestContextCfg;
import it.maxmin.service.jdbc.api.UserService;
import it.maxmin.service.jdbc.exception.ServiceException;

@SpringJUnitConfig(classes = { DatabaseUnitTestContextCfg.class })
class UserServiceImplTest extends BaseDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

	private UserService userService;

	@Autowired
	protected UserServiceImplTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			UserService userService) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.userService = userService;
	}

	@Test
	void createUserTest() {

		LOGGER.info("running test createUserTest");

		// delete all the users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// find an existing address
		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", ad);
		Optional<PojoState> state = jdbcQueryTestUtil.findStateByAddressPostalCode("30010");
		PojoState st = state.orElseThrow(() -> new DaoTestException("Error state not found"));
		jdbcUserTestUtil.verifyState("Italy", "IT", st);
		AddressDto address1 = AddressDto.newInstance(ad.getDescription(), ad.getCity(), ad.getRegion(),
				ad.getPostalCode(), StateDto.newInstance(st.getName(), st.getCode()));

		// create a new address
		address = jdbcQueryTestUtil.findAddressByPostalCode("50033");
		assertEquals(true, address.isEmpty()); 

		AddressDto address2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italy.getName(), italy.getCode()));

		Set<AddressDto> addresses = Set.of(address1, address2);
		Set<RoleDto> roles = Set.of(RoleDto.newInstance(administrator.getRoleName()), RoleDto.newInstance(user.getRoleName()));

		// check the user doesn't exist
		Optional<PojoUser> pojoUser = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		assertEquals(true, pojoUser.isEmpty());

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Max", "Minardi"),
				LocalDate.of(1977, 10, 16), null, DepartmentDto.newInstance(production.getName()), addresses, roles);

		// running the test
		userService.createUser(maxmin);

		// check the user
		pojoUser = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		PojoUser us = pojoUser.orElseThrow(() -> new DaoTestException("Error user not found"));
		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), us);
		Optional<PojoDepartment> department = jdbcQueryTestUtil.findDepartmentById(us.getDepartmentId());
		PojoDepartment dep = department.orElseThrow(() -> new DaoTestException("Error department not found"));
		jdbcUserTestUtil.verifyDepartment(production.getName(), dep);

		// check the addresses
		List<PojoAddress> userAddresses = jdbcQueryTestUtil.findAddressesByUserAccountName(us.getAccountName());
		assertEquals(2, userAddresses.size());
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", userAddresses.get(0));
		jdbcUserTestUtil.verifyAddress("50033", "Via della resistenza", "Florence", "County Liguria",
				userAddresses.get(1));

		// check the roles
		List<PojoRole> userRoles = jdbcQueryTestUtil.findRolesByUserAccountName("maxmin13");
		assertEquals(2, userRoles.size());
		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), userRoles.get(0));
		jdbcUserTestUtil.verifyRole(user.getRoleName(), userRoles.get(1));
	}

	@Test
	@DisplayName("An exception should roll back all the changes in transaction.")
	void createUserTestError() {

		LOGGER.info("running test createUserTestError");

		// Delete all the users. 
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		// find an existing address
		Optional<PojoAddress> address = jdbcQueryTestUtil.findAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new DaoTestException("Error address not found"));
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", ad);
		Optional<PojoState> state = jdbcQueryTestUtil.findStateByAddressPostalCode("30010");
		PojoState st = state.orElseThrow(() -> new DaoTestException("Error state not found"));
		jdbcUserTestUtil.verifyState("Italy", "IT", st);
		AddressDto address1 = AddressDto.newInstance(ad.getDescription(), ad.getCity(), ad.getRegion(),
				ad.getPostalCode(), StateDto.newInstance(st.getName(), st.getCode()));

		// create a new address
		address = jdbcQueryTestUtil.findAddressByPostalCode("50033");
		assertEquals(true, address.isEmpty()); // String description, String city, String region, String postalCode,

		AddressDto address2 = AddressDto.newInstance("Via della resistenza", "Florence", "County Liguria", "50033",
				StateDto.newInstance(italy.getName(), italy.getCode()));

		Set<AddressDto> addresses = Set.of(address1, address2);
		
		// non existent role to cause an exception.
		Set<RoleDto> roles = Set.of(RoleDto.newInstance(administrator.getRoleName()), RoleDto.newInstance("supervisor")); 

		// check the user doesn't exist
		Optional<PojoUser> user = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		assertEquals(true, user.isEmpty());

		UserDto maxmin = UserDto.newInstance(CredentialsDto.newInstance("maxmin13", "Max", "Minardi"),
				LocalDate.of(1977, 10, 16), null, DepartmentDto.newInstance(production.getName()), addresses, roles);

		// running the test
		Throwable throwable = assertThrows(Throwable.class, () -> userService.createUser(maxmin));
		
		assertEquals(ServiceException.class, throwable.getClass());
	
		// check the user
		user = jdbcQueryTestUtil.findUserByAccountName("maxmin13");
		assertEquals(true, user.isEmpty());

		// check the addresses
		List<PojoAddress> userAddresses = jdbcQueryTestUtil.findAddressesByUserAccountName("maxmin13");
		assertEquals(0, userAddresses.size());
		
		// check the role
		List<PojoRole> userRoles = jdbcQueryTestUtil.findRolesByUserAccountName("maxmin13");
		assertEquals(0, userRoles.size());
	}
}
