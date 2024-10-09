package it.maxmin.dao.jpa.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import it.maxmin.domain.jpa.entity.Address;
import it.maxmin.domain.jpa.entity.Department;
import it.maxmin.domain.jpa.entity.State;
import it.maxmin.domain.jpa.entity.User;
import it.maxmin.domain.jpa.entity.UserRole;
import it.maxmin.domain.jpa.pojo.PojoAddress;
import it.maxmin.domain.jpa.pojo.PojoUser;

abstract class BaseTest {
	
	void verifyDepartment(String name, Department actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertNotNull(actual.getUsers());
	}
	
	void verifyState(String name, String code, State actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(code, actual.getCode());
	}
	
	void verifyAddress(String postalCode, String description, String city, String region, Address actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
	}
	
	void verifyAddress(String postalCode, String description, String city, String region, PojoAddress actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
	}
		
	void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate, User actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getDepartment());
		assertNotNull(actual.getAddresses());
		assertNotNull(actual.getRoles());
	}
	
	void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate, PojoUser actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getDepartmentId());
	}

	void verifyRole(String roleName, UserRole actual) {
		assertNotNull(actual.getId());
		assertEquals(roleName, actual.getRoleName());
	}
}
