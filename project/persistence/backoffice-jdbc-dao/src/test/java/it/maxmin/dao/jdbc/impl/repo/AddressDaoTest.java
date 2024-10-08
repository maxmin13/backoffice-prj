package it.maxmin.dao.jdbc.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import it.maxmin.model.jdbc.domain.pojo.PojoUser;

@SpringJUnitConfig(classes = { JdbcUnitTestContextCfg.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AddressDaoTest {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	@Mock
	State ireland;
	@Mock
	State italy;

	JdbcQueryTestUtil jdbcQueryTestUtil;
	AddressDao addressDao;

	@Autowired
	AddressDaoTest(JdbcQueryTestUtil jdbcQueryTestUtil, AddressDao addressDao) {
		this.jdbcQueryTestUtil = jdbcQueryTestUtil;
		this.addressDao = addressDao;

		String[] scripts = { "1_create_database.up.sql", "2_userrole.up.sql", "2_state.up.sql", "2_department.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@BeforeEach
	void init() {
		String[] scripts = { "2_address.up.sql", "2_user.up.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
		
		when(italy.getId()).thenReturn(1l);
		when(italy.getName()).thenReturn("Italy");
		when(italy.getCode()).thenReturn("IT");
		when(ireland.getId()).thenReturn(2l);
		when(ireland.getName()).thenReturn("Ireland");
		when(ireland.getCode()).thenReturn("IE");
	}

	@AfterEach
	void cleanUp() {
		String[] scripts = { "2_useraddress.down.sql", "2_user.down.sql", "2_address.down.sql", "2_state.down.sql",
				"2_department.down.sql", "2_userrole.down.sql", "1_create_database.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);
	}

	@Test
	void findAddressesByUserId() {

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

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
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void findAddressesByUserId_none_found() {

		// delete all the addresses
		String[] scripts = { "2_useraddress.down.sql", "2_address.down.sql" };
		jdbcQueryTestUtil.runDBScripts(scripts);

		PojoUser maxmin = jdbcQueryTestUtil.findUserByAccountName("maxmin13");

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(maxmin.getId());

		assertEquals(0, addresses.size());
	}

	@Test
	void nullCreateThrowsException() {

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.create(address);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create() {

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

		Throwable throwable = assertThrows(Throwable.class, () -> {
			addressDao.update(null);
		});

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		PojoAddress address = PojoAddress.newInstance().withDescription("Via Vecchia").withCity("Dublin")
				.withStateId(ireland.getId()).withRegion("County Dublin").withPostalCode("A65TF33");

		long addressId = jdbcQueryTestUtil.createAddress(address).getId();

		Address addressUpdated = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(italy.getId())).withRegion("Veneto").withPostalCode("23465")
				.withId(addressId);

		// run the test
		addressDao.update(addressUpdated);

		PojoAddress updated = jdbcQueryTestUtil.findAddressByAddressId(addressId);

		assertEquals("Via Nuova", updated.getDescription());
		assertEquals("Venice", updated.getCity());
		assertEquals(italy.getId(), updated.getStateId());
		assertEquals("Veneto", updated.getRegion());
		assertEquals("23465", updated.getPostalCode());
	}

}
