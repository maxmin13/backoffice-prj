package it.maxmin.dao.jdbc.impl.operation.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import it.maxmin.dao.jdbc.impl.JdbcDaoException;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.User;

public class ResultSetUserBuilder {

	public Optional<User> buildUser(ResultSet rs) {
		User user = null;
		try {
			if (rs.getLong("UserId") > 0) {
				user = User.newInstance().withId(rs.getLong("UserId")).withAccountName(rs.getString("AccountName"))
						.withFirstName(rs.getString("FirstName")).withLastName(rs.getString("LastName"))
						.withBirthDate(rs.getDate("BirthDate").toLocalDate())
						.withCreatedAt(LocalDateTime.of(rs.getDate("CreatedAt").toLocalDate(), LocalTime.of(0, 0)));
			}
			return Optional.ofNullable(user);
		}
		catch (SQLException e) {
			throw new JdbcDaoException(e.getMessage(), e);
		}
	}

	public Optional<Role> buildRole(ResultSet rs) {
		Role role = null;
		try {
			if (rs.getLong("RoleId") > 0) {
				role = Role.newInstance().withId(rs.getLong("RoleId")).withName(rs.getString("Name"));
			}
			return Optional.ofNullable(role);
		}
		catch (SQLException e) {
			throw new JdbcDaoException(e.getMessage(), e);
		}
	}

	public Optional<Department> buildDepartment(ResultSet rs) {
		Department department = null;
		try {
			if (rs.getLong("DepartmentId") > 0) {
				department = Department.newInstance().withId(rs.getLong("DepartmentId"))
						.withName(rs.getString("DepartmentName"));
			}
			return Optional.ofNullable(department);
		}
		catch (SQLException e) {
			throw new JdbcDaoException(e.getMessage(), e);
		}
	}
}
