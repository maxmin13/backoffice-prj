package it.maxmin.plain.dao.api;

import java.util.Set;

import it.maxmin.model.plain.pojos.User;

public interface UserDao {
	
	Set<User> findAll();
	
	User findByAccountName(String accountName);

	Set<User> findByFirstName(String firstName);

	User create(User user);

	void update(User user);

	void delete(Long userId);
}
