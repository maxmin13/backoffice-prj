package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.common.service.impl.MessageServiceImpl;
import it.maxmin.dao.jpa.api.repo.AddressDao;
import it.maxmin.model.jpa.dao.entity.Address;
import it.maxmin.model.jpa.dao.entity.State;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

@Transactional
@Repository("addressDao")
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);
//	private static final String SELECT_ALL = ""
//			+ "SELECT DISTINCT a.Id AS AddressId, a.PostalCode AS PostalCode, a.Description AS Description, a.City AS City, a.Region AS Region, "
//			+ "          u.Id AS UserId, u.AccountName AS AccountName, u.FirstName AS FirstName, u.LastName AS LastName, u.BirthDate AS BirthDate, "
//			+ "          s.Id as StateId, s.Code AS StateCode, s.Name AS StateName, "
//			+ "          d.Id as DepartmentId, d.Name AS DepartmentName "
//			+ "      FROM Address a "
//			+ "      LEFT JOIN UserAddress ua ON a.Id = ua.AddressId " 
//			+ "      LEFT JOIN User u ON ua.UserId = u.Id "
//			+ "      LEFT JOIN Department d ON u.DepartmentId = d.Id "
//			+ "      INNER JOIN State s ON a.StateId = s.Id ";
	
	private static final String SELECT_ALL = ""
			+ "SELECT DISTINCT a.Id AS AddressId, a.PostalCode AS PostalCode, a.Description AS Description, a.City AS City, a.Region AS Region, "
			+ "          s.Id as StateId, s.Code AS StateCode, s.Name AS StateName "
			+ "      FROM Address a "
			+ "      INNER JOIN State s ON a.StateId = s.Id ";

	@PersistenceContext
	private EntityManager em;
	private MessageServiceImpl messageService;

	@Autowired
	public AddressDaoImpl(MessageServiceImpl messageService) {
		this.messageService = messageService;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Address> find(Long id) {
		notNull(id, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "address id"));
		try {
			return Optional.of(em.createNamedQuery("Address.findById", Address.class).setParameter("id", id)
					.getSingleResult());
		}
		catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	// @returns a list of transient objects.
	@Transactional(readOnly = true)
	public Set<Address> findAll() {

		@SuppressWarnings("unchecked")
		Stream<Tuple> result = em.createNativeQuery(SELECT_ALL, Tuple.class).getResultList().stream();

		Map<Long, Address> map = new HashMap<>();
		return result.map(tuple -> {

			var addressId = Long.valueOf(tuple.get("AddressId", Integer.class));
			var postalCode = tuple.get("PostalCode", String.class);
			var description = tuple.get("Description", String.class);
			var city = tuple.get("City", String.class);
			var region = tuple.get("Region", String.class);

			// returns the address if already in the map
			Address address = map.computeIfAbsent(addressId, id -> Address.newInstance().withPostalCode(postalCode)
					.withDescription(description).withCity(city).withRegion(region));

			var stateCode = tuple.get("StateCode", String.class);
			var stateName = tuple.get("stateName", String.class);
			var state = State.newInstance().withCode(stateCode).withName(stateName);

			Objects.requireNonNull(address).withState(state);

//			User user = null;
//			var userId = tuple.get("UserId", Integer.class);

//			if (userId != null) {
//				var accountName = tuple.get("AccountName", String.class);
//				var firstName = tuple.get("FirstName", String.class);
//				var lastName = tuple.get("LastName", String.class);
//				var birthDate = tuple.get("BirthDate", Date.class);
//				var departmentName = tuple.get("DepartmentName", String.class);
//				var department = Department.newInstance().withName(departmentName);
//
//				user = User.newInstance().withAccountName(accountName).withFirstName(firstName).withLastName(lastName)
//						.withBirthDate(birthDate.toLocalDate()).withDepartment(department);
//
//				// add the user if not in the list
//				Objects.requireNonNull(address).addUser(user);
//			}

			return address;
		}).collect(Collectors.toSet());
	}

	@Override
	public void create(Address address) {
		notNull(address, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "address"));
		notNull(address.getState(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "state"));
		if (address.getId() == null) {
			LOGGER.info("Inserting new address ...");
			em.persist(address);
			LOGGER.info("User created with id: {}", address.getId());
		}
		else {
			throw new IllegalArgumentException(messageService.getMessage(ERROR_FIELD_NULL_MSG, "id"));
		}
	}

	@Override
	// @returns the managed persistent entity. The entity passed to the method as an
	// argument must be discarded.
	public Address update(Address address) {
		notNull(address, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "address"));
		notNull(address.getId(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "id"));
		notNull(address.getState(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "state"));

		LOGGER.info("Updating new address ...");
		Address a = em.merge(address);
		LOGGER.info("Address saved with id: {}", address.getId());
		return a;
	}

	@Override
	public void saveList(List<Address> addresses) {
		notNull(addresses, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "addresses"));
		// TODO IMPLEMENT BATCH INSERT
		// this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

}
