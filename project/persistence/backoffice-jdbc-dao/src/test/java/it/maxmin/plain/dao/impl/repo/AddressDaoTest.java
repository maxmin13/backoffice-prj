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
		springJdbcCtx = new AnnotationConfigApplicationContext(EmbeddedJdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", DaoTestUtil.class);
	}

	@BeforeEach
	public void init() {
		String[] scripts = { "create_tables.sql", "insert_states.sql", "insert_addresses.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	public void cleanUp() {
		String[] scripts = { "delete_addresses.sql", "delete_states.sql", "drop_tables.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	public static void clear() {
		daoTestUtil.stopTestDB();
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
		
		String[] scripts = { "delete_addresses.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(jdbcTemplate);

		State state = daoTestUtil.findStateByName("Italy");

		Address address1 = new Address();
		address1.setAddress("Via Nuova");
		address1.setCity("Venice");
		address1.setStateId(state.getStateId());
		address1.setRegion("Veneto");
		address1.setPostalCode("30033");
		
		Address address2 = new Address();
		address2.setAddress("Via Vecchia");
		address2.setCity("Milano");
		address2.setStateId(state.getStateId());
		address2.setRegion("Lombardia");
		address2.setPostalCode("43123");

		List<Address> addresses = List.of(address1, address2);

		addressDao.create(addresses);
		
		List<Address> newAddresses = daoTestUtil.findAllAddresses();
		
		assertTrue(newAddresses.size() == 2);
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

		long addressId = daoTestUtil.insertAddress(address).getAddressId();

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
