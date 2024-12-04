package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.DELETE_USER_ROLE;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class DeleteUserRole extends SqlUpdate {

	public DeleteUserRole(DataSource dataSource) {
		super(dataSource, DELETE_USER_ROLE);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("roleId", Types.INTEGER));
	}

	public void execute(Long userId, Long roleId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		notNull(roleId, ERROR_ROLE_ID_NOT_NULL_MSG);
		updateByNamedParam(Map.of("userId", userId, "roleId", roleId));
	}
}
