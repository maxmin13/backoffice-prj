package it.maxmin.model.jpa.dao.entity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
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
class LoginAttemptSequenceGeneratorTest {

	@Autowired
	private EntityManagerFactory emf;
	private EntityManager em;
	private EntityTransaction tx;

	@BeforeEach
	public void init() {
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
	}

	@AfterEach
	public void clear() {
		tx.commit();
		if (em.isOpen()) {
			em.close();
		}
	}

	@Test
	@Sql(scripts = { "classpath:database/2_user.down.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
	void testEntityIdentifierGeneration() {

		Query query = em.createQuery("SELECT d FROM Department d WHERE d.name=:name");
		query.setParameter("name", "Production");
		Department department = (Department) query.getSingleResult();

		User maxmin = User.newInstance().withAccountName("maxmin13").withFirstName("Max").withLastName("Min")
				.withBirthDate(LocalDate.of(1923, 10, 12)).withDepartment(department);
		em.persist(maxmin);
		em.flush();

		LoginAttempt loginAttempt = LoginAttempt.newInstance().withUser(maxmin).withSuccess(true)
				.withLoginAt(Instant.now());

		em.persist(loginAttempt);
		em.flush();

		assertNotNull(loginAttempt.getId());
	}
}
