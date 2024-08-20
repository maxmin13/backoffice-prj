package it.maxmin.dao.jdbc.impl.operation.user;

import static org.springframework.util.Assert.notNull;

import java.util.Objects;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.model.jdbc.domain.entity.Address;
import it.maxmin.model.jdbc.domain.entity.User;

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
		var newUserId = Objects.requireNonNull(newUser.getId()).longValue();
		var addresses = user.getAddresses();
		if (addresses != null) {
			addresses.stream().forEach(address -> {
				Address newAddress = this.insertAddress.execute(address);
				this.insertUserAddress.execute(newUserId, newAddress.getId());
			});
		}
	}
}
