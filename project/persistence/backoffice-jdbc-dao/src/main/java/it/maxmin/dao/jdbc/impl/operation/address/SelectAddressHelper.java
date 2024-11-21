package it.maxmin.dao.jdbc.impl.operation.address;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.Department;
import it.maxmin.model.jdbc.domain.entity.Role;
import it.maxmin.model.jdbc.domain.entity.State;
import it.maxmin.model.jdbc.domain.entity.User;

public class SelectAddressHelper {

	private ResultSetUserBuilder resultSetUserBuilder;
	private ResultSetAddressBuilder resultSetAddressBuilder;

	public SelectAddressHelper(ResultSetUserBuilder resultSetUserBuilder,
			ResultSetAddressBuilder resultSetAddressBuilder) {
		this.resultSetUserBuilder = resultSetUserBuilder;
		this.resultSetAddressBuilder = resultSetAddressBuilder;
	}

	ResultSetExtractor<List<Address>> getResultSetExtractor() {
		return (ResultSetExtractor<List<Address>>) rs -> {
			Map<Long, Address> map = new HashMap<>();
			while (rs.next()) {
				Long addressId = rs.getLong("AddressId");
				Address address = map.computeIfAbsent(addressId,
						id -> resultSetAddressBuilder.buildAddress(rs).orElse(null));
				Optional<State> state = resultSetAddressBuilder.buildState(rs);
				state.ifPresent(requireNonNull(address)::withState);

				String accountName = rs.getString("accountName");
				requireNonNull(address).getUser(accountName).ifPresentOrElse(u -> {
					Optional<Role> role = resultSetUserBuilder.buildRole(rs);
					role.ifPresent(requireNonNull(u)::addRole);
				}, () -> {
					User user = resultSetUserBuilder.buildUser(rs).orElse(null);
					Optional<Department> department = resultSetUserBuilder.buildDepartment(rs);
					department.ifPresent(requireNonNull(user)::withDepartment);
					Optional<Role> role = resultSetUserBuilder.buildRole(rs);
					role.ifPresent(requireNonNull(user)::addRole);
					address.addUser(user);
				});
			}
			return new ArrayList<>(map.values());
		};
	}
}
