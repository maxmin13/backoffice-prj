package it.maxmin.dao.jdbc.impl.operation.user;

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
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.dao.jdbc.DaoTestException;
import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;

class SelectUserByAccountNameTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAllUsersTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private SelectUserByAccountName selectUserByAccountName;

	SelectUserByAccountNameTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@Test
	void executeWithNoAccountName() {

		LOGGER.info("running test executeWithNoAccountName");

		String accountName = null;

		Throwable throwable = assertThrows(Throwable.class, () -> selectUserByAccountName.execute(accountName));

		assertEquals(IllegalArgumentException.class, throwable.getClass());
	}

	@SuppressWarnings("unchecked")
	@Test
	void execute() {

		LOGGER.info("running test execute");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()));
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withRoleName(ADMINISTRATOR.getRoleName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production);
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		List<User> users = List.of(maxmin);

		when(jdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(ResultSetExtractor.class)))
				.thenReturn(users);

		// run the test
		Optional<User> userFound = selectUserByAccountName.execute("maxmin13");
		User user = userFound.orElseThrow(() -> new DaoTestException("Error user not found"));

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16),
				user);

		// roles
		assertEquals(1, user.getRoles().size());

		Optional<Role> role1 = user.getRole(ADMINISTRATOR.getRoleName());

		assertEquals(true, role1.isPresent());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1.get());

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user.getDepartment());

		// addresses
		assertEquals(1, user.getAddresses().size());

		Optional<Address> address1 = user.getAddress("30010");

		assertEquals(true, address1.isPresent());

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1.get());
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.get().getState());
	}
}
