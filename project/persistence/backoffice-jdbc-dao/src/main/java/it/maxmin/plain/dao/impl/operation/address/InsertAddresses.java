package it.maxmin.plain.dao.impl.operation.address;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import it.maxmin.model.plain.pojos.Address;

public class InsertAddresses {

	private JdbcTemplate jdbcTemplate;

	public InsertAddresses(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate.getJdbcTemplate();
	}
	
	public void execute(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("addressId");
		insertAddress.executeBatch(SqlParameterSourceUtils.createBatch(addresses));
	}
}
