package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.INSERT_USER_ADDRESS;
import static org.springframework.util.Assert.notNull;

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

	public void execute(Long userId, Long addressId) {
		notNull(userId, "The user ID must not be null");
		notNull(addressId, "The address ID must not be null");
		updateByNamedParam(Map.of("userId", userId, "addressId", addressId));
	}
}
