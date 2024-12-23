package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.INSERT_USER_ROLE;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class InsertUserRole extends SqlUpdate {

	public InsertUserRole(DataSource dataSource) {
		super(dataSource, INSERT_USER_ROLE);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("roleId", Types.INTEGER));
	}

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(Long userId, Long roleId) {
		notNull(userId, ERROR_ID_NOT_NULL_MSG);
		notNull(roleId, ERROR_ID_NOT_NULL_MSG);
		return updateByNamedParam(Map.of("userId", userId, "roleId", roleId));
	}
}
