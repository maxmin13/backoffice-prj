package it.maxmin.dao.jpa.impl.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import it.maxmin.domain.jpa.entity.Address;
import it.maxmin.domain.jpa.entity.Department;
import it.maxmin.domain.jpa.entity.State;
import it.maxmin.domain.jpa.entity.User;
import it.maxmin.domain.jpa.entity.UserRole;
import it.maxmin.domain.jpa.pojo.PojoAddress;
import it.maxmin.domain.jpa.pojo.PojoDepartment;
import it.maxmin.domain.jpa.pojo.PojoState;
import it.maxmin.domain.jpa.pojo.PojoUser;
import it.maxmin.domain.jpa.pojo.PojoUserRole;

abstract class BaseTest {
	
	@Mock
	State ireland;
	@Mock
	State italy;
	@Mock
	Department accounts;
	@Mock
	Department legal;
	@Mock
	Department production;
	@Mock
	UserRole administrator;
	@Mock
	UserRole user;
	@Mock
	UserRole worker;
	
	void verifyDepartment(String name, Department actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertNotNull(actual.getUsers());
	}
	
	void verifyDepartment(String name, PojoDepartment actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
	}
	
	void verifyState(String name, String code, State actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(code, actual.getCode());
	}
	
	void verifyState(String name, String code, PojoState actual) {
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
	
	void verifyRole(String roleName, PojoUserRole actual) {
		assertNotNull(actual.getId());
		assertEquals(roleName, actual.getRoleName());
	}
	
	@BeforeEach
	void init() {		
		when(italy.getName()).thenReturn("Italy");
		when(italy.getCode()).thenReturn("IT");
		when(ireland.getName()).thenReturn("Ireland");
		when(ireland.getCode()).thenReturn("IE");
		when(accounts.getName()).thenReturn("Accounts");
		when(legal.getName()).thenReturn("Legal");
		when(production.getName()).thenReturn("Production");
		when(administrator.getRoleName()).thenReturn("Administrator");
		when(user.getRoleName()).thenReturn("User");
		when(worker.getRoleName()).thenReturn("Worker");
	}
}
