package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.address.AddressQueryConstants.INSERT_USER_ADDRESS;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class InsertUserAddress extends SqlUpdate {

	public InsertUserAddress(DataSource dataSource) {
		super(dataSource, INSERT_USER_ADDRESS);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("addressId", Types.INTEGER));
	}

	public void execute(long userId, long addressId) {
		updateByNamedParam(Map.of("userId", userId, "addressId", addressId));
	}
}
