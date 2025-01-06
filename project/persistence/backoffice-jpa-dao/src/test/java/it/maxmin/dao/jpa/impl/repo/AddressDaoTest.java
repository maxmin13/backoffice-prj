	package it.maxmin.dao.jpa.impl.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jpa.JpaDaoSpringContextUnitTestCfg;
import it.maxmin.dao.jpa.api.repo.AddressDao;

@SpringJUnitConfig(classes = { JpaDaoSpringContextUnitTestCfg.class })
class AddressDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	AddressDao addressDao;

	@Autowired
	public AddressDaoTest(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	@Test
	@Order(1)
	@DisplayName("01. should find no address")
	void findByIdNotFound() {

		LOGGER.info("running test findByIdNotFound");

		// run the test
//		Optional<Address> address = addressDao.findById(0);

//		assertTrue(address.isEmpty());
	}

	@Test
	@Order(2)
//	@Sql(scripts = { "classpath:database/2_userrole.down.sql", "classpath:database/2_useraddress.down.sql",
//			"classpath:database/2_user.down.sql",
//			"classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("02. verify eagerly loaded properties")
	void findById1() {

		LOGGER.info("running test findById1");

		// TODO
	}

	

}
