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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.JdbcDaoTestUtil;
import it.maxmin.dao.jdbc.JdbcTestCfg;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.pojo.PojoAddress;
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

class AddressDaoTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	private static AnnotationConfigApplicationContext springJdbcCtx;
	private static NamedParameterJdbcTemplate jdbcTemplate;
	private static DataSource dataSource;
	private static JdbcDaoTestUtil daoTestUtil;
	private static State ireland;
	private static State italy;

	@BeforeAll
	static void setup() {
		
		springJdbcCtx = new AnnotationConfigApplicationContext(JdbcTestCfg.class);
		jdbcTemplate = springJdbcCtx.getBean("jdbcTemplate", NamedParameterJdbcTemplate.class);
		dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);
		daoTestUtil = springJdbcCtx.getBean("daoTestUtil", JdbcDaoTestUtil.class);

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		daoTestUtil.runDBScripts(scripts);

		ireland = daoTestUtil.findStateByName("Ireland");
		italy = daoTestUtil.findStateByName("Italy");
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);
	}

	@AfterAll
	static void clear() {
		String[] scripts = { "2_state.down.sql", "2_department.down.sql", "2_userrole.down.sql",
				"1_create_database.down.sql" };
		daoTestUtil.runDBScripts(scripts);
		springJdbcCtx.close();
		daoTestUtil.stopTestDB();
	}

	@Test
	void findAddressesByUserId() {

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		PojoUser maxmin = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(2, addresses.size());

		assertNotNull(addresses.get(0).getId());
		assertEquals("Via borgo di sotto", addresses.get(0).getDescription());
		assertEquals("Rome", addresses.get(0).getCity());
		assertEquals("Lazio", addresses.get(0).getRegion());
		assertEquals("30010", addresses.get(0).getPostalCode());

		assertEquals(italy.getId(), addresses.get(0).getState().getId());
		assertEquals(italy.getName(), addresses.get(0).getState().getName());

		assertNotNull(addresses.get(1).getId());
		assertEquals("Connolly street", addresses.get(1).getDescription());
		assertEquals("Dublin", addresses.get(1).getCity());
		assertEquals("County Dublin", addresses.get(1).getRegion());
		assertEquals("A65TF12", addresses.get(1).getPostalCode());

		assertEquals(ireland.getId(), addresses.get(1).getState().getId());
		assertEquals(ireland.getName(), addresses.get(1).getState().getName());
	}

	@Test
	void findAddressesByUserId_none_associated_found() {

		String[] scripts = { "2_useraddress.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		PojoUser maxmin = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void findAddressesByUserId_none_found() {

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		PojoUser maxmin = daoTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

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

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		// run the test
		Address newAddress = addressDao.create(address);

		assertEquals("Via Nuova", newAddress.getDescription());
		assertEquals("Venice", newAddress.getCity());
		assertEquals(italy.getId(), newAddress.getState().getId());
		assertEquals("Veneto", newAddress.getRegion());
		assertEquals("30033", newAddress.getPostalCode());
		assertNotNull(newAddress.getId());
	}

	@Test
	void create_list() {

		String[] scripts = { "2_address.down.sql" };
		daoTestUtil.runDBScripts(scripts);

		AddressDaoImpl addressDao = new AddressDaoImpl();
		addressDao.setJdbcTemplate(dataSource, jdbcTemplate);

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("30033");

		Address address2 = Address.newInstance().withDescription("Connolly street").withCity("Dublin")
				.withState(State.newInstance().withId(ireland.getId())).withRegion("County Dublin")
				.withPostalCode("A65TF12");

		List<Address> addresses = List.of(address1, address2);

		// run the test
		addressDao.create(addresses);

		List<PojoAddress> newAddresses = daoTestUtil.findAllAddresses();

		assertEquals(2, newAddresses.size());

		assertEquals("Via Nuova", newAddresses.get(0).getDescription());
		assertEquals("Venice", newAddresses.get(0).getCity());
		assertEquals(italy.getId(), newAddresses.get(0).getStateId());
		assertEquals("Veneto", newAddresses.get(0).getRegion());
		assertEquals("30033", newAddresses.get(0).getPostalCode());

		assertEquals("Connolly street", newAddresses.get(1).getDescription());
		assertEquals("Dublin", newAddresses.get(1).getCity());
		assertEquals(ireland.getId(), newAddresses.get(1).getStateId());
		assertEquals("County Dublin", newAddresses.get(1).getRegion());
		assertEquals("A65TF12", newAddresses.get(1).getPostalCode());
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

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withStateId(ireland.getId()).withRegion("County Dublin")
				.withPostalCode("A65TF33");

		long addressId = daoTestUtil.createAddress(address).getId();

		Address addressUpdated = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("23465").withId(addressId);

		// run the test
		addressDao.update(addressUpdated);

		PojoAddress updated = daoTestUtil.findAddressByAddressId(addressId);

		assertEquals("Via Nuova", updated.getDescription());
		assertEquals("Venice", updated.getCity());
		assertEquals(italy.getId(), updated.getStateId());
		assertEquals("Veneto", updated.getRegion());
		assertEquals("23465", updated.getPostalCode());
	}

}
