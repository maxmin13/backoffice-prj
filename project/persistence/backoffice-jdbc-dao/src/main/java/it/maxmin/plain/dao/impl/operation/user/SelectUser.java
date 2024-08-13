package it.maxmin.plain.dao.impl.operation.user;

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

import it.maxmin.model.plain.pojos.Address;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.plain.dao.exception.DataAccessException;

public abstract class SelectUser {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectUser.class);

	ResultSetExtractor<List<User>> usersExtractor = (ResultSetExtractor<List<User>>) rs ->  {
		Map<Long, User> map = new HashMap<>();
		User user = null;
		while (rs.next()) {
			var userId = rs.getLong("UserId");
			user = map.computeIfAbsent(userId, u -> {
				try {
					User us = User.newInstance().withUserId(rs.getLong("UserId"))
							.withAccountName(rs.getString("AccountName")).withFirstName(rs.getString("FirstName"))
							.withLastName(rs.getString("LastName"))
							.withBirthDate(rs.getDate("BirthDate").toLocalDate()).withCreatedDate(
									LocalDateTime.of(rs.getDate("CreatedDate").toLocalDate(), LocalTime.of(0, 0)));

					return us;
				}
				catch (SQLException ex) {
					LOGGER.error("Malformed data!", ex);
					throw new DataAccessException("Malformed data!", ex);
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
