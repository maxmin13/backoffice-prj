package it.maxmin.dao.jdbc.impl.operation.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.exception.DATAAccessException;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.Role;

public class SelectUserHelper {

	ResultSetExtractor<List<User>> getResultSetExtractor() {
		return (ResultSetExtractor<List<User>>) rs -> {
			Map<Long, User> map = new HashMap<>();
			User user = null;
			while (rs.next()) {
				var userId = rs.getLong("Id");
				user = map.computeIfAbsent(userId, id -> {
					try {
						return buildUser(rs);
					} catch (SQLException ex) {
						throw new DATAAccessException("Malformed data!", ex);
					}
				});

				var roleId = rs.getLong("RoleId");
				if (roleId > 0) {
					Objects.requireNonNull(user).addRole(buildRole(rs));
				}

				var addressId = rs.getLong("AddressId");
				if (addressId > 0) {
					Objects.requireNonNull(user).addAddress(buildAddress(rs));
				}
				var departmentId = rs.getLong("DepartmentId");
				if (departmentId > 0) { // if found
					Objects.requireNonNull(user).withDepartment(buildDepartment(rs));
				}
			}
			return new ArrayList<>(map.values());
		};
	}

	private User buildUser(ResultSet rs) throws SQLException {
		return User.newInstance().withId(rs.getLong("Id")).withAccountName(rs.getString("AccountName"))
				.withFirstName(rs.getString("FirstName")).withLastName(rs.getString("LastName"))
				.withBirthDate(rs.getDate("BirthDate").toLocalDate())
				.withCreatedAt(LocalDateTime.of(rs.getDate("CreatedAt").toLocalDate(), LocalTime.of(0, 0)));
	}

	private Role buildRole(ResultSet rs) throws SQLException {
		return Role.newInstance().withId(rs.getLong("RoleId")).withRoleName(rs.getString("RoleName"));
	}

	private Address buildAddress(ResultSet rs) throws SQLException {

		Address address = Address.newInstance().withId(rs.getLong("AddressId"))
				.withDescription(rs.getString("Description")).withCity(rs.getString("City"))
				.withRegion(rs.getString("Region")).withPostalCode(rs.getString("PostalCode"));

		var stateId = rs.getLong("StateId");
		if (stateId > 0) {
			Objects.requireNonNull(address).withState(State.newInstance().withId(stateId)
					.withName(rs.getString("StateName")).withCode(rs.getString("Code")));
		}

		return address;
	}

	private Department buildDepartment(ResultSet rs) throws SQLException {
		return Department.newInstance().withId(rs.getLong("DepartmentId")).withName(rs.getString("DepartmentName"));
	}
}
