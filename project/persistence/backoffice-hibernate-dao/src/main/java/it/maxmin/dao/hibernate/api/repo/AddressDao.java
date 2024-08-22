package it.maxmin.dao.hibernate.api.repo;

import java.util.List;

import it.maxmin.domain.hibernate.entity.Address;

public interface AddressDao {

	List<Address> findAddressesByUserId(Long userId);

	Address create(Address address);

	void create(List<Address> addresses);

	void update(Address address);
}
