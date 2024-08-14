package it.maxmin.plain.dao.impl.operation.address;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import it.maxmin.model.plain.pojos.Address;

public class InsertAddresses {

	private DataSource dataSource;

	public InsertAddresses(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void execute(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(dataSource);
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("addressId");
		insertAddress.executeBatch(SqlParameterSourceUtils.createBatch(addresses));
	}
}
