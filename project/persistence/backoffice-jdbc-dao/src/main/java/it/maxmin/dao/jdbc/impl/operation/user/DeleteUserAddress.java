package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.DELETE_USER_ADDRESS;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class DeleteUserAddress extends SqlUpdate {

	public DeleteUserAddress(DataSource dataSource) {
		super(dataSource, DELETE_USER_ADDRESS);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("addressId", Types.INTEGER));
	}

	public void execute(Long userId, Long addressId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		notNull(addressId, ERROR_ADDRESS_ID_NOT_NULL_MSG);
		updateByNamedParam(Map.of("userId", userId, "addressId", addressId));
	}
}
