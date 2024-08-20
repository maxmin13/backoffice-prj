package it.maxmin.domain.hibernate.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class DepartmentTest {
	
	@ParameterizedTest
	@ValueSource(strings = {"Production"})
	void test_same_object_equal(String deparmentName) {

		Department department = Department.newInstance().withId(1l).withName(deparmentName);
		
		Set<Department> departments = new HashSet<>();
		departments.add(department);
		departments.add(department);

		assertEquals(1, departments.size());
	}
	
	@ParameterizedTest
	@CsvSource({ "Production, Production" })
	void test_departments_equal(String deparmentName1, String deparmentName2) {

		Department department1 = Department.newInstance().withId(1l).withName(deparmentName1);
		Department department2 = Department.newInstance().withId(1l).withName(deparmentName2);
		
		Set<Department> departments = new HashSet<>();
		departments.add(department1);
		departments.add(department2);

		assertEquals(1, departments.size());
	}

	@ParameterizedTest
	@CsvSource({ "Production, Legal" })
	void test_departments_not_equal(String deparmentName1, String deparmentName2) {

		Department department1 = Department.newInstance().withId(1l).withName(deparmentName1);
		Department department2 = Department.newInstance().withId(1l).withName(deparmentName2);
		
		Set<Department> departments = new HashSet<>();
		departments.add(department1);
		departments.add(department2);

		assertEquals(2, departments.size());
	}

}
