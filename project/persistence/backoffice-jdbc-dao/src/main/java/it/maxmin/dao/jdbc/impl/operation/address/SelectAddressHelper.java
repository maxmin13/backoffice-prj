package it.maxmin.dao.jdbc.impl.operation.address;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.User;

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
				resultSetAddressBuilder.buildState(rs).ifPresent(requireNonNull(address)::withState);
				String accountName = rs.getString("accountName");
				if (accountName != null) {
					address.getUser(accountName).ifPresentOrElse(u -> resultSetUserBuilder.buildRole(rs).ifPresent(requireNonNull(u)::addRole), 
							() -> {
						User user = resultSetUserBuilder.buildUser(rs).orElse(null);
						resultSetUserBuilder.buildDepartment(rs).ifPresent(requireNonNull(user)::withDepartment);
						resultSetUserBuilder.buildRole(rs).ifPresent(user::addRole);
						address.addUser(user);
					});
				}
			}
			return new ArrayList<>(map.values());
		};
	}
}
