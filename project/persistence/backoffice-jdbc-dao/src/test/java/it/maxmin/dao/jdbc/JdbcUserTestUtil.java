package it.maxmin.dao.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.pojo.PojoAddress;
import it.maxmin.model.jdbc.dao.pojo.PojoDepartment;
import it.maxmin.model.jdbc.dao.pojo.PojoRole;
import it.maxmin.model.jdbc.dao.pojo.PojoState;
import it.maxmin.model.jdbc.dao.pojo.PojoUser;

public class JdbcUserTestUtil {

	public void verifyAddress(String postalCode, String description, String city, String region, Address actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
		assertNotNull(actual.getVersion());
		assertNotNull(actual.getId());
	}

	public void verifyAddress(String postalCode, Address actual) {
		assertNotNull(actual);
		assertEquals(postalCode, actual.getPostalCode());
	}

	public void verifyAddress(String postalCode, String description, String city, String region, PojoAddress actual) {
		assertNotNull(actual);
		assertNotNull(actual.getId());
		assertEquals(postalCode, actual.getPostalCode());
		assertEquals(description, actual.getDescription());
		assertEquals(city, actual.getCity());
		assertEquals(region, actual.getRegion());
		assertNotNull(actual.getVersion());
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

	public void verifyUser(String accountName, User actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
	}

	public void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate, User actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getDepartment());
		assertNotNull(actual.getAddresses());
		assertNotNull(actual.getRoles());
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getVersion());
	}

	public void verifyUser(String accountName, String firstName, String lastName, LocalDate birthDate,
			PojoUser actual) {
		assertNotNull(actual.getId());
		assertEquals(accountName, actual.getAccountName());
		assertEquals(firstName, actual.getFirstName());
		assertEquals(lastName, actual.getLastName());
		assertEquals(birthDate, actual.getBirthDate());
		assertNotNull(actual.getCreatedAt());
		assertNotNull(actual.getVersion());
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
