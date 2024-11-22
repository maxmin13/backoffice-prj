package it.maxmin.dao.jdbc.impl.operation.user;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
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

				Optional<Department> department = resultSetUserBuilder.buildDepartment(rs);
				department.ifPresent(requireNonNull(user)::withDepartment);

				Optional<Role> role = resultSetUserBuilder.buildRole(rs);
				role.ifPresent(requireNonNull(user)::addRole);

				Optional<Address> address = resultSetAddressBuilder.buildAddress(rs);
				Optional<State> state = resultSetAddressBuilder.buildState(rs);
				address.ifPresent(a -> state.ifPresent(a::withState));
				address.ifPresent(requireNonNull(user)::addAddress);
			}
			return new ArrayList<>(map.values());
		};
	}
}
