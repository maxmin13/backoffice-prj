package it.maxmin.dao.jdbc.impl.operation.user;

import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressByPostalCode;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.User;

public class InsertUserWithAddressAndRole {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserWithAddressAndRole.class);

	private InsertUser insertUser;
	private InsertAddress insertAddress;
	private InsertUserAddress insertUserAddress;
	private InsertUserUserRole insertUserUserRole;
	private SelectAddressByPostalCode selectAddressByPostalCode;
	private SelectUserRoleByRoleName selectUserRoleByRoleName;

	public InsertUserWithAddressAndRole(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		insertUser = new InsertUser(dataSource);
		insertAddress = new InsertAddress(dataSource);
		insertUserAddress = new InsertUserAddress(dataSource);
		insertUserUserRole = new InsertUserUserRole(dataSource);
		selectAddressByPostalCode = new SelectAddressByPostalCode(jdbcTemplate);
		selectUserRoleByRoleName = new SelectUserRoleByRoleName(jdbcTemplate);
	}

	public void execute(User user) {
		User newUser = this.insertUser.execute(user);
		var newUserId = Objects.requireNonNull(newUser.getId()).longValue();
		var addresses = user.getAddresses();
		if (addresses != null) {
			addresses.stream().forEach(address -> {
				if (selectAddressByPostalCode.execute(address.getPostalCode()) != null) {
					// associate the existing address with the user
					this.insertUserAddress.execute(newUserId, address.getId());
				} else {
					Address newAddress = this.insertAddress.execute(address);
					this.insertUserAddress.execute(newUserId, newAddress.getId());
				}
			});
		}
		var roles = user.getRoles();
		if (roles != null) {
			roles.stream().forEach(role -> {
				selectUserRoleByRoleName.execute(role.getRoleName());
				// associate the existing role with the user
				this.insertUserUserRole.execute(newUserId, role.getId());
			});
		}
	}
}
