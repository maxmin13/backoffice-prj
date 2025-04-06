package it.maxmin.model.jdbc.dao.ut.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.dao.entity.UserPassword;

class UserPasswordTest {

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000" })
	void testSameObjectIsEqual(String accountName, int yearEffDate) {

		LocalDateTime effDate = LocalDateTime.of(yearEffDate, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user = User.newInstance().withId(1l).withAccountName(accountName).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword = UserPassword.newInstance().withUser(user).withEffDate(effDate);

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword);
		passwords.add(userPassword);

		assertEquals(1, passwords.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000, maxmin13, 2000" })
	void testSameAccountNamesAndEffDatesAreEqual(String accountName1, int yearEffDate1, String accountName2,
			int yearEffDate2) {

		LocalDateTime effDate1 = LocalDateTime.of(yearEffDate1, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDateTime effDate2 = LocalDateTime.of(yearEffDate2, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword1 = UserPassword.newInstance().withUser(user1).withEffDate(effDate1);

		UserPassword userPassword2 = UserPassword.newInstance().withUser(user2).withEffDate(effDate2);

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword1);
		passwords.add(userPassword2);

		assertEquals(1, passwords.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 1999, reginald, 2023" })
	void testDifferentAccountNamesAndEffDatesAreNotEqual(String accountName1, int yearEffDate1, String accountName2,
			int yearEffDate2) {

		LocalDateTime effDate1 = LocalDateTime.of(yearEffDate1, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDateTime effDate2 = LocalDateTime.of(yearEffDate2, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		UserPassword userPassword1 = UserPassword.newInstance().withUser(user1).withEffDate(effDate1);

		UserPassword userPassword2 = UserPassword.newInstance().withUser(user2).withEffDate(effDate2);

		Set<UserPassword> passwords = new HashSet<>();
		passwords.add(userPassword1);
		passwords.add(userPassword2);

		assertEquals(2, passwords.size());
	}

}
