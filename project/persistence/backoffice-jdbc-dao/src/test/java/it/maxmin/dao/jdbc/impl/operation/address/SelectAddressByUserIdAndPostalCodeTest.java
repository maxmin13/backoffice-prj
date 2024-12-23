package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;

class SelectAddressByUserIdAndPostalCodeTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAddressByUserIdAndPostalCodeTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;
	
	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private SelectAddressByUserIdAndPostalCode selectAddressByUserIdAndPostalCode;

	SelectAddressByUserIdAndPostalCodeTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}
	
	@Test
	void executeWithNoUserId() {

		LOGGER.info("running test executeWithNoUserId");

		Long userId = null;
		String postalCode = "30010";

		Throwable throwable = assertThrows(Throwable.class, () -> selectAddressByUserIdAndPostalCode.execute(userId, postalCode));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}
	
	@Test
	void executeWithNoPostalCode() {

		LOGGER.info("running test executeWithNoPostalCode");

		Long userId = 1l;
		String postalCode = null;

		Throwable throwable = assertThrows(Throwable.class, () -> selectAddressByUserIdAndPostalCode.execute(userId, postalCode));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@SuppressWarnings("unchecked")
	@Test
	void execute() {

		LOGGER.info("running test execute");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withVersion(2);
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withName(ADMINISTRATOR.getName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production);
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		List<Address> addresses = List.of(rome);

		when(jdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), any(ResultSetExtractor.class)))
				.thenReturn(addresses);

		// run the test
		Address address = selectAddressByUserIdAndPostalCode.execute(maxmin.getId(), rome.getPostalCode())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address.getState());
	}
}
