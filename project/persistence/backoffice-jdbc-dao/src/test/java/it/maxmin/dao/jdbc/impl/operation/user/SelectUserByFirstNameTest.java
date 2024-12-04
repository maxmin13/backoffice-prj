package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.dao.jdbc.JdbcDaoTestException;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;

class SelectUserByFirstNameTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAllUsersTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private SelectUserByFirstName selectUserByFirstName;

	SelectUserByFirstNameTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void executeWithNoFirstName() {

		LOGGER.info("running test executeWithNoFirstName");

		String firstName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> selectUserByFirstName.execute(firstName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@SuppressWarnings("unchecked")
	@Test
	void execute() {

		LOGGER.info("running test execute");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withVersion(1l);
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withName(ADMINISTRATOR.getName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production)
				.withVersion(1l).withCreatedAt(LocalDateTime.now());
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		List<User> users = List.of(maxmin);

		when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(ResultSetExtractor.class)))
				.thenReturn(users);

		// run the test
		List<User> usersFound = selectUserByFirstName.execute("Max");

		assertEquals(1, usersFound.size());

		User userFound = usersFound.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), userFound);

		// roles
		assertEquals(1, userFound.getRoles().size());

		Role role1 = userFound.getRole(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), userFound.getDepartment());

		// addresses
		assertEquals(1, userFound.getAddresses().size());

		Address address1 = userFound.getAddress("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());
	}
}