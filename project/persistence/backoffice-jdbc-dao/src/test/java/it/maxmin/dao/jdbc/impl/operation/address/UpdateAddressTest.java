package it.maxmin.dao.jdbc.impl.operation.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoState;

class UpdateAddressTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAddressTest.class);
	private UpdateAddress updateAddress;

	@Autowired
	UpdateAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.updateAddress = new UpdateAddress(dataSource);
	}

	@Test
	void executeWithNoAddressThrowsException() {

		LOGGER.info("running test executeWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> updateAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoAddressIdThrowsException() {

		LOGGER.info("running test executeWithoutAddressIdThrowsException");

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> updateAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoStateThrowsException() {

		LOGGER.info("running test executeWithNoStateThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> updateAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void executeWithNoStateIdThrowsException() {

		LOGGER.info("running test executeWithNoStateThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033").withState(State.newInstance());

		Throwable throwable = assertThrows(Throwable.class, () -> updateAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test update");

		// Find an existing address
		PojoAddress address = jdbcQueryTestUtil.findAddressByPostalCode("30010");

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address);
		assertEquals(italy.getId(), address.getStateId());

		// Change the address
		Address addressUpdated = Address.newInstance().withId(address.getId()).withDescription("Via Nuova")
				.withCity("Venice").withState(State.newInstance().withId(italy.getId())).withRegion("Veneto")
				.withPostalCode("33311");

		// run the test
		updateAddress.execute(addressUpdated);

		PojoAddress updated = jdbcQueryTestUtil.findAddressByAddressId(address.getId());

		jdbcUserTestUtil.verifyAddress("33311", "Via Nuova", "Venice", "Veneto", updated);

		PojoState state = jdbcQueryTestUtil.findStateByAddressPostalCode("33311");

		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), state);
	}

}