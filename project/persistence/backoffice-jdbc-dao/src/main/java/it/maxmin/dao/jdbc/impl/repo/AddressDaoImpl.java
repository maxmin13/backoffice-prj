package it.maxmin.dao.jdbc.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddresses;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressesByUserId;
import it.maxmin.dao.jdbc.impl.operation.address.UpdateAddress;
import it.maxmin.model.jdbc.Address;

@Repository
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);

	private UpdateAddress updateAddress;
	private InsertAddress insertAddress;
	private InsertAddresses insertAddresses;
	private SelectAddressesByUserId selectAddressesByUserId;

	@Autowired
	public void setJdbcTemplate(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateAddress = new UpdateAddress(dataSource);
		this.insertAddress = new InsertAddress(dataSource);
		this.insertAddresses = new InsertAddresses(dataSource);
		this.selectAddressesByUserId = new SelectAddressesByUserId(jdbcTemplate);
	}

	@Override
	public List<Address> findAddressesByUserId(Long userId) {
		notNull(userId, "The user ID must not be null");
		return this.selectAddressesByUserId.execute(userId);
	}

	@Override
	public Address create(Address address) {
		notNull(address, "The address must not be null");
		Address newAddress = this.insertAddress.execute(address);
		LOGGER.info("New address  {} inserted with id: {}", newAddress.getDescription(), newAddress.getId());
		return address;
	}

	@Override
	public void create(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");
		this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

	@Override
	public void update(Address address) {
		notNull(address, "The address must not be null");
		updateAddress.execute(address);
		LOGGER.info("Existing address updated with id: {}", address.getId());
	}

}
