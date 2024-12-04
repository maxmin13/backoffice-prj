package it.maxmin.dao.jdbc.impl.operation.user;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.User;

public class SelectUserHelper {

	private ResultSetUserBuilder resultSetUserBuilder;
	private ResultSetAddressBuilder resultSetAddressBuilder;

	public SelectUserHelper(ResultSetUserBuilder resultSetUserBuilder,
			ResultSetAddressBuilder resultSetAddressBuilder) {
		this.resultSetUserBuilder = resultSetUserBuilder;
		this.resultSetAddressBuilder = resultSetAddressBuilder;
	}

	ResultSetExtractor<List<User>> getResultSetExtractor() {
		return (ResultSetExtractor<List<User>>) rs -> {
			Map<Long, User> map = new HashMap<>();
			while (rs.next()) {
				Long userId = rs.getLong("UserId");
				User user = map.computeIfAbsent(userId, id -> resultSetUserBuilder.buildUser(rs).orElse(null));

				resultSetUserBuilder.buildDepartment(rs).ifPresent(requireNonNull(user)::withDepartment);
				resultSetUserBuilder.buildRole(rs).ifPresent(user::addRole);
				resultSetAddressBuilder.buildAddress(rs).ifPresent(a -> {
					resultSetAddressBuilder.buildState(rs).ifPresent(a::withState);
					user.addAddress(a);
				});
			}
			return new ArrayList<>(map.values());
		};
	}
}
