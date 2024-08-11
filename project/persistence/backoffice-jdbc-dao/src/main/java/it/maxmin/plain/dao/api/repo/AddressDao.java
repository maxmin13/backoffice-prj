package it.maxmin.plain.dao.api.repo;

import java.util.List;

import it.maxmin.model.plain.pojos.Address;

public interface AddressDao {

	Address create(Address address);

	List<Address> create(List<Address> addresses);

	void update(Address address);
}
