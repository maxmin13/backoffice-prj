package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
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

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(Long userId, Long addressId) {
		notNull(userId, ERROR_ID_NOT_NULL_MSG);
		notNull(addressId, ERROR_ID_NOT_NULL_MSG);
		return updateByNamedParam(Map.of("userId", userId, "addressId", addressId));
	}
}
