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
import it.maxmin.model.jdbc.Address;
import it.maxmin.model.jdbc.User;

public abstract class SelectUser {
	
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUser.class);

	ResultSetExtractor<List<User>> usersExtractor = (ResultSetExtractor<List<User>>) rs ->  {
		Map<Long, User> map = new HashMap<>();
		User user = null;
		while (rs.next()) {
			var userId = rs.getLong("UserId");
			user = map.computeIfAbsent(userId, u -> {
				try {
					return User.newInstance().withUserId(rs.getLong("UserId"))
							.withAccountName(rs.getString("AccountName")).withFirstName(rs.getString("FirstName"))
							.withLastName(rs.getString("LastName"))
							.withBirthDate(rs.getDate("BirthDate").toLocalDate()).withCreatedDate(
									LocalDateTime.of(rs.getDate("CreatedDate").toLocalDate(), LocalTime.of(0, 0)));
				}
				catch (SQLException ex) {
					throw new DATAAccessException("Malformed data!", ex);
				}
			});
			var addressId = rs.getLong("AddressId");
			if (addressId > 0) { // if a record is found
				Objects.requireNonNull(user)
						.addAddress(Address.newInstance().withAddressId(rs.getLong("AddressId"))
								.withAddress(rs.getString("Address")).withCity(rs.getString("City"))
								.withStateId(rs.getLong("StateId")).withRegion(rs.getString("Region"))
								.withPostalCode(rs.getString("PostalCode")));
			}
		}
		return new ArrayList<>(map.values());
	};
}
