package it.maxmin.model.jpa.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

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
class AddressTest {

	@Autowired
	private EntityManagerFactory emf;
	
	@Test
	@Sql(scripts = { "classpath:database/2_address.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	@DisplayName("Test equals and hashCode methods within the same and different persistent contexts")
	void testEquality() {
		
		EntityManager em1 = emf.createEntityManager();
		
		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();
		
		// prepare the test
		Query query = em1.createQuery("SELECT s FROM State s WHERE s.name=:name");
		query.setParameter("name", "Italy");
		State state = (State) query.getSingleResult();

		Address address = Address.newInstance().withPostalCode("33322").withDescription("Via borgo di sotto")
				.withCity("Rome").withRegion("County Lazio").withState(state);

		em1.persist(address);
		
		Long addressId = address.getId();
		
		em1.flush();
		
		// same persistent context
		Address address1 = em1.find(Address.class, addressId);
		Address address2 = em1.find(Address.class, addressId);

		assertSame(address1, address2); // the same in-memory instance on the Java heap.
		assertEquals(address1, address2);
		assertEquals(address1.getId(), address2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();
		
		Address address3 = em2.find(Address.class, addressId);
		
		assertNotSame(address1, address3); // different in-memory instance on the Java heap.
		assertEquals(address1, address3);
		assertEquals(address1.getId(), address3.getId());
		
		tx2.commit();
		em2.close();
		
		// Test if the Address class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);
		addresses.add(address3);
		
		assertEquals(1, addresses.size());
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"30010"})
	void testSameObjectIsEqual(String postalCode) {

		Address address = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode);
		
		Set<Address> addresses = new HashSet<>();
		addresses.add(address);
		addresses.add(address);

		assertEquals(1, addresses.size());
	}

	@ParameterizedTest
	@CsvSource({ "30030, 30030" })
	void testSamePostalCodesAreEqual(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);

		assertEquals(1, addresses.size());
	}

	@ParameterizedTest
	@CsvSource({ "54123, 30030" })
	void testDifferentPostalCodesAreNotEqual(String postalCode1, String postalCode2) {

		Address address1 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode1);

		Address address2 = Address.newInstance().withId(1l).withDescription("Via Roma").withCity("Roma")
				.withState(State.newInstance().withId(1l)).withRegion("Lazio").withPostalCode(postalCode2);

		Set<Address> addresses = new HashSet<>();
		addresses.add(address1);
		addresses.add(address2);

		assertEquals(2, addresses.size());
	}
	
	@Test
	void addNullUser() {

		Address address = Address.newInstance();

		assertEquals(false, address.addUser(null));
		assertEquals(0, address.getUsers().size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, maxmin13" })
	void addSameUser(String accountName1, String accountName2) {

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1);
		User user2 = User.newInstance().withId(1l).withAccountName(accountName2);

		Address address = Address.newInstance();

		assertEquals(true, address.addUser(user1));
		assertEquals(false, address.addUser(user2));
		assertEquals(1, address.getUsers().size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, artur23" })
	void addDifferentUsers(String accountName1, String accountName2) {

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1);
		User user2 = User.newInstance().withId(1l).withAccountName(accountName2);

		Address address = Address.newInstance();

		assertEquals(true, address.addUser(user1));
		assertEquals(true, address.addUser(user2));
		assertEquals(2, address.getUsers().size());
	}

	@Test
	void removeNullUser() {

		Address address = Address.newInstance();

		assertEquals(false, address.removeUser(null));
	}

	@Test
	void getNullUser() {

		Address address = Address.newInstance();

		Optional<User> user = address.getUser(null);

		assertEquals(true, user.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13" })
	void getUser(String accountName) {

		User user = User.newInstance().withId(1l).withAccountName(accountName);

		Address address = Address.newInstance();
		address.getUsers().add(user);

		User user1 = address.getUser(accountName).orElseThrow(() -> new JpaModelTestException("Error user not found"));

		assertEquals(1l, user1.getId());

		assertEquals(true, address.getUser("artur23").isEmpty());
	}

}
