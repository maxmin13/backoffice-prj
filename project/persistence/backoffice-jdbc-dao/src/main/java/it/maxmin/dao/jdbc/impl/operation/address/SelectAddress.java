package it.maxmin.dao.jdbc.impl.operation.address;

import java.sql.SQLException;
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
import it.maxmin.model.jdbc.domain.entity.State;

public abstract class SelectAddress {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(SelectAddress.class);

	ResultSetExtractor<List<Address>> addressExtractor = (ResultSetExtractor<List<Address>>) rs -> {
		Map<Long, Address> map = new HashMap<>();
		Address address = null;
		while (rs.next()) {
			var addressId = rs.getLong("Id");
			address = map.computeIfAbsent(addressId, u -> {
				try {
					return Address.newInstance().withId(addressId).withDescription(rs.getString("Description"))
							.withCity(rs.getString("City")).withPostalCode(rs.getString("PostalCode"))
							.withRegion(rs.getString("Region"));
				}
				catch (SQLException ex) {
					throw new DATAAccessException("Malformed data!", ex);
				}
			});
			var stateId = rs.getLong("StateId");
			if (stateId > 0) { // if found
				Objects.requireNonNull(address).withState(State.newInstance().withId(stateId)
						.withCode(rs.getString("Code")).withName(rs.getString("Name")));
			}
		}
		return new ArrayList<>(map.values());
	};
}
