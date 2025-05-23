package it.maxmin.dao.jdbc.ut.operation.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcBaseTestDao;
import it.maxmin.dao.jdbc.config.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddresses;
import it.maxmin.dao.jdbc.util.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.util.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class InsertAddressesTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertAddressesTest.class);
	private InsertAddresses insertAddresses;

	@Autowired
	InsertAddressesTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil, DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertAddresses = new InsertAddresses(dataSource);
	}
	
	@Test
	void executeWithNoAddresses() {

		LOGGER.info("running test executeWithNoAddresses");

		List<Address> addresses = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertAddresses.execute(addresses));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void execute() {

		LOGGER.info("running test execute");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italyState.getId())).withRegion("Veneto").withPostalCode("30033");

		Address address2 = Address.newInstance().withDescription("Connolly street").withCity("Dublin")
				.withState(State.newInstance().withId(irelandState.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF12");

		List<Address> addresses = List.of(address1, address2);

		// run the test
		insertAddresses.execute(addresses);

		List<PojoAddress> newAddresses = jdbcQueryTestUtil.selectAllAddresses();

		assertEquals(2, newAddresses.size());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "Veneto", newAddresses.get(0));
		assertEquals(italyState.getId(), newAddresses.get(0).getStateId());
		
		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", newAddresses.get(1));
		assertEquals(irelandState.getId(), newAddresses.get(1).getStateId());
	}
}
