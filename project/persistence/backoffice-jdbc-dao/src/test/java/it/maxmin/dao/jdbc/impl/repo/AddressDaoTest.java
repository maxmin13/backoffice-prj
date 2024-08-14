package it.maxmin.dao.jdbc.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.DaoTestUtil;
import it.maxmin.dao.jdbc.EmbeddedJdbcTestCfg;
import it.maxmin.model.jdbc.Address;
import it.maxmin.model.jdbc.State;
import it.maxmin.model.jdbc.User;

@ExtendWith(MockitoExtension.class)
class AddressDaoTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DataSource dataSource;
	private static DaoTestUtil daoTestUtil;

	@BeforeAll
	static void setup() {
		LOGGER.info("Running AddressDaoTest tests");
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@BeforeEach
	void init() {
		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_address.up.sql",
				"2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_user.down.sql", "2_useraddress.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_userrole.down.sql", "1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	static void clear() {
		daoTestUtil.stopTestDB();
	}

	@Test
	void findAddressesByUserId() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertEquals(2, addresses.size());

		State italy = daoTestUtil.findStateByName("Italy");

		assertEquals("Via borgo di sotto", addresses.get(0).getAddress());
		assertEquals("Rome", addresses.get(0).getCity());
		assertEquals(italy.getStateId(), addresses.get(0).getStateId());
		assertEquals("Lazio", addresses.get(0).getRegion());
		assertEquals("30010", addresses.get(0).getPostalCode());

		State ireland = daoTestUtil.findStateByName("Ireland");

		assertEquals("Connolly street", addresses.get(1).getAddress());
		assertEquals("Dublin", addresses.get(1).getCity());
		assertEquals(ireland.getStateId(), addresses.get(1).getStateId());
		assertEquals("County Dublin", addresses.get(1).getRegion());
		assertEquals("A65TF12", addresses.get(1).getPostalCode());
	}

	@Test
	void findAddressesByUserId_none_associated_found() {

		String[] scripts = { "2_useraddress.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertEquals(0, addresses.size());
	}

	@Test
	void findAddressesByUserId_none_found() {

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertEquals(0, addresses.size());
	}

	@Test
	void nullCreateThrowsException() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);
		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.create(address);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		State italy = daoTestUtil.findStateByName("Italy");

		Address address = new Address();
		address.setAddress("Via Nuova");
		address.setCity("Venice");
		address.setStateId(italy.getStateId());
		address.setRegion("Veneto");
		address.setPostalCode("30033");

		// run the test
		Address newAddress = addressDao.create(address);

		assertEquals("Via Nuova", newAddress.getAddress());
		assertEquals("Venice", newAddress.getCity());
		assertEquals(italy.getStateId(), newAddress.getStateId());
		assertEquals("Veneto", newAddress.getRegion());
		assertEquals("30033", newAddress.getPostalCode());
		assertNotNull(newAddress.getAddressId());
	}

	@Test
	void create_list() {

		String[] scripts = { "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		State italy = daoTestUtil.findStateByName("Italy");

		Address venice = new Address();
		venice.setAddress("Via Nuova");
		venice.setCity("Venice");
		venice.setStateId(italy.getStateId());
		venice.setRegion("Veneto");
		venice.setPostalCode("30033");

		State ireand = daoTestUtil.findStateByName("Ireland");

		Address ireland = new Address();
		ireland.setAddress("Connolly street");
		ireland.setCity("Dublin");
		ireland.setStateId(ireand.getStateId());
		ireland.setRegion("County Dublin");
		ireland.setPostalCode("A65TF12");

		List<Address> addresses = List.of(venice, ireland);

		addressDao.create(addresses);

		List<Address> newAddresses = daoTestUtil.findAllAddresses();

		assertEquals(2, newAddresses.size());

		assertEquals("Via Nuova", newAddresses.get(0).getAddress());
		assertEquals("Venice", newAddresses.get(0).getCity());
		assertEquals(venice.getStateId(), newAddresses.get(0).getStateId());
		assertEquals("Veneto", newAddresses.get(0).getRegion());
		assertEquals("30033", newAddresses.get(0).getPostalCode());

		assertEquals("Connolly street", addresses.get(1).getAddress());
		assertEquals("Dublin", addresses.get(1).getCity());
		assertEquals(ireland.getStateId(), addresses.get(1).getStateId());
		assertEquals("County Dublin", addresses.get(1).getRegion());
		assertEquals("A65TF12", addresses.get(1).getPostalCode());
	}

	@Test
	void nullUpdateThrowsException() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		State ireland = daoTestUtil.findStateByName("Ireland");

		Address address = new Address();
		address.setAddress("Via Vecchia");
		address.setCity("Ferrara");
		address.setStateId(ireland.getStateId());
		address.setRegion("Emilia Romagna");
		address.setPostalCode("12333");

		long addressId = daoTestUtil.createAddress(address).getAddressId();

		address = new Address();
		address.setAddressId(addressId);
		address.setAddress("Connolly street");
		address.setCity("Dublin");
		address.setStateId(ireland.getStateId());
		address.setRegion("County Dublin");
		address.setPostalCode("A65TF12");

		addressDao.update(address);

		Address updated = daoTestUtil.findAddressByAddressId(addressId);

		assertEquals("Connolly street", updated.getAddress());
		assertEquals("Dublin", updated.getCity());
		assertEquals(ireland.getStateId(), updated.getStateId());
		assertEquals("County Dublin", updated.getRegion());
		assertEquals("A65TF12", updated.getPostalCode());
	}

}
