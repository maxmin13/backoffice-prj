package it.maxmin.model.jpa.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.model.jpa.dao.JpaModelSpringContextTestCfg;
import it.maxmin.model.jpa.dao.exception.JpaModelTestException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = { "classpath:database/1_create_database.up.sql", "classpath:database/2_role.up.sql",
		"classpath:database/2_state.up.sql",
		"classpath:database/2_department.up.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = { "classpath:database/2_state.down.sql", "classpath:database/2_department.down.sql",
		"classpath:database/2_role.down.sql",
		"classpath:database/1_create_database.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringJUnitConfig(classes = { JpaModelSpringContextTestCfg.class })
class UserTest {

	@Autowired
	private EntityManagerFactory emf;

	@Test
	@Sql(scripts = { "classpath:database/2_user.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("Test equals and hashCode methods within the same and different persistent contexts")
	void testEquality() {

		EntityManager em1 = emf.createEntityManager();

		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();

		// prepare the test
		Query query = em1.createQuery("SELECT d FROM Department d WHERE d.name=:name");
		query.setParameter("name", "Accounting");
		Department department = (Department) query.getSingleResult();

		User user = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(department);

		em1.persist(user);

		Long userId = user.getId();

		em1.flush();
		
		// same persistent context
		User user1 = em1.find(User.class, userId);
		User user2 = em1.find(User.class, userId);

		assertSame(user1, user2); // the same in-memory instance on the Java heap.
		assertEquals(user1, user2);
		assertEquals(user1.getId(), user2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		User user3 = em2.find(User.class, userId);

		assertNotSame(user1, user3); // different in-memory instance on the Java heap.
		assertEquals(user1, user3);
		assertEquals(user1.getId(), user3.getId());

		tx2.commit();
		em2.close();

		// Test if the User class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<User> users = new HashSet<>();
		users.add(user1);
		users.add(user2);
		users.add(user3);

		assertEquals(1, users.size());
	}

	@ParameterizedTest
	@ValueSource(strings = { "maxmin13" })
	void testSameObjectIsEqual(String accountName) {

		User user = User.newInstance().withId(1l).withAccountName(accountName)
				.withBirthDate(LocalDate.of(1988, Month.MARCH, 12)).withFirstName("Max").withLastName("Min")
				.withDepartment(Department.newInstance().withId(1l));

		Set<User> users = new HashSet<>();

		assertEquals(true, users.add(user));
		assertEquals(false, users.add(user));
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

		assertEquals(true, users.add(user1));
		assertEquals(false, users.add(user2));
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

		assertEquals(true, users.add(user1));
		assertEquals(true, users.add(user2));
		assertEquals(2, users.size());
	}

	@Test
	void addNullRole() {

		User user = User.newInstance();

		assertEquals(false, user.addRole(null));
		assertEquals(0, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void addSameRole(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		User user = User.newInstance();

		assertEquals(true, user.addRole(role1));
		assertEquals(false, user.addRole(role2));
		assertEquals(1, user.getRoles().size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, User", })
	void addDifferentRoles(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		User user = User.newInstance();

		assertEquals(true, user.addRole(role1));
		assertEquals(true, user.addRole(role2));
		assertEquals(2, user.getRoles().size());
	}

	@Test
	void removeNullRole() {

		User user = User.newInstance();

		assertEquals(false, user.removeRole(null));
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

		Role role1 = user.getRole(name).orElseThrow(() -> new JpaModelTestException("Error role not found"));

		assertEquals(1l, role1.getId());

		assertEquals(true, user.getRole("User").isEmpty());
	}

	@Test
	void addNullAddress() {

		User user = User.newInstance();

		assertEquals(false, user.addAddress(null));
		assertEquals(0, user.getAddresses().size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 30030" })
	void addSameAddress(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		User user = User.newInstance();

		assertEquals(true, user.addAddress(address1));
		assertEquals(false, user.addAddress(address2));
		assertEquals(1, user.getAddresses().size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 112233" })
	void addDIfferentAddresses(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		User user = User.newInstance();

		assertEquals(true, user.addAddress(address1));
		assertEquals(true, user.addAddress(address2));
		assertEquals(2, user.getAddresses().size());
	}

	@Test
	void removeNullAddress() {

		User user = User.newInstance();

		assertEquals(false, user.removeAddress(null));
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
				.orElseThrow(() -> new JpaModelTestException("Error address not found"));

		assertEquals(1l, address1.getId());

		assertEquals(true, user.getAddress("111111").isEmpty());
	}
}
