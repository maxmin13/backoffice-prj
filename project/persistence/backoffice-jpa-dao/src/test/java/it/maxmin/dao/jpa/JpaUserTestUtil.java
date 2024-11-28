package it.maxmin.dao.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import it.maxmin.model.jpa.dao.entity.Address;
import it.maxmin.model.jpa.dao.entity.Department;
import it.maxmin.model.jpa.dao.entity.Role;
import it.maxmin.model.jpa.dao.entity.State;
import it.maxmin.model.jpa.dao.entity.User;
import it.maxmin.model.jpa.dao.pojo.PojoAddress;
import it.maxmin.model.jpa.dao.pojo.PojoDepartment;
import it.maxmin.model.jpa.dao.pojo.PojoRole;
import it.maxmin.model.jpa.dao.pojo.PojoState;
import it.maxmin.model.jpa.dao.pojo.PojoUser;

public class JpaUserTestUtil {

	public void verifyAddress(String postalCode, String description, String city, String region, Address actual) {
		assertNotNull(actual.getId());
		verifyAddressWithoutId(postalCode, description, city, region, actual);
	}
	
	public void verifyAddressWithoutId(String postalCode, String description, String city, String region, Address actual) {
		assertNotNull(actual);
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
	}

	public void verifyAddress(String postalCode, String description, String city, String region, PojoAddress actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
	}

	public void verifyState(String name, String code, State actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(code, actual.getCode());
	}
	
	public void verifyState(String name, String code, PojoState actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
		assertEquals(code, actual.getCode());
	}

	public void verifyDepartment(String name, Department actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
	}
	
	public void verifyDepartment(String name, PojoDepartment actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
	}

	public void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate, User actual) {
		verifyUserWithNoCreatedAtDate(accountName, firstName, lastName, birthDate, actual);
		assertNotNull(actual.getCreatedAt());
	}
	
	public void verifyUserWithNoCreatedAtDate(String accountName, String firstName, String lastName, LocalDate birthDate, User actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getDepartment());
		assertNotNull(actual.getAddresses());
		assertNotNull(actual.getRoles());
	}

	public void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate, PojoUser actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getCreatedAt());
	}

	public void verifyRole(String name, Role actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
	}
	
	public void verifyRole(String name, PojoRole actual) {
		assertNotNull(actual.getId());
		assertEquals(name, actual.getName());
	}
}
