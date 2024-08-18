package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.UPDATE_USER;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.jdbc.domain.entity.User;

public class UpdateUser extends SqlUpdate {

	@ParametersAreNonnullByDefault 
	public UpdateUser(DataSource dataSource) {
		super(dataSource, UPDATE_USER);
		super.declareParameter(new SqlParameter("accountName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("birthData", Types.DATE));
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
	}

	public void execute(User user) {
		notNull(user, "The user must not be null");
		updateByNamedParam(Map.of("accountName", user.getAccountName(), "firstName", user.getFirstName(), "lastName",
				user.getLastName(), "birthData", user.getBirthDate(), "userId", user.getId()));
	}
}
