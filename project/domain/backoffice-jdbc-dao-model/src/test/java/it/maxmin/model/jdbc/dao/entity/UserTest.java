package it.maxmin.model.jdbc.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import it.maxmin.model.jdbc.dao.ModelTestException;

class UserTest {

	@ParameterizedTest
	@ValueSource(strings = { "maxmin13" })
	void testSameObjectIsEqual(String accountName) {

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
	void testSameAccountNamesAreEqual(String accountName1, String accountName2) {

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
	void testDifferentAccountNamesAreNotEqual(String accountName1, String accountName2) {

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
	void addNullRole() {

		User user = User.newInstance();
		user.addRole(null);

		assertEquals(0, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void addEqualRoles(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		User user = User.newInstance();
		user.addRole(role1);
		user.addRole(role2);

		assertEquals(1, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, User", })
	void addDifferentRoles(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		User user = User.newInstance();
		user.addRole(role1);
		user.addRole(role2);

		assertEquals(2, user.getRoles().size());
	}

	@Test
	void addNullAddress() {

		User user = User.newInstance();
		user.addAddress(null);

		assertEquals(0, user.getAddresses().size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 30030" })
	void addEqualAddress(String postalCode1, String postalCode2) {

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
	void addDifferentAddress(String postalCode1, String postalCode2) {

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
	void getNullRole() {

		User user = User.newInstance();

		Optional<Role> role = user.getRole(null);

		assertEquals(true, role.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator" })
	void getRole(String name) {

		Role role = Role.newInstance().withId(1l).withName(name);

		User user = User.newInstance();
		user.getRoles().add(role);

		Role role1 = user.getRole("Administrator").orElseThrow(() -> new ModelTestException("Error role not found"));

		assertEquals(1l, role1.getId());

		assertEquals(true, user.getRole("User").isEmpty());
	}

	@Test
	void getNullAddress() {

		User user = User.newInstance();

		Optional<Address> address = user.getAddress(null);

		assertEquals(true, address.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "30030" })
	void getAddress(String postalCode) {

		Address address = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode);

		User user = User.newInstance();
		user.addAddress(address);

		Address address1 = user.getAddress("30030")
				.orElseThrow(() -> new ModelTestException("Error address not found"));

		assertEquals(1l, address1.getId());

		assertEquals(true, user.getAddress("111111").isEmpty());
	}
}
