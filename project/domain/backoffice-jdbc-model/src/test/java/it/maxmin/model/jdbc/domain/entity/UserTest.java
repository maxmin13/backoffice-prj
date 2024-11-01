package it.maxmin.model.jdbc.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

	@ParameterizedTest
	@ValueSource(strings = { "maxmin13" })
	void test_same_object_equal(String accountName) {

		User user = User.newInstance().withId(1l).withAccountName(accountName)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		Set<User> users = new HashSet<>();
		users.add(user);
		users.add(user);

		assertEquals(1, users.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, maxmin13" })
	void test_users_equal(String accountName1, String accountName2) {

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		Set<User> users = new HashSet<>();
		users.add(user1);
		users.add(user2);

		assertEquals(1, users.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, reginald999" })
	void test_users_not_equal(String accountName1, String accountName2) {

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		Set<User> users = new HashSet<>();
		users.add(user1);
		users.add(user2);

		assertEquals(2, users.size());
	}

	@Test
	void addRole_null() {

		User user = User.newInstance();
		user.addRole(null);

		assertEquals(0, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void addRole_equal(String roleName1, String roleName2) {

		UserRole role1 = UserRole.newInstance().withId(1l).withRoleName(roleName1);
		UserRole role2 = UserRole.newInstance().withId(1l).withRoleName(roleName2);

		User user = User.newInstance();
		user.addRole(role1);
		user.addRole(role2);

		assertEquals(1, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, User", })
	void addRole_not_equal(String roleName1, String roleName2) {

		UserRole role1 = UserRole.newInstance().withId(1l).withRoleName(roleName1);
		UserRole role2 = UserRole.newInstance().withId(1l).withRoleName(roleName2);

		User user = User.newInstance();
		user.addRole(role1);
		user.addRole(role2);

		assertEquals(2, user.getRoles().size());
	}

	@Test
	void addAddress_null() {

		User user = User.newInstance();
		user.addAddress(null);

		assertEquals(0, user.getAddresses().size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 30030" })
	void addAddress_equal(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		User user = User.newInstance();
		user.addAddress(address1);
		user.addAddress(address2);

		assertEquals(1, user.getAddresses().size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 112233" })
	void addAddress_not_equal(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		User user = User.newInstance();
		user.addAddress(address1);
		user.addAddress(address2);

		assertEquals(2, user.getAddresses().size());
	}

	@Test
	void getRole__null() {

		User user = User.newInstance();

		UserRole role = user.getRole(null);

		assertNull(role);
	}

	@ParameterizedTest
	@CsvSource({ "Administrator" })
	void getRole(String roleName) {

		UserRole role = UserRole.newInstance().withId(1l).withRoleName(roleName);

		User user = User.newInstance();
		user.getRoles().add(role);

		UserRole role1 = user.getRole("Administrator");

		assertEquals(1l, role1.getId());

		UserRole role2 = user.getRole("User");

		assertNull(role2);
	}

	@Test
	void getAddress__null() {

		User user = User.newInstance();

		Address address = user.getAddress(null);

		assertNull(address);
	}

	@ParameterizedTest
	@CsvSource({ "30030" })
	void getAddress(String postalCode) {

		Address address = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode);

		User user = User.newInstance();
		user.addAddress(address);

		Address address1 = user.getAddress("30030");

		assertEquals(1l, address1.getId());

		Address address2 = user.getAddress("111111");

		assertNull(address2);
	}
}
