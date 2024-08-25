package it.maxmin.dao.hibernate.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.hibernate.api.repo.AddressDao;
import it.maxmin.domain.hibernate.entity.Address;
import it.maxmin.domain.hibernate.entity.State;
import it.maxmin.domain.hibernate.entity.User;
import jakarta.persistence.Tuple;

@Transactional
@Repository("addressDao")
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);
	private static final String SELECT_ALL = ""
			+ "SELECT DISTINCT a.Id AS AddressId, a.PostalCode AS PostalCode, u.Id AS UserId, u.AccountName AS AccountName, "
			+ "          s.Id as StateId, s.Code AS StateCode " 
			+ "      FROM Address a "
			+ "      INNER JOIN UserAddress ua ON a.Id = ua.AddressId " 
			+ "      INNER JOIN User u ON ua.UserId = u.Id "
			+ "      INNER JOIN State s ON a.StateId = s.Id ";

	private SessionFactory sessionFactory;

	public AddressDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Address findById(long addressId) {
		return sessionFactory.getCurrentSession().createNamedQuery("Address.findById", Address.class)
				.setParameter("id", addressId).uniqueResult();
	}

	@Transactional(readOnly = true)
	@Override
	public List<Address> findAddressesByAccountName(String accountName) {
		notNull(accountName, "The account name must not be null");
		return sessionFactory.getCurrentSession()
				.createNamedQuery("Address.findAllWithUsersByAccountName", Address.class)
				.setParameter("accountName", accountName).list();
	}

	@Override
	public Set<Address> findAll() {
		List<Tuple> result = sessionFactory.getCurrentSession().createNativeQuery(SELECT_ALL, Tuple.class)
				.getResultList();
		Map<Long, Address> map = new HashMap<>();
		return result.stream().map(tuple -> {
			var addressId = Long.valueOf(tuple.get("AddressId", Integer.class));

			Address address = map.computeIfAbsent(addressId,
					id -> Address.newInstance().withId(id).withPostalCode(tuple.get("PostalCode", String.class)));

			var stateId = Long.valueOf(tuple.get("StateId", Integer.class));
			var stateCode = tuple.get("StateCode", String.class);
			Objects.requireNonNull(address).withState(State.newInstance().withId(stateId).withCode(stateCode));

			var userId = Long.valueOf(tuple.get("UserId", Integer.class));
			var accountName = tuple.get("AccountName", String.class);
			Objects.requireNonNull(address).addUser(User.newInstance().withId(userId).withAccountName(accountName));

			return address;
		}).collect(Collectors.toSet());
	}

	@Override
	public List<Address> findAllWithUsers() {
		return sessionFactory.getCurrentSession()
				.createNamedQuery("Address.findAllWithUsersByAccountName", Address.class).list();
	}

	@Transactional(readOnly = true)
	@Override
	public void saveList(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");
		// this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

	@Transactional
	@Override
	public Address save(Address address) {
		notNull(address, "The address must not be null");
		var session = sessionFactory.getCurrentSession();
		if (address.getId() == null) {
			session.persist(address);
		} else {
			session.merge(address);
		}
		LOGGER.info("Address saved with id: {}", address.getId());
		return address;
	}

}
