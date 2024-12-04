package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.Address;

public interface AddressDao {

	Optional<Address> selectAddressByPostalCode(String postalCode);
	
	List<Address> selectAddressesByUserId(Long userId);

	Address insert(Address address);

	void insertList(List<Address> addresses);

	Integer update(Address address);
}
