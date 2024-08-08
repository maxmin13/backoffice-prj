package it.maxmin.plain.dao.api;

import java.util.List;

import it.maxmin.model.plain.pojos.User;

public interface UserDao {
	
	List<User> findAll();
	
	String findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	User create(User user);

	void update(User user);

	void delete(Long userId);
}
