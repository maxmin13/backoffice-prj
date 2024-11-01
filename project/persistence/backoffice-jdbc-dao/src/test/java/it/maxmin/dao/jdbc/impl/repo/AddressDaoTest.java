package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.impl.repo.constant.Department.ACCOUNTS;
import static it.maxmin.dao.jdbc.impl.repo.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.repo.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.repo.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.repo.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.dao.jdbc.JdbcQueryTestUtil;
import it.maxmin.dao.jdbc.JdbcUnitTestContextCfg;
import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoDepartment;
import it.maxmin.model.jdbc.domain.pojo.PojoState;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcUnitTestContextCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressDaoTest extends JdbcBaseTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	JdbcQueryTestUtil jdbcQueryTestUtil;
	AddressDao addressDao;
	PojoDepartment legal;
	PojoDepartment accounts;
	PojoDepartment production;
	PojoState italy;
	PojoState ireland;

	@Autowired
	AddressDaoTest(JdbcQueryTestUtil jdbcQueryTestUtil, AddressDao addressDao) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.addressDao = addressDao;

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		this.legal = jdbcQueryTestUtil.findDepartmentByName(LEGAL.getName());
		this.accounts = jdbcQueryTestUtil.findDepartmentByName(ACCOUNTS.getName());
		this.production = jdbcQueryTestUtil.findDepartmentByName(PRODUCTION.getName());
		this.italy = jdbcQueryTestUtil.findStateByName(ITALY.getName());
		this.ireland = jdbcQueryTestUtil.findStateByName(IRELAND.getName());
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_department.down.sql", "2_userrole.down.sql", "1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@Test
	void findAddressesByUserId() {

		LOGGER.info("running test findAddressesByUserId");

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(2, addresses.size());

		verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", addresses.get(0));
		verifyState(ITALY.getName(), ITALY.getCode(), addresses.get(0).getState());

		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", addresses.get(1));
		verifyState(IRELAND.getName(), IRELAND.getCode(), addresses.get(1).getState());
	}

	@Test
	void findAddressesByUserIdNoneAssociatedToUser() {

		LOGGER.info("running test findAddressesByUserIdNoneAssociatedToUser");

		String[] scripts = { "2_useraddress.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void findAddressesByUserIdNotFound() {

		LOGGER.info("running test findAddressesByUserIdNotFound");

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void createWithNoAddressThrowsException() {

		LOGGER.info("running test createWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.create(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create() {
		
		LOGGER.info("running test create");

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		// run the test
		Address newAddress = addressDao.create(address);

		verifyAddress("30033", "Via Nuova", "Venice", "Veneto", newAddress);
	}

	@Test
	void createList() {
		
		LOGGER.info("running test createList");

		String[] scripts = { "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		Address address2 = Address.newInstance().withDescription("Connolly street").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF12");

		List<Address> addresses = List.of(address1, address2);

		// run the test
		addressDao.create(addresses);

		List<PojoAddress> newAddresses = jdbcQueryTestUtil.findAllAddresses();

		assertEquals(2, newAddresses.size());

		verifyAddress("30033", "Via Nuova", "Venice", "Veneto", newAddresses.get(0));
		verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", newAddresses.get(1));
	}

	@Test
	void updateWithNoAddressThrowsException() {
		
		LOGGER.info("running test updateWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.update(address);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {
		
		LOGGER.info("running test update");

		// Find an existing address
		PojoAddress address = jdbcQueryTestUtil.findAddressByPostalCode("30010");

		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS
		// TODO ASSERT THE ADDRESS

		// Change the address
		Address addressUpdated = Address.newInstance().withId(address.getId()).withDescription("Via Nuova")
				.withCity("Venice").withState(State.newInstance().withId(italy.getId())).withRegion("Veneto")
				.withPostalCode("30010");

		// run the test
		addressDao.update(addressUpdated);

		PojoAddress updated = jdbcQueryTestUtil.findAddressByPostalCode("30010");

		verifyAddress("30010", "Via Nuova", "Venice", "Veneto", updated);
	}

}
