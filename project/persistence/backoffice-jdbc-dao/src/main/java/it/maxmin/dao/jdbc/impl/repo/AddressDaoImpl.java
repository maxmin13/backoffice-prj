package it.maxmin.dao.jdbc.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddress;
import it.maxmin.dao.jdbc.impl.operation.address.InsertAddresses;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressByPostalCode;
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressesByUserId;
import it.maxmin.dao.jdbc.impl.operation.address.UpdateAddress;
import it.maxmin.model.jdbc.dao.entity.Address;

@Transactional
@Repository("addressDao")
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);

	private UpdateAddress updateAddress;
	private InsertAddress insertAddress;
	private InsertAddresses insertAddresses;
	private SelectAddressesByUserId selectAddressesByUserId;
	private SelectAddressByPostalCode selectAddressByPostalCode;

	@Autowired
	public AddressDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateAddress = new UpdateAddress(dataSource);
		this.insertAddress = new InsertAddress(dataSource);
		this.insertAddresses = new InsertAddresses(dataSource);
		this.selectAddressesByUserId = new SelectAddressesByUserId(jdbcTemplate);
		this.selectAddressByPostalCode = new SelectAddressByPostalCode(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Address> selectAddressesByUserId(Long userId) {
		notNull(userId, "The user ID must not be null");
		return this.selectAddressesByUserId.execute(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Address> selectAddressByPostalCode(String postalCode) {
		notNull(postalCode, "The user postal code must not be null");
		return Optional.ofNullable(this.selectAddressByPostalCode.execute(postalCode));
	}

	@Override
	public Address insert(Address address) {
		notNull(address, "The address must not be null");
		Address newAddress;
		if (address.getId() == null) {
			LOGGER.info("Inserting new address ...");
			newAddress = this.insertAddress.execute(address);
			LOGGER.info("New address  {} inserted with id: {}", newAddress.getDescription(), newAddress.getId());
		} else {
			throw new IllegalArgumentException("The address ID must be null");
		}
		return address;
	}

	@Override
	public void insertList(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");
		this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

	@Override
	public void update(Address address) {
		notNull(address, "The address must not be null");
		if (address.getId() == null) {
			throw new IllegalArgumentException("The address ID must not be null");
		} else {
			LOGGER.info("Updating new address ...");
			updateAddress.execute(address);
			LOGGER.info("Address saved with id: {}", address.getId());
		}
	}

}
