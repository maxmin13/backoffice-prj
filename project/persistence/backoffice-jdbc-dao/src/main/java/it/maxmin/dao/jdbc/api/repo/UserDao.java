package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.User;

public interface UserDao {

	List<User> findAll();

	Optional<User> findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	void create(User user);

	void associateAddress(Long userId, Long addressId);

	void associateRole(Long userId, Long roleId);

	void update(User user);
}
