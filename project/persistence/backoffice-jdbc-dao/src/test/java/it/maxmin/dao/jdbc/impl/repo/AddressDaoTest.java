package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddresses;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressByPostalCode;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressByUserIdAndPostalCode;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressesByUserId;
import it.maxmin.dao.jdbc.impl.operation.address.UpdateAddress;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;

@ExtendWith(MockitoExtension.class)
class AddressDaoTest {

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

	@Mock
	private SelectAddressByPostalCode selectAddressByPostalCode;

	@Mock
	private SelectAddressByUserIdAndPostalCode selectAddressByUserIdAndPostalCode;

	@InjectMocks
	private AddressDaoImpl addressDao;

	AddressDaoTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void selectAddressesByUserIdWithNoIdThrowsException() {

		LOGGER.info("running test selectAddressesByUserIdWithNoIdThrowsException");

		Long userId = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.selectAddressesByUserId(userId));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectAddressesByUserId() {

		LOGGER.info("running test selectAddressesByUserId");

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033").withVersion(1l);
		Address address2 = Address.newInstance().withId(3l).withDescription("Via Vecchia").withCity("Rome")
				.withState(State.newInstance().withId(4l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withRegion("County Lazio").withPostalCode("20021").withVersion(1l);

		when(selectAddressesByUserId.execute(1l)).thenReturn(List.of(address1, address2));

		// run the test
		List<Address> addresses = addressDao.selectAddressesByUserId(1l);

		verify(selectAddressesByUserId, times(1)).execute(1l);

		assertEquals(2, addresses.size());

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "County Veneto", addresses.get(0));
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), addresses.get(0).getState());

		jdbcUserTestUtil.verifyAddress("20021", "Via Vecchia", "Rome", "County Lazio", addresses.get(1));
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), addresses.get(1).getState());
	}

	@Test
	void selectAddressByPostalCodeWithNoPostalCodeThrowsException() {

		LOGGER.info("running test selectAddressByPostalCodeWithNoPostalCodeThrowsException");

		String postalCode = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.selectAddressByPostalCode(postalCode));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectAddressByPostalCode() {

		LOGGER.info("running test selectAddressByPostalCode");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033").withVersion(1l);

		when(selectAddressByPostalCode.execute("30033")).thenReturn(Optional.of(address));

		// run the test

		Address addressFound = addressDao.selectAddressByPostalCode("30033")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		verify(selectAddressByPostalCode, times(1)).execute("30033");

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "County Veneto", addressFound);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), addressFound.getState());
	}

	@Test
	void selectAddressByUserIdAndPostalCodeWithNoPostalCodeThrowsException() {

		LOGGER.info("running test selectAddressByUserIdAndPostalCodeWithNoPostalCodeThrowsException");

		Long userId = 1l;
		String postalCode = null;

		Throwable throwable = assertThrows(Throwable.class,
				() -> addressDao.selectAddressByUserIdAndPostalCode(userId, postalCode));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectAddressByUserIdAndPostalCodeWithNoUserIdThrowsException() {

		LOGGER.info("running test selectAddressByUserIdAndPostalCodeWithNoUserIdThrowsException");

		Long userId = null;
		String postalCode = "30010";

		Throwable throwable = assertThrows(Throwable.class,
				() -> addressDao.selectAddressByUserIdAndPostalCode(userId, postalCode));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void selectAddressByUserIdAndPostalCode() {

		LOGGER.info("running test selectAddressByUserIdAndPostalCode");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033").withVersion(1l);

		when(selectAddressByUserIdAndPostalCode.execute(1L, "30033")).thenReturn(Optional.of(address));

		// run the test

		Address addressFound = addressDao.selectAddressByUserIdAndPostalCode(1L, "30033")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		verify(selectAddressByUserIdAndPostalCode, times(1)).execute(1L, "30033");

		jdbcUserTestUtil.verifyAddress("30033", "Via Nuova", "Venice", "County Veneto", addressFound);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), addressFound.getState());
	}

	@Test
	void insertWithNoAddressThrowsException() {

		LOGGER.info("running test insertWithNoAddressThrowsException");

		Address address = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.insert(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void insertWithAddressIdThrowsException() {

		LOGGER.info("running test insertWithAddressIdThrowsException");

		Address address = Address.newInstance().withId(1l).withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.insert(address));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void insert() {

		LOGGER.info("running test insert");

		Address address = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033").withVersion(0l);

		when(insertAddress.execute(address)).thenReturn(address);

		// run the test
		Address newAddress = addressDao.insert(address);

		verify(insertAddress, times(1)).execute(newAddress);

		jdbcUserTestUtil.verifyAddressIgnoringIdField("30033", "Via Nuova", "Venice", "County Veneto", newAddress);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), newAddress.getState());
	}

	@Test
	void insertListWithNoAddressesThrowsException() {

		LOGGER.info("running test insertListWithNoAddressesThrowsException");

		List<Address> addresses = null;

		Throwable throwable = assertThrows(Throwable.class, () -> addressDao.insertList(addresses));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@Test
	void insertList() {

		LOGGER.info("running test insertList");

		Address address1 = Address.newInstance().withDescription("Via Nuova").withCity("Venice")
				.withState(State.newInstance().withId(2l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withRegion("County Veneto").withPostalCode("30033");
		Address address2 = Address.newInstance().withDescription("Via Vecchia").withCity("Rome")
				.withState(State.newInstance().withId(4l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withRegion("County Lazio").withPostalCode("20021");

		List<Address> addresses = List.of(address1, address2);

		// run the test
		addressDao.insertList(addresses);

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
				.withRegion("County Veneto").withPostalCode("30033").withVersion(2l);

		// run the test
		addressDao.update(address);

		verify(updateAddress, times(1)).execute(address);
	}

}
