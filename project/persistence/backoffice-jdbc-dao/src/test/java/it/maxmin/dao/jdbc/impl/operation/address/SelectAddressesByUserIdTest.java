package it.maxmin.dao.jdbc.impl.operation.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

class SelectAddressesByUserIdTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAddressesByUserIdTest.class);
	private SelectAddressesByUserId selectAddressesByUserId;

	@Autowired
	SelectAddressesByUserIdTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			NamedParameterJdbcTemplate jdbcTemplate) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.selectAddressesByUserId = new SelectAddressesByUserId(jdbcTemplate);
	}

	@Test
	void executeWithNoAddressId() {

		LOGGER.info("running test executeWithNoAddresses");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> selectAddressesByUserId.execute(userId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeAddressNotFound() {

		LOGGER.info("running test executeAddressNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = selectAddressesByUserId.execute(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = selectAddressesByUserId.execute(maxmin.getId());

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), addresses.get(0).getState());

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		jdbcUserTestUtil.verifyState(ireland.getName(), ireland.getCode(), addresses.get(1).getState());
	}

}
