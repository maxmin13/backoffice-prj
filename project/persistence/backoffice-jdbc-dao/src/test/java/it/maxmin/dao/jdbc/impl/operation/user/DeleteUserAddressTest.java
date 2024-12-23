package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
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
import it.maxmin.dao.jdbc.JdbcDaoSpringContextTestCfg;
import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcDaoSpringContextTestCfg.class })
class DeleteUserAddressTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserAddressTest.class);
	private DeleteUserAddress deleteUserAddress;

	@Autowired
	DeleteUserAddressTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.deleteUserAddress = new DeleteUserAddress(dataSource);
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		Long userId = null;
		Long addressId = 1l;

		Throwable throwable = assertThrows(Throwable.class, () -> deleteUserAddress.execute(userId, addressId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void executeWithNoAddressIdThrowsException() {

		LOGGER.info("running test executeWithNoAddressIdThrowsException");

		Long userId = 1l;
		Long addressId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> deleteUserAddress.execute(userId, addressId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void execute() {

		LOGGER.info("running test execute");

		// find a user with an address
		PojoUser maxmin = jdbcQueryTestUtil.selectUserByAccountName("maxmin13")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_USER_NOT_FOUND_MSG));
		List<PojoAddress> addresses = jdbcQueryTestUtil.selectAddressesByUserId(maxmin.getId());

		assertEquals(2, addresses.size());

		PojoAddress address1 = addresses.stream().filter(each -> each.getPostalCode().equals("30010")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		addresses.stream().filter(each -> each.getPostalCode().equals("A65TF12")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		// run the test
		deleteUserAddress.execute(maxmin.getId(), address1.getId());

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(maxmin.getId());

		assertEquals(1, addresses.size());

		addresses.stream().filter(each -> each.getPostalCode().equals("A65TF12")).findFirst()
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		assertEquals(0, addresses.stream().filter(each -> each.getPostalCode().equals("30010")).count());
	}
}
