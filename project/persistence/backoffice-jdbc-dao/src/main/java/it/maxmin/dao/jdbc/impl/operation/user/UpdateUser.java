package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_DEPARTMENT_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_VERSION_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.UPDATE_USER;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.jdbc.dao.entity.User;

public class UpdateUser extends SqlUpdate {

	public UpdateUser(DataSource dataSource) {
		super(dataSource, UPDATE_USER);
		super.declareParameter(new SqlParameter("accountName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("birthData", Types.DATE));
		super.declareParameter(new SqlParameter("departmentId", Types.INTEGER));
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		super.declareParameter(new SqlParameter("version", Types.INTEGER));
	}

	/**
	 * @return the number of rows affected by the update
	 */
	public Integer execute(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		notNull(user.getId(), ERROR_USER_ID_NOT_NULL_MSG);
		notNull(user.getDepartment(), ERROR_DEPARTMENT_NOT_NULL_MSG);
		notNull(user.getDepartment().getId(), ERROR_DEPARTMENT_ID_NOT_NULL_MSG);
		notNull(user.getVersion(), ERROR_USER_VERSION_NOT_NULL_MSG);
		return updateByNamedParam(Map.of("userId", user.getId(), "firstName", user.getFirstName(), "lastName",
				user.getLastName(), "birthData", user.getBirthDate(), "accountName", user.getAccountName(),
				"departmentId", user.getDepartment().getId(), "version", user.getVersion()));
	}
}
