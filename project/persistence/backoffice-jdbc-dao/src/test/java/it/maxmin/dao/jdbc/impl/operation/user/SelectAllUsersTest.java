package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.constant.Department.LEGAL;
import static it.maxmin.dao.jdbc.impl.constant.Department.PRODUCTION;
import static it.maxmin.dao.jdbc.impl.constant.State.IRELAND;
import static it.maxmin.dao.jdbc.impl.constant.State.ITALY;
import static it.maxmin.dao.jdbc.impl.constant.Role.ADMINISTRATOR;
import static it.maxmin.dao.jdbc.impl.constant.Role.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import it.maxmin.dao.jdbc.JdbcUserTestUtil;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.Role;

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
				.withCity("Rome").withRegion("County Lazio").withState(State.newInstance().withId(5l).withName(ITALY.getName()).withCode(ITALY.getCode()));
		Department production = Department.newInstance().withId(3l).withName(PRODUCTION.getName());
		Role administrator = Role.newInstance().withId(2l).withRoleName(ADMINISTRATOR.getRoleName());
		User maxmin = User.newInstance().withId(1l).withAccountName("maxmin13").withFirstName("Max")
				.withLastName("Minardi").withBirthDate(LocalDate.of(1977, 10, 16)).withDepartment(production);
		maxmin.addRole(administrator);
		maxmin.addAddress(rome);
		
		Address dublin = Address.newInstance().withId(4l).withPostalCode("A65TF12").withDescription("Connolly street")
				.withCity("Dublin").withRegion("County Dublin").withState(State.newInstance().withId(5l).withName(IRELAND.getName()).withCode(IRELAND.getCode()));
		Department legal = Department.newInstance().withId(3l).withName(LEGAL.getName());
		Role user = Role.newInstance().withId(2l).withRoleName(USER.getRoleName());
		User artur = User.newInstance().withId(1l).withAccountName("artur").withFirstName("Arturo").withLastName("Art")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(legal);
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

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("maxmin13", "Max", "Minardi", LocalDate.of(1977, 10, 16), user1);

		// roles
		assertEquals(1, user1.getRoles().size());

		Role role1 = user1.getRole(ADMINISTRATOR.getRoleName());

		jdbcUserTestUtil.verifyRole(ADMINISTRATOR.getRoleName(), role1);

		// department
		jdbcUserTestUtil.verifyDepartment(PRODUCTION.getName(), user1.getDepartment());

		// addresses
		assertEquals(1, user1.getAddresses().size());

		Address address1 = user1.getAddress("30010");

		jdbcUserTestUtil.verifyAddress("30010", "Via borgo di sotto", "Rome", "County Lazio", address1);
		jdbcUserTestUtil.verifyState(ITALY.getName(), ITALY.getCode(), address1.getState());

		User user2 = usersFound.get(1);

		jdbcUserTestUtil.verifyUserWithNoCreatedAtDate("artur", "Arturo", "Art", LocalDate.of(1923, 10, 12), user2);

		// roles
		assertEquals(1, user2.getRoles().size());

		Role role2 = user2.getRole(USER.getRoleName());

		jdbcUserTestUtil.verifyRole(USER.getRoleName(), role2);

		// department
		jdbcUserTestUtil.verifyDepartment(LEGAL.getName(), user2.getDepartment());

		// addresses
		assertEquals(1, user2.getAddresses().size());

		Address address2 = user2.getAddress("A65TF12");

		jdbcUserTestUtil.verifyAddress("A65TF12", "Connolly street", "Dublin", "County Dublin", address2);
		jdbcUserTestUtil.verifyState(IRELAND.getName(), IRELAND.getCode(), address2.getState());
	}
}
