package it.maxmin.dao.jdbc.impl.operation.address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.exception.DATAAccessException;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.State;

public class SelectAddressHelper {

	ResultSetExtractor<List<Address>> getResultSetExtractor() {
		return (ResultSetExtractor<List<Address>>) rs -> {
			Map<Long, Address> map = new HashMap<>();
			Address address = null;
			while (rs.next()) {
				var addressId = rs.getLong("Id");
				address = map.computeIfAbsent(addressId, i -> {
					try {
						return buildAddress(rs);
					} catch (SQLException ex) {
						throw new DATAAccessException("Malformed data!", ex);
					}
				});
				var stateId = rs.getLong("StateId");
				if (stateId > 0) {
					Objects.requireNonNull(address).withState(buildState(rs));
				}
			}
			return new ArrayList<>(map.values());
		};
	}

	private Address buildAddress(ResultSet rs) throws SQLException {
		return Address.newInstance().withId(rs.getLong("Id")).withDescription(rs.getString("Description"))
				.withCity(rs.getString("City")).withPostalCode(rs.getString("PostalCode"))
				.withRegion(rs.getString("Region"));
	}

	private State buildState(ResultSet rs) throws SQLException {
		return State.newInstance().withId(rs.getLong("StateId")).withCode(rs.getString("Code"))
				.withName(rs.getString("Name"));
	}
}
