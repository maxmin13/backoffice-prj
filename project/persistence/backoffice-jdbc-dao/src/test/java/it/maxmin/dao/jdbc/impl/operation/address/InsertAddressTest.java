package it.maxmin.dao.jdbc.impl.operation.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.BaseDaoTest;
import it.maxmin.dao.jdbc.DaoTestException;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.UnitTestContextCfg;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoState;

@SpringJUnitConfig(classes = { UnitTestContextCfg.class })
class InsertAddressTest extends BaseDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertAddressTest.class);
	private InsertAddress insertAddress;

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
	void executeWithNoStateThrowsException() {

		LOGGER.info("running test executeWithNoStateThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> insertAddress.execute(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void executeWithNoStateIdThrowsException() {

		LOGGER.info("running test executeWithNoStateThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withRegion("Veneto").withPostalCode("30033").withState(State.newInstance());

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

		Optional<PojoAddress> newAddress = jdbcQueryTestUtil.findAddressByAddressId(address.getId());
		PojoAddress ad = newAddress.orElseThrow(() -> new DaoTestException("Error address not found"));
		
		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "Veneto", ad);

		Optional<PojoState> state = jdbcQueryTestUtil.findStateByAddressPostalCode("30033");
		PojoState st = state.orElseThrow(() -> new DaoTestException("Error state not found"));

		jdbcUserTestUtil.verifyState(italy.getName(), italy.getCode(), st);
	}
}
