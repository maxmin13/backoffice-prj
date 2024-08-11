package it.maxmin.plain.dao.api.repo;

import it.maxmin.model.plain.pojos.Address;

public interface AddressDao {

	Address create(Address address);

	void update(Address address);
}
