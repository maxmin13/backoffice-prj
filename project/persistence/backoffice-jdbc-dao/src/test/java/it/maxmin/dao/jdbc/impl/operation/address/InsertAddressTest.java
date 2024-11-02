package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.maxmin.dao.jdbc.BaseTestUser;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoState;

public class InsertAddressTest extends BaseTestUser {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertAddressTest.class);
	InsertAddress insertAddress;

	@Autowired
	InsertAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertAddress = new InsertAddress(dataSource);
	}

	@Test
	void executeWithNoAddressThrowsException() {

		LOGGER.info("running test executeWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithAddressIdThrowsException() {

		LOGGER.info("running test executeWithAddressIdThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> insertAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		// run the test
		address = insertAddress.execute(address);

		assertNotNull(address);
		assertNotNull(address.getId());

		PojoAddress newAddress = jdbcQueryTestUtil.findAddressByAddressId(address.getId());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "Veneto", newAddress);

		PojoState state = jdbcQueryTestUtil.findStateByAddressPostalCode("30033");

		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), state);
	}
}
