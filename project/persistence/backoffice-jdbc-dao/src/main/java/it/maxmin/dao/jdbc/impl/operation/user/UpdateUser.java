package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.UPDATE_USER;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.SqlUpdate;

import it.maxmin.model.jdbc.domain.entity.User;

public class UpdateUser extends SqlUpdate {
	
	public UpdateUser(DataSource dataSource) {
		super(dataSource, UPDATE_USER);
		super.declareParameter(new SqlParameter("accountName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("firstName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("lastName", Types.VARCHAR));
		super.declareParameter(new SqlParameter("birthData", Types.DATE));
		super.declareParameter(new SqlParameter("userId", Types.INTEGER));
		
//		super.declareParameter(new SqlParameter("description", Types.VARCHAR));
//		super.declareParameter(new SqlParameter("city", Types.VARCHAR));
//		super.declareParameter(new SqlParameter("stateId", Types.INTEGER));
//		super.declareParameter(new SqlParameter("region", Types.VARCHAR));
//		super.declareParameter(new SqlParameter("postalCode", Types.VARCHAR));
//		super.declareParameter(new SqlParameter("addressId", Types.INTEGER));
	}

	public void execute(User user) {
		notNull(user, "The user must not be null");
		notNull(user.getId(), "User identifier must not be null");
		updateByNamedParam(Map.of("userId", user.getId(), "firstName", user.getFirstName(), "lastName",
				user.getLastName(), "birthData", user.getBirthDate(), "accountName", user.getAccountName()));
		
//		updateByNamedParam(Map.of("addressId", address.getId(), "description", address.getDescription(), "city",
//				address.getCity(), "stateId", address.getState().getId(), "region", address.getRegion(), "postalCode",
//				address.getPostalCode()));
	}
}
