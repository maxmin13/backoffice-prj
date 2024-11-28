package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESSES_BY_USER_ID;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
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
import it.maxmin.dao.jdbc.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.JdbcDaoTestException;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
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
	void selectAddressesByUserIdAddressNotFound() {

		LOGGER.info("running test selectAddressesByUserIdNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		Optional<PojoUser> pojoUser = jdbcQueryTestUtil.selectUserByAccountName("maxmin13");
		PojoUser maxmin = pojoUser.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// run the test
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", maxmin.getId(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);

		assertEquals(0, addresses.size());
	}

	@Test
	void selectAddressesByUserId() {

		LOGGER.info("running test selectAddressesByUserId");

		Optional<PojoUser> pojoUser = jdbcQueryTestUtil.selectUserByAccountName("maxmin13");
		PojoUser maxmin = pojoUser.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", maxmin.getId(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);

		assertEquals(2, addresses.size());

		// first address
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), addresses.get(0).getState());

		Set<User> users1 = addresses.get(0).getUsers();

		assertEquals(1, users1.size());

		// user
		Optional<User> user1 = users1.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst();
		User us1 = user1.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// roles
		assertEquals(3, us1.getRoles().size());

		Optional<Role> role1 = us1.getRole(administrator.getName());
		Role r1 = role1.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administrator.getName(), r1);

		Optional<Role> role2 = us1.getRole(user.getName());
		Role r2 = role2.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(user.getName(), r2);

		Optional<Role> role3 = us1.getRole(worker.getName());
		Role r3 = role3.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(worker.getName(), r3);

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), us1.getDepartment());

		// addresses
		assertEquals(0, us1.getAddresses().size());

		// second address
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), addresses.get(1).getState());

		Set<User> users2 = addresses.get(1).getUsers();

		assertEquals(1, users2.size());

		// user
		Optional<User> user2 = users2.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst();
		User u2 = user2.orElseThrow(() -> new JdbcDaoTestException("Error uer not found"));

		// roles
		assertEquals(3, u2.getRoles().size());

		role1 = u2.getRole(administrator.getName());
		r1 = role1.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administrator.getName(), r1);

		role2 = u2.getRole(user.getName());
		r2 = role2.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(user.getName(), r2);

		role3 = u2.getRole(worker.getName());
		r3 = role3.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(worker.getName(), r3);

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), u2.getDepartment());

		// addresses
		assertEquals(0, u2.getAddresses().size());
	}

	@Test
	void selectAddressesByPostalCodeAddressNotFound() {

		LOGGER.info("running test selectAddressesByPostalCodeAddressNotFound");

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
	void selectAddressesByPostalCodeWithNoUser() {

		LOGGER.info("running test selectAddressesByPostalCode");

		// delete all the users
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		Optional<PojoAddress> address = jdbcQueryTestUtil.selectAddressByPostalCode("30010");
		PojoAddress add = address.orElseThrow(() -> new JdbcDaoTestException("Error address not found"));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("postalCode", add.getPostalCode(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(0, users.size());
	}

	@Test
	void selectAddressesByPostalCode() {

		LOGGER.info("running test selectAddressesByPostalCode");

		Optional<PojoAddress> address = jdbcQueryTestUtil.selectAddressByPostalCode("30010");
		PojoAddress ad = address.orElseThrow(() -> new JdbcDaoTestException("Error address not found"));

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("postalCode", ad.getPostalCode(), Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(1, users.size());

		// user
		Optional<User> maxmin = users.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst();
		User max = maxmin.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));

		// roles
		assertEquals(3, max.getRoles().size());

		Optional<Role> role1 = max.getRole(administrator.getName());
		Role r1 = role1.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(administrator.getName(), r1);

		Optional<Role> role2 = max.getRole(user.getName());
		Role r2 = role2.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(user.getName(), r2);

		Optional<Role> role3 = max.getRole(worker.getName());
		Role r3 = role3.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(worker.getName(), r3);

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), max.getDepartment());

		// addresses
		assertEquals(0, max.getAddresses().size());
	}
}
