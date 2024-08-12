package it.maxmin.plain.dao.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import it.maxmin.model.plain.pojos.UserAddress;

public class InsertUserAddress {

	private JdbcTemplate jdbcTemplate;

	public InsertUserAddress(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate.getJdbcTemplate();
	}

	public void execute(UserAddress userAddress) {
		notNull(userAddress, "The user address must not be null");

		SimpleJdbcInsert insertUserAddress = new SimpleJdbcInsert(this.jdbcTemplate.getDataSource());
		insertUserAddress.withTableName("UserAddress");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(userAddress);
		insertUserAddress.execute(paramSource);
	}
}
