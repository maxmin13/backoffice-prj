package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_USER_POSTAL_CODE;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.domain.entity.Address;

public class SelectAddressByPostalCode extends SelectAddressHelper {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<Address>> resultSetExtractor;

	public SelectAddressByPostalCode(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		SelectAddressHelper selectAddressHelper = new SelectAddressHelper();
		resultSetExtractor = selectAddressHelper.getResultSetExtractor();
	}

	public Address execute(String postalCode) {
		notNull(postalCode, "The postal code must not be null");
		Address address = null;
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_USER_POSTAL_CODE, param, resultSetExtractor);
		if (addresses != null && !addresses.isEmpty()) {
			address = addresses.get(0);
		}
		return address;
	}
}
