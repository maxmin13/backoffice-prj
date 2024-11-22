package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESSES_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;

public class SelectAddressesByUserId {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<Address>> resultSetExtractor;

	public SelectAddressesByUserId(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectAddressHelper selectAddressHelper = new SelectAddressHelper(resultSetUserBuilder,
				resultSetAddressBuilder);
		resultSetExtractor = selectAddressHelper.getResultSetExtractor();
	}

	public List<Address> execute(Long userId) {
		notNull(userId, "The user ID must not be null");
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, resultSetExtractor);
	}
}
