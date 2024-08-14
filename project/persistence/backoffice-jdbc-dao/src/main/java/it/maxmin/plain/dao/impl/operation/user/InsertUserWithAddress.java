package it.maxmin.plain.dao.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.model.plain.pojos.Address;
import it.maxmin.model.plain.pojos.User;
import it.maxmin.model.plain.pojos.UserAddress;
import it.maxmin.plain.dao.impl.operation.address.InsertAddress;

public class InsertUserWithAddress {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(InsertUserWithAddress.class);

	private InsertUser insertUser;
	private InsertUserAddress insertUserAddress;
	private InsertAddress insertAddress;

	public InsertUserWithAddress(DataSource dataSource) {
		insertUser = new InsertUser(dataSource);
		insertAddress = new InsertAddress(dataSource);
		insertUserAddress = new InsertUserAddress(dataSource);
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
