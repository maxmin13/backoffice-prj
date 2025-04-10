package it.maxmin.dao.jdbc.ut.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.dao.jdbc.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.constant.Role.USER;
import static it.maxmin.dao.jdbc.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.constant.State.ITALY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.exception.JdbcDaoTestException;
import it.maxmin.dao.jdbc.impl.operation.user.SelectAllUsers;
import it.maxmin.dao.jdbc.util.JdbcUserTestUtil;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;

class SelectAllUsersTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAllUsersTest.class);

	private JdbcUserTestUtil jdbcUserTestUtil;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@InjectMocks
	private SelectAllUsers selectAllUsers;

	SelectAllUsersTest() {
		MockitoAnnotations.openMocks(this);
		jdbcUserTestUtil = new JdbcUserTestUtil();
	}

	@SuppressWarnings("unchecked")
	@Test
	void execute() {

		LOGGER.info("running test execute");

		Address rome = Address.newInstance().withId(4l).withPostalCode("30010").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio")
				.withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()))
				.withVersion(1);
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withName(ADMINISTRATOR.getName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production)
				.withVersion(2).withCreatedAt(LocalDateTime.now());
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);

		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin")
				.withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()))
				.withVersion(3);
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withName(USER.getName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal).withVersion(4)
				.withCreatedAt(LocalDateTime.now());
		artur.addRole(user);
		artur.addAddress(dublin);

		List<User> users = List.of(maxmin, artur);

		JdbcTemplate template = mock(JdbcTemplate.class);
		when(template.query(anyString(), any(ResultSetExtractor.class))).thenReturn(users);
		when(jdbcTemplate.getJdbcTemplate()).thenReturn(template);

		// run the test
		List<User> usersFound = selectAllUsers.execute();

		assertEquals(2, usersFound.size());

		User user1 = usersFound.get(0);

		jdbcUserTestUtil.verifyUser("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// roles
		assertEquals(1, user1.getRoles().size());

		Role role1 = user1.getRole(ADMINISTRATOR.getName())
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user1.getDepartment());

		// addresses
		assertEquals(1, user1.getAddresses().size());

		Address address1 = user1.getAddress("30010")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());

		User user2 = usersFound.get(1);

		jdbcUserTestUtil.verifyUser("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// roles
		assertEquals(1, user2.getRoles().size());

		Role role2 = user2.getRole(USER.getName()).orElseThrow(() -> new JdbcDaoTestException(ERROR_ROLE_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyRole(USER.getName(), role2);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user2.getDepartment());

		// addresses
		assertEquals(1, user2.getAddresses().size());

		Address address2 = user2.getAddress("A65TF12")
				.orElseThrow(() -> new JdbcDaoTestException(ERROR_ADDRESS_NOT_FOUND_MSG));

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.getState());
	}
}
