package it.maxmin.dao.hibernate.api.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.maxmin.domain.hibernate.entity.Address;

public interface AddressDao {
	
	Optional<Address> findById(long addressId);
	
	Set<Address> findAll();

	Address save(Address address);

	void saveList(List<Address> addresses);

}
