package it.maxmin.dao.jpa.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.sql.Date;
import java.sql.Timestamp;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.api.repo.AddressDao;
import it.maxmin.domain.jpa.entity.Address;
import it.maxmin.domain.jpa.entity.Department;
import it.maxmin.domain.jpa.entity.State;
import it.maxmin.domain.jpa.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;

@Transactional
@Repository("addressDao")
public class AddressDaoImpl implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(AddressDaoImpl.class);
	private static final String SELECT_ALL = ""
			+ "SELECT DISTINCT a.Id AS AddressId, a.PostalCode AS PostalCode, a.Description AS Description, a.City AS City, a.Region AS Region, "
			+ "          u.Id AS UserId, u.AccountName AS AccountName, u.FirstName AS FirstName, u.LastName AS LastName, u.BirthDate AS BirthDate, u.CreatedAt AS CreatedAt, "
			+ "          s.Id as StateId, s.Code AS StateCode, s.Name AS StateName, "
			+ "          d.Id as DepartmentId, d.Name AS DepartmentName " 
			+ "      FROM Address a "
			+ "      LEFT JOIN UserAddress ua ON a.Id = ua.AddressId " 
			+ "      LEFT JOIN User u ON ua.UserId = u.Id "
			+ "      LEFT JOIN Department d ON u.DepartmentId = d.Id "
			+ "      INNER JOIN State s ON a.StateId = s.Id ";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public Optional<Address> findById(long addressId) {
		try {
			return Optional.of(em.createNamedQuery("Address.findById", Address.class)
					.setParameter("id", addressId).getSingleResult());
		} catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Set<Address> findAll() {

		@SuppressWarnings("unchecked")
		Stream<Tuple> result = em.createNativeQuery(SELECT_ALL, Tuple.class)
				.getResultList().stream();
		
		Map<Long, Address> map = new HashMap<>();
		return result.map(tuple -> {

			var addressId = Long.valueOf(tuple.get("AddressId", Integer.class));
			var postalCode = tuple.get("PostalCode", String.class);
			var description = tuple.get("Description", String.class);
			var city = tuple.get("City", String.class);
			var region = tuple.get("Region", String.class);

			// returns the address if already in the map
			Address address = map.computeIfAbsent(addressId, id -> Address.newInstance().withId(id)
					.withPostalCode(postalCode).withDescription(description).withCity(city).withRegion(region));

			var stateId = Long.valueOf(tuple.get("StateId", Integer.class));
			var stateCode = tuple.get("StateCode", String.class);
			var stateName = tuple.get("stateName", String.class);
			var state = State.newInstance().withId(stateId).withCode(stateCode).withName(stateName);

			Objects.requireNonNull(address).withState(state);

			User user = null;
			var userId = tuple.get("UserId", Integer.class);

			if (userId != null) {
				var accountName = tuple.get("AccountName", String.class);
				var firstName = tuple.get("FirstName", String.class);
				var lastName = tuple.get("LastName", String.class);
				var birthDate = tuple.get("BirthDate", Date.class);
				var createdAt = tuple.get("CreatedAt", Timestamp.class);

				var departmentId = Long.valueOf(tuple.get("DepartmentId", Integer.class));
				var departmentName = tuple.get("DepartmentName", String.class);
				var department = Department.newInstance().withId(departmentId).withName(departmentName);

				user = User.newInstance().withId(Long.valueOf(userId)).withAccountName(accountName)
						.withFirstName(firstName).withLastName(lastName).withBirthDate(birthDate.toLocalDate())
						.withCreatedAt(createdAt.toLocalDateTime()).withDepartment(department);

				// add the user if not in the list
				Objects.requireNonNull(address).addUser(user);
			}

			return address;
		}).collect(Collectors.toSet());
	}

	@Override
	public void saveList(List<Address> addresses) {
		notNull(addresses, "The addresses must not be null");
		// this.insertAddresses.execute(addresses);
		LOGGER.info("New addresses inserted");
	}

	@Override
	public Address save(Address address) {
		notNull(address, "The address must not be null");
		if (address.getId() == null) {
			LOGGER.info("Inserting new address ...");
			em.persist(address);
		} else {
			LOGGER.info("Updating new address ...");
			em.merge(address);
		}
		LOGGER.info("Address saved with id: {}", address.getId());
		return address;
	}

}
