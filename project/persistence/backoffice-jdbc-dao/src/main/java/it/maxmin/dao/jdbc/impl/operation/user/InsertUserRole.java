package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.MessageConstants.ROLE_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.USER_ID_NOT_NULL_MSG;
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

	public void execute(Long userId, Long roleId) {
		notNull(userId, USER_ID_NOT_NULL_MSG);
		notNull(roleId, ROLE_ID_NOT_NULL_MSG);
		updateByNamedParam(Map.of("userId", userId, "roleId", roleId));
	}
}
