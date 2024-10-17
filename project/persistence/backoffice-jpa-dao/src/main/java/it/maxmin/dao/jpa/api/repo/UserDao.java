package it.maxmin.dao.jpa.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.domain.jpa.entity.User;

public interface UserDao {

	List<User> findAll();
	
	List<User> findAllWithAddressAndRole();

	Optional<User> findByAccountName(String accountName);

	User save(User user);

}