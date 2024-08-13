package it.maxmin.plain.dao.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

import it.maxmin.model.plain.pojos.Address;
import it.maxmin.model.plain.pojos.State;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.DaoTestUtil;
import it.maxmin.plain.dao.EmbeddedJdbcTestCfg;

@ExtendWith(MockitoExtension.class)
public class AddressDaoTest {

	private static Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DaoTestUtil daoTestUtil;

	@BeforeAll
	public static void setup() {
		LOGGER.info("Running AddressDaoTest tests");
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@BeforeEach
	public void init() {
		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_address.up.sql",
				"2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	public void cleanUp() {
		String[] scripts = { "2_user.down.sql", "2_useraddress.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_userrole.down.sql", "1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	public static void clear() {
		daoTestUtil.stopTestDB();
	}

	@Test
	public void findAddressesByUserId() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertTrue(addresses.size() == 2);

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
	public void findAddressesByUserId_none_associated_found() {

		String[] scripts = { "2_useraddress.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertTrue(addresses.size() == 0);
	}

	@Test
	public void findAddressesByUserId_none_found() {

		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		User user = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(user.getUserId());

		assertTrue(addresses.size() == 0);
	}

	@Test
	public void nullCreateThrowsException() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);
		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.create(address);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void create() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		State italy = daoTestUtil.findStateByName("Italy");

		Address address = new Address();
		address.setAddress("Via Nuova");
		address.setCity("Venice");
		address.setStateId(italy.getStateId());
		address.setRegion("Veneto");
		address.setPostalCode("30033");

		Address newAddress = addressDao.create(address);

		assertEquals("Via Nuova", newAddress.getAddress());
		assertEquals("Venice", newAddress.getCity());
		assertEquals(italy.getStateId(), newAddress.getStateId());
		assertEquals("Veneto", newAddress.getRegion());
		assertEquals("30033", newAddress.getPostalCode());
		assertNotNull(newAddress.getAddressId());
	}

	@Test
	public void create_list() {

		String[] scripts = { "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

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

		assertTrue(newAddresses.size() == 2);

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
	public void nullUpdateThrowsException() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	public void update() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

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
