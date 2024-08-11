package it.maxmin.plain.dao.impl.operation.address;

import static org.springframework.util.Assert.notNull;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import it.maxmin.model.plain.pojos.Address;

public class InsertAddress {

	private JdbcTemplate jdbcTemplate;

	public InsertAddress(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate.getJdbcTemplate();
	}

	public Address execute(Address address) {
		notNull(address, "The address must not be null");

		SimpleJdbcInsert insertUser = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertUser.withTableName("Address").usingGeneratedKeyColumns("addressId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = insertUser.executeAndReturnKeyHolder(paramSource);
		address.setAddressId(result.getKey().longValue());

		return address;
	}
}
