package it.maxmin.dao.jdbc.impl.operation.user;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.exception.DATAAccessException;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;
import it.maxmin.model.jdbc.domain.entity.UserRole;

public abstract class SelectUser {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUser.class);

	ResultSetExtractor<List<User>> userExtractor = (ResultSetExtractor<List<User>>) rs ->  {
		Map<Long, User> map = new HashMap<>();
		User user = null;
		while (rs.next()) {
			var userId = rs.getLong("Id");
			user = map.computeIfAbsent(userId, id -> {
				try {
					return User.newInstance().withId(id)
							.withAccountName(rs.getString("AccountName"))
							.withFirstName(rs.getString("FirstName"))
							.withLastName(rs.getString("LastName"))
							.withBirthDate(rs.getDate("BirthDate").toLocalDate())
							.withCreatedAt(LocalDateTime.of(rs.getDate("CreatedAt").toLocalDate(), LocalTime.of(0, 0)));
				}
				catch (SQLException ex) {
					throw new DATAAccessException("Malformed data!", ex);
				}
			});
			
			// TODO test this with 2 roles
			var roleId = rs.getLong("RoleId");
			if (roleId > 0) { // if found
				UserRole role = UserRole.newInstance();
				// depends on UserRole hashCode() and equal()
				Objects.requireNonNull(user).addRole(role.withId(roleId).withRoleName(rs.getString("RoleName")));
			}
			
			//TODO test this with 2 addresses
			var addressId = rs.getLong("AddressId");
			if (addressId > 0) { // if found
				Address address = Address.newInstance();
				Objects.requireNonNull(user)
					// depends on Address hashCode() and equal()
					.addAddress(address.withId(addressId)
							.withDescription(rs.getString("Description"))
							.withCity(rs.getString("City"))
							.withRegion(rs.getString("Region"))
							.withPostalCode(rs.getString("PostalCode")));
					var stateId = rs.getLong("StateId");
					if (stateId > 0) { // if found
						Objects.requireNonNull(address)
								.withState(State.newInstance().withId(stateId)
										.withName(rs.getString("StateName")).withCode(rs.getString("Code")));
					}
			}
			var departmentId = rs.getLong("DepartmentId");
			if (departmentId > 0) { // if found
				Objects.requireNonNull(user)
						.withDepartment(Department.newInstance().withId(departmentId)
								.withName(rs.getString("DepartmentName")));
			}
		}
		return new ArrayList<>(map.values());
	};
}
