package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.SELECT_ADDRESSES_BY_USER_ID;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.domain.entity.Address;

public class SelectAddressesByUserId extends AddressResultsetExtractor {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectAddressesByUserId(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Address> execute(Long userId) {
		notNull(userId, "The user ID must not be null");
		SqlParameterSource param = new MapSqlParameterSource("userId", userId);
		return jdbcTemplate.query(SELECT_ADDRESSES_BY_USER_ID, param, addressExtractor);
	}
}
