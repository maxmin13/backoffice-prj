package it.maxmin.dao.jdbc.impl.operation.user;

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
class DeleteUserAddressesTest extends JdbcBaseTestDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteUserAddressesTest.class);
	private DeleteUserAddresses deleteUserAddresses;

	@Autowired
	DeleteUserAddressesTest(JdbcQueryTestUtil jdbcQueryTestUtil, JdbcUserTestUtil jdbcUserTestUtil,
			DataSource dataSource) {
		super(jdbcQueryTestUtil, jdbcUserTestUtil);
		this.deleteUserAddresses = new DeleteUserAddresses(dataSource);
	}

	@Test
	void executeWithNoUserIdThrowsException() {

		LOGGER.info("running test executeWithNoUserIdThrowsException");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> deleteUserAddresses.execute(userId));

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

		// run the test
		deleteUserAddresses.execute(maxmin.getId());

		addresses = jdbcQueryTestUtil.selectAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}
}
