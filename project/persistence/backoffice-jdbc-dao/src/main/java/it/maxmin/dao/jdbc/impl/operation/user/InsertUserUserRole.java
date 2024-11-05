package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.INSERT_USER_USER_ROLE;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

public class InsertUserUserRole extends SqlUpdate {

	public InsertUserUserRole(DataSource dataSource) {
		super(dataSource, INSERT_USER_USER_ROLE);
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("roleId", Types.INTEGER));
	}

	public void execute(Long userId, Long roleId) {
		notNull(userId, "The user ID must not be null");
		notNull(roleId, "The role ID must not be null");
		updateByNamedParam(Map.of("userId", userId, "roleId", roleId));
	}
}
