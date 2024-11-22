package it.maxmin.dao.jpa.api.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.maxmin.model.jpa.dao.entity.Address;

public interface AddressDao {
	
	Optional<Address> findById(long addressId);
	
	Set<Address> findAll();

	Address create(Address address);
	
	Address update(Address address);

	void saveList(List<Address> addresses);

}
