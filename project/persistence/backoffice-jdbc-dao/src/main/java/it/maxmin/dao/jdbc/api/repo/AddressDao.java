package it.maxmin.dao.jdbc.api.repo;

import java.util.List;

import it.maxmin.model.jdbc.domain.entity.Address;

public interface AddressDao {

	List<Address> findAddressesByUserId(Long userId);

	Address create(Address address);

	void create(List<Address> addresses);

	void update(Address address);
}
