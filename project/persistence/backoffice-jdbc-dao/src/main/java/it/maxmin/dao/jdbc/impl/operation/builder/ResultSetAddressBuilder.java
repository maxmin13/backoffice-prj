package it.maxmin.dao.jdbc.impl.operation.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import it.maxmin.dao.jdbc.exception.JdbcDaoException;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.State;

public class ResultSetAddressBuilder {

	public Optional<Address> buildAddress(ResultSet rs) {
		Address address = null;
		try {
			if (rs.getLong("AddressId") > 0) {
				address = Address.newInstance().withId(rs.getLong("AddressId"))
						.withDescription(rs.getString("Description")).withCity(rs.getString("City"))
						.withPostalCode(rs.getString("PostalCode")).withRegion(rs.getString("Region"))
						.withVersion(rs.getLong("Version"));
			}
			return Optional.ofNullable(address);
		} catch (SQLException e) {
			throw new JdbcDaoException(e.getMessage(), e);
		}
	}

	public Optional<State> buildState(ResultSet rs) {
		State state = null;
		try {
			if (rs.getLong("StateId") > 0) {
				state = State.newInstance().withId(rs.getLong("StateId")).withCode(rs.getString("Code"))
						.withName(rs.getString("StateName"));
			}
			return Optional.ofNullable(state);
		} catch (SQLException e) {
			throw new JdbcDaoException(e.getMessage(), e);
		}
	}
}
