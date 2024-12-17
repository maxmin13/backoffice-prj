package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.Address;

public interface AddressDao {

	Optional<Address> selectAddressByPostalCode(String postalCode);

	Optional<Address> selectAddressByUserIdAndPostalCode(Long userId, String postalCode);

	List<Address> selectAddressesByUserId(Long userId);

	Address insert(Address address);

	Integer insertList(List<Address> addresses);

	Integer update(Address address);
}
