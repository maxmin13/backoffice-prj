package it.maxmin.model.jpa.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashSet;
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
class StateTest {
	
	@Autowired
	private EntityManagerFactory emf;

	@Test
	@DisplayName("Test equals and hashCode methods within the same and different persistent contexts")
	void testEquality() {

		EntityManager em1 = emf.createEntityManager();

		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();

		Query query = em1.createQuery("SELECT s FROM State s WHERE s.name=:name");
		query.setParameter("name", "Ireland");
		State state = (State) query.getSingleResult();
		Long stateId = state.getId();

		// same persistent context
		State state1 = em1.find(State.class, stateId);
		State state2 = em1.find(State.class, stateId);

		assertSame(state1, state2); // the same in-memory instance on the Java heap.
		assertEquals(state1, state2);
		assertEquals(state1.getId(), state2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		State state3 = em2.find(State.class, stateId);

		assertNotSame(state1, state3); // different in-memory instance on the Java heap.
		assertEquals(state1, state3);
		assertEquals(state1.getId(), state3.getId());

		tx2.commit();
		em2.close();

		// Test if the State class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<State> states = new HashSet<>();
		states.add(state1);
		states.add(state2);
		states.add(state3);

		assertEquals(1, states.size());
	}

	@ParameterizedTest
	@ValueSource(strings = { "IT" })
	void testSameObjectIsEqual(String code) {

		State state = State.newInstance().withId(1l).withName("Italy").withCode(code);

		Set<State> states = new HashSet<>();
		states.add(state);
		states.add(state);

		assertEquals(1, states.size());
	}

	@ParameterizedTest
	@CsvSource({ "IT, IT" })
	void testSameCodesAreEqual(String code1, String code2) {

		State state1 = State.newInstance().withId(1l).withName("Italy").withCode(code1);

		State state2 = State.newInstance().withId(1l).withName("Italy").withCode(code2);

		Set<State> states = new HashSet<>();
		states.add(state1);
		states.add(state2);

		assertEquals(1, states.size());
	}

	@ParameterizedTest
	@CsvSource({ "IT, IE" })
	void testDifferentCodesAreNotEqual(String code1, String code2) {

		State state1 = State.newInstance().withId(1l).withName("Italy").withCode(code1);

		State state2 = State.newInstance().withId(1l).withName("Italy").withCode(code2);

		Set<State> states = new HashSet<>();
		states.add(state1);
		states.add(state2);

		assertEquals(2, states.size());
	}

}
