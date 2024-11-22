package it.maxmin.dao.jdbc.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jdbc.domain.entity.User;

public interface UserDao {

	List<User> findAll();

	Optional<User> findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	void create(User user);

	void associate(Long userId, Long addressId);

	void update(User user);
}
