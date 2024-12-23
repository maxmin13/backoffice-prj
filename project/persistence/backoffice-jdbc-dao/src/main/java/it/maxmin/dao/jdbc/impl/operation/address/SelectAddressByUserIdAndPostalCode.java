package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;

public class SelectAddressByUserIdAndPostalCode {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<Address>> resultSetExtractor;

	public SelectAddressByUserIdAndPostalCode(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectAddressHelper selectAddressHelper = new SelectAddressHelper(resultSetUserBuilder,
				resultSetAddressBuilder);
		resultSetExtractor = selectAddressHelper.getResultSetExtractor();
	}

	public Optional<Address> execute(Long userId, String postalCode) {
		notNull(userId, ERROR_ID_NOT_NULL_MSG);
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("userId", userId);
		params.addValue("postalCode", postalCode);
		List<Address> addresses = jdbcTemplate.query(SELECT_ADDRESS_BY_USER_ID_AND_POSTAL_CODE, params,
				resultSetExtractor);
		return (addresses == null || addresses.isEmpty()) ? Optional.empty() : Optional.of(addresses.get(0));
	}
}
