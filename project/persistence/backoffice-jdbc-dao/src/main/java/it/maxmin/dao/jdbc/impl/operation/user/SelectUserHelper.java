package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.exception.JdbcDaoException;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.User;public class SelectUserHelper {

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
				User user = map.computeIfAbsent(userId,
						id -> resultSetUserBuilder.buildUser(rs).orElseThrow(() -> new JdbcDaoException(ERROR_USER_NOT_NULL_MSG)));

				resultSetUserBuilder.buildDepartment(rs).ifPresent(requireNonNull(user)::withDepartment);
				resultSetUserBuilder.buildRole(rs).ifPresent(requireNonNull(user)::addRole);
				resultSetAddressBuilder.buildAddress(rs).ifPresent(a -> {
					resultSetAddressBuilder.buildState(rs).ifPresent(a::withState);
					requireNonNull(user).addAddress(a);
				});
			}
			return new ArrayList<>(map.values());
		};
	}
}
