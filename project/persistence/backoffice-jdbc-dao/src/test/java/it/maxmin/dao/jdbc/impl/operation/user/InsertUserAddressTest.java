package it.maxmin.dao.jdbc.impl.operation.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

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
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class InsertUserAddressTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserAddressTest.class);
	private InsertUserAddress insertUserAddress;

	@Autowired
	InsertUserAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.insertUserAddress = new InsertUserAddress(dataSource);
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");
		
		Long userId = null;
		Long addressId = 1l;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserAddress.execute(userId, addressId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void executeWithNoAddressIdThrowsException() {

		LOGGER.info("running test executeWithNoAddressIdThrowsException");
		
		Long userId = 1l;
		Long addressId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> insertUserAddress.execute(userId, addressId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		PojoUser franco = PojoUser.newInstance().withAccountName("franc").withBirthDate(LocalDate.of(1981, 11, 12))
				.withFirstName("Franco").withLastName("Red").withDepartmentId(legalDepartment.getId());

		PojoUser newUser = jdbcQueryTestUtil.insertUser(franco);

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withStateId(italyState.getId()).withRegion("Veneto").withPostalCode("30033");

		PojoAddress newAddress = jdbcQueryTestUtil.insertAddress(address);

		// run the test
		insertUserAddress.execute(newUser.getId(), newAddress.getId());

		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(newUser.getId());

		assertEquals(1, addresses.size());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "Veneto", addresses.get(0));

		assertEquals(italyState.getId(), addresses.get(0).getStateId());
	}
}
