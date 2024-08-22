package it.maxmin.dao.hibernate.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import it.maxmin.dao.hibernate.api.repo.AddressDao;
import it.maxmin.domain.hibernate.entity.Address;
import org.springframework.transaction.annotation.Transactional;

@Repository("addressDao")
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);

	private SessionFactory sessionFactory;

	public AddressDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Address> findAddressesByUserId(Long userId) {
		notNull(userId, "The user ID must not be null");
		return null;
	}

	@Override
	public Address create(Address address) {
		notNull(address, "The address must not be null");
		Address newAddress = null; //this.insertAddress.execute(address);
	//	LOGGER.info("New address  {} inserted with id: {}", newAddress.getDescription(), newAddress.getId());
		return address;
	}

	@Override
	public void create(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");
		//this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

	@Override
	public void update(Address address) {
		notNull(address, "The address must not be null");
		//updateAddress.execute(address);
		LOGGER.info("Existing address updated with id: {}", address.getId());
	}

}
