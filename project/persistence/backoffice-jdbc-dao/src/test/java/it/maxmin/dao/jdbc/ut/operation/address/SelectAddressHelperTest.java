package it.maxmin.dao.jdbc.ut.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESSES_BY_USER_ID;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.config.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressHelper;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.dao.jdbc.util.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.util.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class SelectAddressHelperTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAddressHelperTest.class);
	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<Address>> resultSetExtractor;

	@Autowired
	SelectAddressHelperTest(NamedParameterJdbcTemplate jdbcTemplate, JdbcQueryTestUtil jdbcQueryTestUtil,
			JdbcUserTestUtil jdbcUserTestUtil) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.jdbcTemplate = jdbcTemplate;
	}

	@BeforeEach
	void initTests() {
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectAddressHelper selectAddressHelper = new SelectAddressHelper(resultSetUserBuilder,
				resultSetAddressBuilder);
		resultSetExtractor = selectAddressHelper.getResultSetExtractor();
	}

	@Test
	void selectAddressByUserIdNotFound() {

		LOGGER.info("running test selectAddressByUserIdNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// run the test
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", maxmin.getId(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);

		assertEquals(0, addresses.size());
	}

	@Test
	void selectAddressByUserId() {

		LOGGER.info("running test selectAddressByUserId");

		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", maxmin.getId(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);

		assertEquals(2, addresses.size());

		// first address
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italyState.getName(), italyState.getCode(), addresses.get(0).getState());

		Set<User> users1 = addresses.get(0).getUsers();

		assertEquals(1, users1.size());

		// user
		User user1 = users1.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// roles
		assertEquals(3, user1.getRoles().size());

		Role role1 = user1.getRole(administratorRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administratorRole.getName(), role1);

		Role role2 = user1.getRole(userRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(userRole.getName(), role2);

		Role role3 = user1.getRole(workerRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(workerRole.getName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(productionDepartment.getName(), user1.getDepartment());

		// addresses
		assertEquals(0, user1.getAddresses().size());

		// second address
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		jdbcUserTestUtil.verifyState(irelandState.getName(), irelandState.getCode(), addresses.get(1).getState());

		Set<User> users2 = addresses.get(1).getUsers();

		assertEquals(1, users2.size());

		// user
		User user2 = users2.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException("Error uer not found"));

		// roles
		assertEquals(3, user2.getRoles().size());

		Role role4 = user2.getRole(administratorRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administratorRole.getName(), role4);

		Role role5 = user2.getRole(userRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(userRole.getName(), role5);

		Role role6 = user2.getRole(workerRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(workerRole.getName(), role6);

		// department
		jdbcUserTestUtil.verifyDepartment(productionDepartment.getName(), user2.getDepartment());

		// addresses
		assertEquals(0, user2.getAddresses().size());
	}

	@Test
	void selectAddressByPostalCodeNotFound() {

		LOGGER.info("running test selectAddressByPostalCodeNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("postalCode", "30010", Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);

		assertEquals(0, addresses.size());
	}

	@Test
	void selectAddressByPostalCodeWithNoUser() {

		LOGGER.info("running test selectAddressByPostalCodeWithNoUser");

		// delete all the users
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_useraddress.down.sql",
				"2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoAddress address = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("postalCode", address.getPostalCode(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italyState.getName(), italyState.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(0, users.size());
	}

	@Test
	void selectAddressByPostalCode() {

		LOGGER.info("running test selectAddressByPostalCode");

		PojoAddress address = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("postalCode", address.getPostalCode(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italyState.getName(), italyState.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(1, users.size());

		// user
		User maxmin = users.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// roles
		assertEquals(3, maxmin.getRoles().size());

		Role role1 = maxmin.getRole(administratorRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administratorRole.getName(), role1);

		Role role2 = maxmin.getRole(userRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(userRole.getName(), role2);

		Role role3 = maxmin.getRole(workerRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(workerRole.getName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(productionDepartment.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());
	}

	@Test
	void selectAddressByAccountNameAndPostalCodeNotFound() {

		LOGGER.info("running test selectAddressByAccountNameAndPostalCodeNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser user = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", user.getId());
		params.addValue("postalCode", "30010");

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE, params,
				resultSetExtractor);

		assertEquals(0, addresses.size());
	}

	@Test
	void selectAddressByUserIdAndPostalCodeWithNoUser() {

		LOGGER.info("running test selectAddressByUserIdAndPostalCodeWithNoUser");

		// delete all the users
		String[] scripts = { "2_transaction.down.sql", "2_account.down.sql", "2_useraddress.down.sql",
				"2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoAddress address = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", address.getPostalCode());
		params.addValue("postalCode", address.getPostalCode());

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE, params,
				resultSetExtractor);

		assertEquals(0, addresses.size());
	}

	@Test
	void selectAddressByUserIdAndPostalCode() {

		LOGGER.info("running test selectAddressByUserIdAndPostalCode");
		
		PojoUser user = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		PojoAddress address = jdbcQueryTestUtil.selectAddressByUserIdPostalCode(user.getId(), "30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", user.getId());
		params.addValue("postalCode", address.getPostalCode());

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE, params,
				resultSetExtractor);

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italyState.getName(), italyState.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(1, users.size());

		// user
		User maxmin = users.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// roles
		assertEquals(3, maxmin.getRoles().size());

		Role role1 = maxmin.getRole(administratorRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administratorRole.getName(), role1);

		Role role2 = maxmin.getRole(userRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(userRole.getName(), role2);

		Role role3 = maxmin.getRole(workerRole.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(workerRole.getName(), role3);

		// department
		jdbcUserTestUtil.verifyDepartment(productionDepartment.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());
	}
}
