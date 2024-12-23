package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoState;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class UpdateAddressTest extends JdbcBaseTestDao {

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
				.withState(State.newInstance().withId(italyState.getId())).withRegion("Veneto").withPostalCode("30033");

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
	
	// TODO ?????? @Test
	void executeWithNoVersionThrowsException() {

		LOGGER.info("running test executeWithNoVersion");

		// Find an existing address
		PojoAddress address = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address);
		assertEquals(italyState.getId(), address.getStateId());

		// Change the address
		Address addressUpdated = Address.newInstance().withId(address.getId()).withDescription("Via Nuova")
				.withCity("Venice").withState(State.newInstance().withId(italyState.getId())).withRegion("Veneto")
				.withPostalCode("33311");

		// run the test
		Throwable throwable = assertThrows(Throwable.class, () -> updateAddress.execute(addressUpdated));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test update");

		// Find an existing address
		PojoAddress address = jdbcQueryTestUtil.selectAddressByPostalCode("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address);
		assertEquals(italyState.getId(), address.getStateId());

		// Change the address
		Address addressUpdated = Address.newInstance().withId(address.getId()).withDescription("Via Nuova")
				.withCity("Venice").withState(State.newInstance().withId(italyState.getId())).withRegion("Veneto")
				.withPostalCode("33311").withVersion(address.getVersion());

		// run the test
		updateAddress.execute(addressUpdated);

		PojoAddress updated = jdbcQueryTestUtil.selectAddressByAddressId(address.getId())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("33311", "Via Nuova", "Venice", "Veneto", updated);
		assertEquals(address.getVersion() + 1, updated.getVersion());

		PojoState state = jdbcQueryTestUtil.selectStateByAddressPostalCode("33311")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_STATE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyState(italyState.getName(), italyState.getCode(), state);
	}

}