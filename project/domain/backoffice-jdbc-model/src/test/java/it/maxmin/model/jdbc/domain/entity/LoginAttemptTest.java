package it.maxmin.model.jdbc.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LoginAttemptTest {

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000" })
	void same_object_equal(String accountName, int yearCreatedAt) {

		LocalDateTime createdAt = LocalDateTime.of(yearCreatedAt, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user = User.newInstance().withId(1l).withAccountName(accountName).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt = LoginAttempt.newInstance().withUser(user).withSuccess(true)
				.withCreatedAt(createdAt);

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt);
		attempts.add(loginAttempt);

		assertEquals(1, attempts.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 2000, maxmin13, 2000" })
	void test_login_attempts_equal(String accountName1, int yearCreatedAt1, String accountName2, int yearCreatedAt2) {

		LocalDateTime createdAt1 = LocalDateTime.of(yearCreatedAt1, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDateTime createdAt2 = LocalDateTime.of(yearCreatedAt2, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt1 = LoginAttempt.newInstance().withUser(user1).withSuccess(true)
				.withCreatedAt(createdAt1);

		LoginAttempt loginAttempt2 = LoginAttempt.newInstance().withUser(user2).withSuccess(true)
				.withCreatedAt(createdAt2);

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt1);
		attempts.add(loginAttempt2);

		assertEquals(1, attempts.size());
	}

	@ParameterizedTest
	@CsvSource({ "maxmin13, 1999, reginald, 2023" })
	void test_login_attempts_not_equal(String accountName1, int yearCreatedAt1, String accountName2,
			int yearCreatedAt2) {

		LocalDateTime createdAt1 = LocalDateTime.of(yearCreatedAt1, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDateTime createdAt2 = LocalDateTime.of(yearCreatedAt2, Month.MARCH, 28, 14, 33, 48, 000000);

		LocalDate birthDate = LocalDate.of(1982, 9, 1);

		User user1 = User.newInstance().withId(1l).withAccountName(accountName1).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		User user2 = User.newInstance().withId(1l).withAccountName(accountName2).withBirthDate(birthDate)
				.withFirstName("Max").withLastName("Min").withDepartment(Department.newInstance().withId(1l));

		LoginAttempt loginAttempt1 = LoginAttempt.newInstance().withUser(user1).withSuccess(true)
				.withCreatedAt(createdAt1);

		LoginAttempt loginAttempt2 = LoginAttempt.newInstance().withUser(user2).withSuccess(true)
				.withCreatedAt(createdAt2);

		Set<LoginAttempt> attempts = new HashSet<>();
		attempts.add(loginAttempt1);
		attempts.add(loginAttempt2);

		assertEquals(2, attempts.size());
	}

}