package it.maxmin.plain.dao.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.model.plain.pojos.Address;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.model.plain.pojos.UserAddress;
import it.maxmin.plain.dao.impl.operation.address.InsertAddress;
import it.maxmin.plain.dao.impl.operation.address.InsertUserAddress;
import it.maxmin.plain.dao.impl.operation.address.SelectAddressesByUserId;

public class InsertUserWithAddress {

	private static Logger LOGGER = LoggerFactory.getLogger(InsertUserWithAddress.class);

	private InsertUser insertUser;
	private InsertUserAddress insertUserAddress;
	private InsertAddress insertAddress;
	private SelectAddressesByUserId selectAddressesByUserId;

	public InsertUserWithAddress(NamedParameterJdbcTemplate jdbcTemplate) {
		insertUser = new InsertUser(jdbcTemplate);
		insertAddress = new InsertAddress(jdbcTemplate);
		insertUserAddress = new InsertUserAddress(jdbcTemplate);
		selectAddressesByUserId = new SelectAddressesByUserId(jdbcTemplate);
	}

	public void execute(User user) {
		notNull(user, "The user must not be null");
		User newUser = this.insertUser.execute(user);
		var newUserId = Objects.requireNonNull(newUser.getUserId()).longValue();
		var addresses = user.getAddresses();
		if (addresses != null) {
			addresses.stream().forEach(address -> {
				Address newAddress = this.insertAddress.execute(address);
				this.insertUserAddress.execute(
						UserAddress.newInstance().withAddressId(newAddress.getAddressId()).withUserId(newUserId));
			});
		}
	}
}
