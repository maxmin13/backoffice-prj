package it.maxmin.dao.jpa.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jpa.dao.entity.User;

public interface UserDao {

	List<User> findAll();
	
	List<User> findAllWithAddressAndRole();

	Optional<User> findByAccountName(String accountName);

	void create(User user);

	User update(User user);
	
	void delete(User user);
}
