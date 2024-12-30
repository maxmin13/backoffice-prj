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
class RoleTest {

	@Autowired
	private EntityManagerFactory emf;

	@Test
	@DisplayName("Test equals and hashCode methods within the same and different persistent contexts")
	void testEquality() {

		EntityManager em1 = emf.createEntityManager();

		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();

		Query query = em1.createQuery("SELECT r FROM Role r WHERE r.name=:name");
		query.setParameter("name", "Administrator");
		Role role = (Role) query.getSingleResult();
		Long roleId = role.getId();

		// same persistent context
		Role role1 = em1.find(Role.class, roleId);
		Role role2 = em1.find(Role.class, roleId);

		assertSame(role1, role2); // the same in-memory instance on the Java heap.
		assertEquals(role1, role2);
		assertEquals(role1.getId(), role2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		Role role3 = em2.find(Role.class, roleId);

		assertNotSame(role1, role3); // different in-memory instance on the Java heap.
		assertEquals(role1, role3);
		assertEquals(role1.getId(), role3.getId());

		tx2.commit();
		em2.close();

		// Test if the Role class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);
		roles.add(role3);

		assertEquals(1, roles.size());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Administrator" })
	void testSameObjectIsEqual(String name) {

		Role role = Role.newInstance().withId(1l).withName(name);

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		roles.add(role);

		assertEquals(1, roles.size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void testSameRoleNamesAreEqual(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(1, roles.size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Legal" })
	void testDifferentRoleNamesAreNotEqual(String name1, String name2) {

		Role role1 = Role.newInstance().withId(1l).withName(name1);
		Role role2 = Role.newInstance().withId(1l).withName(name2);

		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(2, roles.size());
	}

}
