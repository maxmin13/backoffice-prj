package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.DELETE_USER_ADDRESSES;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class DeleteUserAddresses extends SqlUpdate {

	public DeleteUserAddresses(DataSource dataSource) {
		super(dataSource, DELETE_USER_ADDRESSES);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
	}

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(Long userId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		return updateByNamedParam(Map.of("userId", userId));
	}
}