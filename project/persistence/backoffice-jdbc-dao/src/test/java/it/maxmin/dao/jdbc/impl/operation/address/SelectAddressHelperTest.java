package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESSES_BY_USER_ID;
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

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Role;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

class SelectAddressHelperTest extends BaseTestUser {

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
	void selectAddressesByUserIdNotFound() {

		LOGGER.info("running test selectAddressesByUserIdNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

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

		Long userId = jdbcQueryTestUtil.findUserByAccountName("maxmin13").getId();

		// run the test
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId, Types.VARCHAR);

		// run the test
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);

		assertEquals(2, addresses.size());

		// first address
		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), addresses.get(0).getState());

		Set<User> users = addresses.get(0).getUsers();

		assertEquals(1, users.size());

		// user
		User maxmin = users.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst().orElse(null);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		Optional<Role> role1 = maxmin.getRole(administrator.getRoleName());

		assertEquals(true, role1.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role1.get());

		Optional<Role> role2 = maxmin.getRole(user.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role2.get());

		Optional<Role> role3 = maxmin.getRole(worker.getRoleName());

		assertEquals(true, role3.isPresent());

		jdbcUserTestUtil.verifyRole(worker.getRoleName(), role3.get());

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());

		// second address
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), addresses.get(1).getState());

		users = addresses.get(0).getUsers();

		assertEquals(1, users.size());

		// user
		maxmin = users.stream().filter(each -> each.getAccountName().equals("maxmin13")).findFirst().orElse(null);

		// roles
		assertEquals(3, maxmin.getRoles().size());

		role1 = maxmin.getRole(administrator.getRoleName());

		assertEquals(true, role1.isPresent());

		jdbcUserTestUtil.verifyRole(administrator.getRoleName(), role1.get());

		role2 = maxmin.getRole(user.getRoleName());

		assertEquals(true, role2.isPresent());

		jdbcUserTestUtil.verifyRole(user.getRoleName(), role2.get());

		role3 = maxmin.getRole(worker.getRoleName());

		assertEquals(true, role3.isPresent());

		jdbcUserTestUtil.verifyRole(worker.getRoleName(), role3.get());

		// department
		jdbcUserTestUtil.verifyDepartment(production.getName(), maxmin.getDepartment());

		// addresses
		assertEquals(0, maxmin.getAddresses().size());
	}
}
