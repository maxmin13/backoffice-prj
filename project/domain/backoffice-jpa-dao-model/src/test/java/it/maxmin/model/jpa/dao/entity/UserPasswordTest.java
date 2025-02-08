package it.maxmin.model.jpa.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import it.maxmin.model.jpa.dao.JpaModelSpringContextTestCfg;
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
class UserPasswordTest {

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

		UserPassword password = UserPassword.newInstance().withValue("secret").withUser(user)
				.withEffDate(Instant.now());

		em1.persist(password);

		Long passwordId = password.getId();

		em1.flush();

		// same persistent context
		UserPassword password1 = em1.find(UserPassword.class, passwordId);
		UserPassword password2 = em1.find(UserPassword.class, passwordId);

		assertSame(password1, password2); // the same in-memory instance on the Java heap.
		assertEquals(password1, password2);
		assertEquals(password1.getId(), password2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		UserPassword password3 = em2.find(UserPassword.class, passwordId);

		assertNotSame(password1, password3); // different in-memory instance on the Java heap.
		assertEquals(password1, password3);
		assertEquals(password1.getId(), password3.getId());

		tx2.commit();
		em2.close();

		// Test if the UserPassword class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(password1);
		passwords.add(password2);
		passwords.add(password3);

		assertEquals(1, passwords.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000" })
	void testSameObjectIsEqual(String accountName, int yearEffDate) {

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user = User.newInstance().withId(1l).withAccountName(accountName).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword = UserPassword.newInstance().withUser(user).withEffDate(Instant.now());

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword);
		passwords.add(userPassword);

		assertEquals(1, passwords.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000, maxmin13, 2000" })
	void testSameAccountNamesAndEffDateAreEqual(String accountName1, int yearEffDate1, String accountName2,
			int yearEffDate2) {

		Instant effDate = Instant.now();
		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword1 = UserPassword.newInstance().withUser(user1).withEffDate(effDate);
		UserPassword userPassword2 = UserPassword.newInstance().withUser(user2).withEffDate(effDate);

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword1);
		passwords.add(userPassword2);

		assertEquals(1, passwords.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 1999, reginald, 2023" })
	void testDifferentAccountNamesAndEffDateAreNotEqual(String accountName1, int yearEffDate1, String accountName2,
			int yearEffDate2) {

		Instant effDate = Instant.now();
		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword1 = UserPassword.newInstance().withUser(user1).withEffDate(effDate);

		UserPassword userPassword2 = UserPassword.newInstance().withUser(user2).withEffDate(effDate);

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword1);
		passwords.add(userPassword2);

		assertEquals(2, passwords.size());
	}

}
