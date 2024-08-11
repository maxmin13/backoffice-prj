package it.maxmin.plain.dao.impl.operation.address;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
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

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("addressId");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = insertAddress.executeAndReturnKeyHolder(paramSource);
		address.setAddressId(result.getKey().longValue());

		return address;
	}

	public void execute(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("addressId");
		insertAddress.executeBatch(SqlParameterSourceUtils.createBatch(addresses));
	}
}
