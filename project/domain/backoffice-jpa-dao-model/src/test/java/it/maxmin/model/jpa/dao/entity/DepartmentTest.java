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
class DepartmentTest {

	@Autowired
	private EntityManagerFactory emf;

	@Test
	@DisplayName("Test equals and hashCode methods within the same and different persistent contexts")
	void testEquality() {

		EntityManager em1 = emf.createEntityManager();

		EntityTransaction tx1 = em1.getTransaction();
		tx1.begin();

		Query query = em1.createQuery("SELECT d FROM Department d WHERE d.name=:name");
		query.setParameter("name", "Accounts");
		Department department = (Department) query.getSingleResult();
		Long departmentId = department.getId();

		// same persistent context
		Department department1 = em1.find(Department.class, departmentId);
		Department department2 = em1.find(Department.class, departmentId);

		assertSame(department1, department2); // the same in-memory instance on the Java heap.
		assertEquals(department1, department2);
		assertEquals(department1.getId(), department2.getId());

		tx1.commit();
		em1.close();

		// different persistent context
		EntityManager em2 = emf.createEntityManager();

		EntityTransaction tx2 = em2.getTransaction();
		tx2.begin();

		Department department3 = em2.find(Department.class, departmentId);

		assertNotSame(department1, department3); // different in-memory instance on the Java heap.
		assertEquals(department1, department3);
		assertEquals(department1.getId(), department3.getId());

		tx2.commit();
		em2.close();

		// Test if the Department class is prepared for detached state and you can
		// safely put instances loaded in different persistence contexts into a Set.
		Set<Department> departments = new HashSet<>();
		departments.add(department1);
		departments.add(department2);
		departments.add(department3);

		assertEquals(1, departments.size());
	}

	@ParameterizedTest
	@ValueSource(strings = { "Production" })
	void testSameObjectIsEqual(String deparmentName) {

		Department department = Department.newInstance().withId(1l).withName(deparmentName);

		Set<Department> departments = new HashSet<>();
		departments.add(department);
		departments.add(department);

		assertEquals(1, departments.size());
	}

	@ParameterizedTest
	@CsvSource({ "Production, Production" })
	void testSameDepartmentNamesAreEqual(String deparmentName1, String deparmentName2) {

		Department department1 = Department.newInstance().withId(1l).withName(deparmentName1);
		Department department2 = Department.newInstance().withId(1l).withName(deparmentName2);

		Set<Department> departments = new HashSet<>();
		departments.add(department1);
		departments.add(department2);

		assertEquals(1, departments.size());
	}

	@ParameterizedTest
	@CsvSource({ "Production, Legal" })
	void testDifferentDepartmentNamesAreNotEqual(String deparmentName1, String deparmentName2) {

		Department department1 = Department.newInstance().withId(1l).withName(deparmentName1);
		Department department2 = Department.newInstance().withId(1l).withName(deparmentName2);

		Set<Department> departments = new HashSet<>();
		departments.add(department1);
		departments.add(department2);

		assertEquals(2, departments.size());
	}

}
