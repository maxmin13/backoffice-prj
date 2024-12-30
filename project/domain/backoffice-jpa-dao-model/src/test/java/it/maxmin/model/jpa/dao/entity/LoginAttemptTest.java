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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
class LoginAttemptTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptTest.class);

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
		query.setParameter("name", "Accounts");
		Department department = (Department) query.getSingleResult();

		User user = User.newInstance().withAccountName("carl23").withBirthDate(LocalDate.of(1982, 9, 1))
				.withFirstName("Carlo").withLastName("Rossi").withDepartment(department);

		em1.persist(user);

		LoginAttempt login = LoginAttempt.newInstance().withUser(user).withSuccess(true).withLoginAt(Instant.now());

		em1.persist(login);

		Long loginId = login.getId();

		// same persistent context
		LoginAttempt login1 = em1.find(LoginAttempt.class, loginId);
		LOGGER.info("login1 loginAt: {}", login1.getLoginAt());
		LoginAttempt login2 = em1.find(LoginAttempt.class, loginId);
		LOGGER.info("login2 loginAt: {}", login2.getLoginAt());

		assertSame(login1, login2); // the same in-memory instance on the Java heap.
		assertEquals(login1, login2);
		assertEquals(login1.getId(), login2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		LoginAttempt login3 = em2.find(LoginAttempt.class, loginId);
		LOGGER.info("login3 loginAt: {}", login3.getLoginAt());

		assertNotSame(login1, login3); // different in-memory instance on the Java heap.
		assertEquals(login1, login3);
		assertEquals(login1.getId(), login3.getId());

		tx2.commit();
		em2.close();

		// TODO 
		Set<LoginAttempt> logins = new HashSet<>();
		logins.add(login1);
		logins.add(login2);
		logins.add(login3);

		assertEquals(1, logins.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000" })
	void testSameObjectIsEqual(String accountName, int yearLoginAt) {

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user = User.newInstance().withId(1l).withAccountName(accountName).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt = LoginAttempt.newInstance().withId(1l).withUser(user).withSuccess(true)
				.withLoginAt(Instant.now());

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt);
		attempts.add(loginAttempt);

		assertEquals(1, attempts.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000, maxmin13, 2000" })
	void testSameAccountNamesAndLoginAtAreEqual(String accountName1, int yearLoginAt1, String accountName2,
			int yearLoginAt2) {

		Instant nowUtc = Instant.now();

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt1 = LoginAttempt.newInstance().withId(1l).withUser(user1).withSuccess(true)
				.withLoginAt(nowUtc);

		LoginAttempt loginAttempt2 = LoginAttempt.newInstance().withId(1l).withUser(user2).withSuccess(true)
				.withLoginAt(nowUtc);

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt1);
		attempts.add(loginAttempt2);

		assertEquals(1, attempts.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 1999, reginald, 2023" })
	void testDifferentAccountNamesAndLoginAtAreNotEqual(String accountName1, int yearLoginAt1, String accountName2,
			int yearLoginAt2) {

		Instant nowUtc = Instant.now();
		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt1 = LoginAttempt.newInstance().withId(1l).withUser(user1).withSuccess(true)
				.withLoginAt(nowUtc);

		LoginAttempt loginAttempt2 = LoginAttempt.newInstance().withId(1l).withUser(user2).withSuccess(true)
				.withLoginAt(nowUtc);

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt1);
		attempts.add(loginAttempt2);

		assertEquals(2, attempts.size());
	}

}
