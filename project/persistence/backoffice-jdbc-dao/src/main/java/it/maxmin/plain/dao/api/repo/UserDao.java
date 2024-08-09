package it.maxmin.plain.dao.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.plain.pojos.User;

public interface UserDao {
	
	List<User> findAll();
	
	Optional<User> findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	User create(User user);

	void update(User user);
}
