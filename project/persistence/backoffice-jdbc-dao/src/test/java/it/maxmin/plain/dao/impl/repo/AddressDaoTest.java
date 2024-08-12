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

		State state1 = daoTestUtil.findStateByName("Italy");

		assertEquals("Via borgo di sotto", addresses.get(0).getAddress());
		assertEquals("Rome", addresses.get(0).getCity());
		assertEquals(state1.getStateId(), addresses.get(0).getStateId());
		assertEquals("Lazio", addresses.get(0).getRegion());
		assertEquals("30010", addresses.get(0).getPostalCode());

		State state2 = daoTestUtil.findStateByName("Ireland");

		assertEquals("Connolly street", addresses.get(1).getAddress());
		assertEquals("Dublin", addresses.get(1).getCity());
		assertEquals(state2.getStateId(), addresses.get(1).getStateId());
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

		State state = daoTestUtil.findStateByName("Italy");

		Address address = new Address();
		address.setAddress("Via Nuova");
		address.setCity("Venice");
		address.setStateId(state.getStateId());
		address.setRegion("Veneto");
		address.setPostalCode("30033");

		Address newAddress = addressDao.create(address);

		assertEquals("Via Nuova", newAddress.getAddress());
		assertEquals("Venice", newAddress.getCity());
		assertEquals(state.getStateId(), newAddress.getStateId());
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

		State state1 = daoTestUtil.findStateByName("Italy");

		Address address1 = new Address();
		address1.setAddress("Via Nuova");
		address1.setCity("Venice");
		address1.setStateId(state1.getStateId());
		address1.setRegion("Veneto");
		address1.setPostalCode("30033");

		State state2 = daoTestUtil.findStateByName("Ireland");

		Address address2 = new Address();
		address2.setAddress("Via Vecchia");
		address2.setCity("Milano");
		address2.setStateId(state2.getStateId());
		address2.setRegion("Lombardia");
		address2.setPostalCode("43123");

		List<Address> addresses = List.of(address1, address2);

		addressDao.create(addresses);

		List<Address> newAddresses = daoTestUtil.findAllAddresses();

		assertTrue(newAddresses.size() == 2);

		assertEquals("Via Nuova", newAddresses.get(0).getAddress());
		assertEquals("Venice", newAddresses.get(0).getCity());
		assertEquals(state1.getStateId(), newAddresses.get(0).getStateId());
		assertEquals("Veneto", newAddresses.get(0).getRegion());
		assertEquals("30033", newAddresses.get(0).getPostalCode());

		assertEquals("Via Vecchia", newAddresses.get(1).getAddress());
		assertEquals("Milano", newAddresses.get(1).getCity());
		assertEquals(state2.getStateId(), newAddresses.get(1).getStateId());
		assertEquals("Lombardia", newAddresses.get(1).getRegion());
		assertEquals("43123", newAddresses.get(1).getPostalCode());
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

		State state = daoTestUtil.findStateByName("Ireland");

		Address address = new Address();
		address.setAddress("Via Vecchia");
		address.setCity("Ferrara");
		address.setStateId(state.getStateId());
		address.setRegion("Emilia Romagna");
		address.setPostalCode("12333");

		long addressId = daoTestUtil.createAddress(address).getAddressId();

		address = new Address();
		address.setAddressId(addressId);
		address.setAddress("Via Nuova");
		address.setCity("Modena");
		address.setStateId(state.getStateId());
		address.setRegion("Romagna");
		address.setPostalCode("11111");

		addressDao.update(address);

		Address updated = daoTestUtil.findAddressByAddressId(addressId);

		assertEquals("Via Nuova", updated.getAddress());
		assertEquals("Modena", updated.getCity());
		assertEquals(state.getStateId(), updated.getStateId());
		assertEquals("Romagna", updated.getRegion());
		assertEquals("11111", updated.getPostalCode());
	}

}
