	package it.maxmin.dao.jpa.ut.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jpa.api.repo.AddressDao;
import it.maxmin.dao.jpa.config.JpaDaoSpringContextUnitTestCfg;
import it.maxmin.model.jpa.dao.entity.Address;

@SpringJUnitConfig(classes = { JpaDaoSpringContextUnitTestCfg.class })
@Sql(scripts = { "classpath:database/up/1_create_database.up.sql", "classpath:database/up/2_accountstatus.up.sql",
		"classpath:database/up/2_accounttype.up.sql", "classpath:database/up/2_department.up.sql",
		"classpath:database/up/2_role.up.sql", "classpath:database/up/2_state.up.sql",
		"classpath:database/up/2_transactiontype.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/down/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AddressDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	AddressDao addressDao;

	@Autowired
	public AddressDaoTest(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	@Test
	@Order(1)
	@Sql(scripts = { "classpath:database/down/2_userrole.down.sql", "classpath:database/down/2_useraddress.down.sql",
			"classpath:database/down/2_user.down.sql",
			"classpath:database/down/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("01. verify an empty list is returned")
	void testFindAllNotFound() {

		LOGGER.info("running test testFindAllNotFound");

		// run the test
		List<Address> users = addressDao.findAll();

		assertEquals(0, users.size());
	}
	

}
