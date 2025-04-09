package it.maxmin.dao.jpa.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jpa.dao.entity.Address;

public interface AddressDao {
	
	Optional<Address> find(Long id);
	
	List<Address> findAll();

	void create(Address address);
	
	Address update(Address address);

}
