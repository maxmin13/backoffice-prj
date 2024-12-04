package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.User;

public interface UserDao {

	List<User> selectAll();

	Optional<User> selectByAccountName(String accountName);

	List<User> selectByFirstName(String firstName);

	User insert(User user);

	void associateAddress(Long userId, Long addressId);
	
	void removeAddress(Long userId, Long addressId);

	void associateRole(Long userId, Long roleId);
	
	void removeRole(Long userId, Long roleId);

	Integer update(User user);
}
