package it.maxmin.dao.hibernate.api.repo;

import java.util.List;
import java.util.Set;

import it.maxmin.domain.hibernate.entity.Address;

public interface AddressDao {
	
	Address findById(long addressId);

	List<Address> findAddressesByAccountName(String accountName);
	
	Set<Address> findAll();
	
	List<Address> findAllWithUsers();

	Address save(Address address);

	void saveList(List<Address> addresses);

}
