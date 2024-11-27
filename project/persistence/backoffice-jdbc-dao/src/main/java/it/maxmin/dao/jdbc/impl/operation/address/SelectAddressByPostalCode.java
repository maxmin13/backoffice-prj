package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.constant.MessageConstants.POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_POSTAL_CODE;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;

public class SelectAddressByPostalCode {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<Address>> resultSetExtractor;

	public SelectAddressByPostalCode(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectAddressHelper selectAddressHelper = new SelectAddressHelper(resultSetUserBuilder,
				resultSetAddressBuilder);
		resultSetExtractor = selectAddressHelper.getResultSetExtractor();
	}

	public Optional<Address> execute(String postalCode) {
		notNull(postalCode, POSTAL_CODE_NOT_NULL_MSG);
		SqlParameterSource param = new MapSqlParameterSource("postalCode", postalCode);
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_POSTAL_CODE, param, resultSetExtractor);
		return (addresses == null || addresses.isEmpty()) ? Optional.empty() : Optional.of(addresses.get(0));
	}
}
