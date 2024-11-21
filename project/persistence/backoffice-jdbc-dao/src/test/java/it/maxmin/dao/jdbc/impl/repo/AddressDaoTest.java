package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddresses;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressesByUserId;
import it.maxmin.dao.jdbc.impl.operation.address.UpdateAddress;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.State;

@ExtendWith(MockitoExtension.class)
class AddressDaoTest  {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;
	
	@Mock
	private UpdateAddress updateAddress;
	
	@Mock
	private InsertAddress insertAddress;
	
	@Mock
	private InsertAddresses insertAddresses;
	
	@Mock
	private SelectAddressesByUserId selectAddressesByUserId;
	
	@InjectMocks
	private AddressDaoImpl addressDao;

	AddressDaoTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void findAddressesByUserIdWithNoIdThrowsException() {

		LOGGER.info("running test findAddressesByUserIdWithNoIdThrowsException");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.findAddressesByUserId(userId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void findAddressesByUserId() {

		LOGGER.info("running test findAddressesByUserId");

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");
		Address address2 = Address.newInstance().withId(3l).withDescription("Via Vecchia").withCity("Rome")
				.withState(State.newInstance().withId(4l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withRegion("County Lazio").withPostalCode("20021");

		when(selectAddressesByUserId.execute(1l)).thenReturn(List.of(address1, address2));

		// run the test
		List<Address> addresses = addressDao.findAddressesByUserId(1l);

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "County Veneto", addresses.get(0));
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), addresses.get(0).getState());

		jdbcUserTestUtil.verifyAddress("20021", "Via Vecchia", "Rome", "County Lazio", addresses.get(1));
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), addresses.get(1).getState());
	}

	@Test
	void createWithNoAddressThrowsException() {

		LOGGER.info("running test createWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.create(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void createWithAddressIdThrowsException() {

		LOGGER.info("running test createWithAddressIdThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.create(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void create() {

		LOGGER.info("running test create");

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");
		
		when(insertAddress.execute(address)).thenReturn(address);

		// run the test
		address = addressDao.create(address);

		jdbcUserTestUtil.verifyAddressWithoutId("30033", "Via Nuova", "Venice", "County Veneto", address);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address.getState());
	}

	@Test
	void createListWithNoAddressesThrowsException() {

		LOGGER.info("running test createListWithNoAddressesThrowsException");

		List<Address> addresses = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.create(addresses));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void createList() {

		LOGGER.info("running test createList");

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");
		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Rome")
				.withState(State.newInstance().withId(4l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withRegion("County Lazio").withPostalCode("20021");

		List<Address> addresses = List.of(address1, address2);
		
		// run the test
		addressDao.create(addresses);

		verify(insertAddresses, times(1)).execute(addresses);
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
	void updateWithNoAddressIdThrowsException() {

		LOGGER.info("running test updateWithNoAddressIdThrowsException");

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.update(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void update() {

		LOGGER.info("running test update");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");

		// run the test
		addressDao.update(address);

		verify(updateAddress, times(1)).execute(address);
	}

}
