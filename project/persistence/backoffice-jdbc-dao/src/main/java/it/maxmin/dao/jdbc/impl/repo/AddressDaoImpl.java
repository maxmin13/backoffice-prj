package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESSES_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_ID_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_ID_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ADDRESS_VERSION_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_POSTAL_CODE_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_USER_ID_NOT_NULL_MSG;
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
import it.maxmin.dao.jdbc.impl.operation.address.SelectAddressByUserIdAndPostalCode;
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
	private SelectAddressByUserIdAndPostalCode selectAddressByAccountNameAndPostalCode;

	@Autowired
	public AddressDaoImpl(DataSource dataSource, NamedParameterJdbcTemplate jdbcTemplate) {
		this.updateAddress = new UpdateAddress(dataSource);
		this.insertAddress = new InsertAddress(dataSource);
		this.insertAddresses = new InsertAddresses(dataSource);
		this.selectAddressesByUserId = new SelectAddressesByUserId(jdbcTemplate);
		this.selectAddressByPostalCode = new SelectAddressByPostalCode(jdbcTemplate);
		this.selectAddressByAccountNameAndPostalCode = new SelectAddressByUserIdAndPostalCode(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Address> selectAddressesByUserId(Long userId) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		return this.selectAddressesByUserId.execute(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Address> selectAddressByUserIdAndPostalCode(Long userId, String postalCode) {
		notNull(userId, ERROR_USER_ID_NOT_NULL_MSG);
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		return this.selectAddressByAccountNameAndPostalCode.execute(userId, postalCode);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Address> selectAddressByPostalCode(String postalCode) {
		notNull(postalCode, ERROR_POSTAL_CODE_NOT_NULL_MSG);
		return this.selectAddressByPostalCode.execute(postalCode);
	}

	@Override
	public Address insert(Address address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		if (address.getId() == null) {
			LOGGER.info("Inserting new address ...");
			Address newAddress = this.insertAddress.execute(address);
			LOGGER.info("New address  {} inserted with id: {}", newAddress.getDescription(), newAddress.getId());
			return newAddress;
		}
		else {
			throw new IllegalArgumentException(ERROR_ADDRESS_ID_NULL_MSG);
		}
	}

	@Override
	/**
	 * @return the number of rows affected by the update
	 */
	public Integer insertList(List<Address> addresses) {
		notNull(addresses, ERROR_ADDRESSES_NOT_NULL_MSG);
		Integer rows = this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
		return rows;
	}

	@Override
	/**
	 * @return the number of rows affected by the update
	 */
	public Integer update(Address address) {
		notNull(address, ERROR_ADDRESS_NOT_NULL_MSG);
		if (address.getVersion() == null) {
			throw new IllegalArgumentException(ERROR_ADDRESS_VERSION_NOT_NULL_MSG);
		}
		else if (address.getId() == null) {
			throw new IllegalArgumentException(ERROR_ADDRESS_ID_NOT_NULL_MSG);
		}
		else {
			LOGGER.info("Updating new address ...");
			return updateAddress.execute(address);
		}
	}

}
