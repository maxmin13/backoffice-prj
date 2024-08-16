package it.maxmin.dao.jdbc.impl.operation.address;

import static org.springframework.util.Assert.notNull;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import it.maxmin.model.jdbc.Address;

public class InsertAddress {

	private static final Logger LOGGER = LoggerFactory.getLogger(InsertAddress.class);
	
	private DataSource dataSource;

	public InsertAddress(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Address execute(Address address) {
		notNull(address, "The address must not be null");

		SimpleJdbcInsert insertAddress = new SimpleJdbcInsert(dataSource);
		insertAddress.withTableName("Address").usingGeneratedKeyColumns("Id");
		BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(address);
		KeyHolder result = insertAddress.executeAndReturnKeyHolder(paramSource);
		Number keyValue = result.getKey();
		if (keyValue != null) {
			address.setId(keyValue.longValue());
		}
		else {
			LOGGER.warn("Address primary key not generated");
		}

		return address;
	}

}
