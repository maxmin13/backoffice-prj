package it.maxmin.domain.jpa.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserRoleTest {
	
	@ParameterizedTest
	@ValueSource(strings = {"Administrator"})
	void testSameObjectEqualMethod(String roleName) {

		UserRole role = UserRole.newInstance().withId(1l).withRoleName(roleName);
		
		Set<UserRole> roles = new HashSet<>();
		roles.add(role);
		roles.add(role);

		assertEquals(1, roles.size());
	}
	
	@ParameterizedTest
	@CsvSource({ "Administrator, Administrator" })
	void testRolesEqualMethod(String roleName1, String roleName2) {

		UserRole role1 = UserRole.newInstance().withId(1l).withRoleName(roleName1);
		UserRole role2 = UserRole.newInstance().withId(1l).withRoleName(roleName2);

		Set<UserRole> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(1, roles.size());
	}

	
	@ParameterizedTest
	@CsvSource({ "Administrator, Legal" })
	void testRolesNotEqualMethod(String roleName1, String roleName2) {

		UserRole role1 = UserRole.newInstance().withId(1l).withRoleName(roleName1);
		UserRole role2 = UserRole.newInstance().withId(1l).withRoleName(roleName2);

		Set<UserRole> roles = new HashSet<>();
		roles.add(role1);
		roles.add(role2);

		assertEquals(2, roles.size());
	}

}
