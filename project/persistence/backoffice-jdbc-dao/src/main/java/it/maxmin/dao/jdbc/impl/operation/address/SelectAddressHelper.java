package it.maxmin.dao.jdbc.impl.operation.address;

import static it.maxmin.common.constant.MessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
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

	public ResultSetExtractor<List<Address>> getResultSetExtractor() {
		return (ResultSetExtractor<List<Address>>) rs -> {
			Map<Long, Address> map = new HashMap<>();
			while (rs.next()) {
				Long addressId = rs.getLong("AddressId");
				Address address = map.computeIfAbsent(addressId, id -> resultSetAddressBuilder.buildAddress(rs)
						.orElseThrow(() -> new JdbcDaoException(ERROR_ADDRESS_NOT_NULL_MSG)));
				resultSetAddressBuilder.buildState(rs).ifPresent(requireNonNull(address)::withState);
				String accountName = rs.getString("accountName");
				if (accountName != null) {
					address.getUser(accountName).ifPresentOrElse(
							u -> resultSetUserBuilder.buildRole(rs).ifPresent(requireNonNull(u)::addRole), () -> {
								User user = resultSetUserBuilder.buildUser(rs)
										.orElseThrow(() -> new JdbcDaoException(ERROR_USER_NOT_NULL_MSG));
								resultSetUserBuilder.buildDepartment(rs)
										.ifPresent(requireNonNull(user)::withDepartment);
								resultSetUserBuilder.buildRole(rs).ifPresent(user::addRole);
								address.addUser(user);
							});
				}
			}
			return new ArrayList<>(map.values());
		};
	}
}
