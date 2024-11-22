package it.maxmin.model.jdbc.dao.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class RoleTest {

	@ParameterizedTest
	@ValueSource(strings = { "Administrator" })
	void testSameObjectIsEqual(String roleName) {

		Role role = Role.newInstance().withId(1l).withRoleName(roleName);

		Set<Role> roles = new HashSet<>();
		roles.add(role);
		roles.add(role);

		assertEquals(1, roles.size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void testSameRoleNamesAreEqual(String roleName1, String roleName2) {

		Role role1 = Role.newInstance().withId(1l).withRoleName(roleName1);
		Role role2 = Role.newInstance().withId(1l).withRoleName(roleName2);

		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(1, roles.size());
	}

	@ParameterizedTest
	@CsvSource({ "Administrator, Legal" })
	void testDifferentRoleNamesAreNotEqual(String roleName1, String roleName2) {

		Role role1 = Role.newInstance().withId(1l).withRoleName(roleName1);
		Role role2 = Role.newInstance().withId(1l).withRoleName(roleName2);

		Set<Role> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(2, roles.size());
	}

}
