package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ID_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.INSERT_USER;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import it.maxmin.model.jdbc.dao.entity.User;

public class InsertUser extends SqlUpdate {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUser.class);

	public InsertUser(DataSource dataSource) {
		super(dataSource, INSERT_USER);
		super.declareParameter(new SqlParameter("accountName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("departmentId", Types.INTEGER));
		super.declareParameter(new SqlParameter("birtDate", Types.DATE));
		super.setGeneratedKeysColumnNames("id");
		super.setReturnGeneratedKeys(true);
	}

	public User execute(User user) {
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		notNull(user.getDepartment(), ERROR_DEPARTMENT_NOT_NULL_MSG);
		if (user.getDepartment().getId() == null) {
			throw new IllegalArgumentException(ERROR_ID_NOT_NULL_MSG);
		}
		
		var keyHolder = new GeneratedKeyHolder();
		updateByNamedParam(Map.of("accountName", user.getAccountName(), "firstName", user.getFirstName(), "lastName",
				user.getLastName(), "departmentId", user.getDepartment().getId(), "birtDate", user.getBirthDate()), keyHolder);

		var userId = Objects.requireNonNull(keyHolder.getKey()).longValue();
		user.setId(userId);

		return user;
	}
}
