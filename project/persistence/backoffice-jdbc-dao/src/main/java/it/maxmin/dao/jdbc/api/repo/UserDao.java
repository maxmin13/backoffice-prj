package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.User;

public interface UserDao {

	List<User> selectAll();

	Optional<User> selectByAccountName(String accountName);

	List<User> selectByFirstName(String firstName);

	User insert(User user);

	Integer associateAddress(Long userId, Long addressId);
	
	Integer removeAddress(Long userId, Long addressId);
	
	Integer removeAllAddresses(Long userId);

	Integer associateRole(Long userId, Long roleId);
	
	Integer removeRole(Long userId, Long roleId);
	
	Integer removeAllRoles(Long userId);

	Integer update(User user);
}
