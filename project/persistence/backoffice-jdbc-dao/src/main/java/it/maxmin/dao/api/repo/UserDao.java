package it.maxmin.dao.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.plain.pojos.User;
import it.maxmin.model.plain.pojos.UserAddress;

public interface UserDao {

	List<User> findAll();

	Optional<User> findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	void create(User user);

	void associate(UserAddress userAddress);

	void update(User user);
}
