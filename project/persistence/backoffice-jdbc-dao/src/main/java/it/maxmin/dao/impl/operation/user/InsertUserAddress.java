package it.maxmin.dao.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import it.maxmin.model.plain.pojos.UserAddress;

public class InsertUserAddress {

	private DataSource dataSource;

	public InsertUserAddress(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void execute(UserAddress userAddress) {
		notNull(userAddress, "The user address must not be null");

		SimpleJdbcInsert insertUserAddress = new SimpleJdbcInsert(dataSource);
		insertUserAddress.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		insertUserAddress.execute(paramSource);
	}
}
