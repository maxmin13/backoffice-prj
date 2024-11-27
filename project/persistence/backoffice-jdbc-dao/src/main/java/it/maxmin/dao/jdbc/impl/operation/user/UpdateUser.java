package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.MessageConstants.DEPARTMENT_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.USER_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.MessageConstants.USER_NOT_NULL_MSG;
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
	}

	public void execute(User user) {
		notNull(user, USER_NOT_NULL_MSG);
		notNull(user.getId(), USER_ID_NOT_NULL_MSG);
		notNull(user.getDepartment(), DEPARTMENT_NOT_NULL_MSG);
		notNull(user.getDepartment().getId(), DEPARTMENT_ID_NOT_NULL_MSG);
		updateByNamedParam(Map.of("userId", user.getId(), "firstName", user.getFirstName(), "lastName",
				user.getLastName(), "birthData", user.getBirthDate(), "accountName", user.getAccountName(),
				"departmentId", user.getDepartment().getId()));
	}
}
