package it.maxmin.dao.jpa.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.model.jpa.dao.entity.User;

public interface UserDao {

	Optional<User> find(Long id);

	Optional<User> findByAccountName(String accountName);

	List<User> findByFirstName(String firstName);

	List<User> findAll();

	List<User> findAllWithAddressAndRole();

	void create(User user);

	User update(User user);

	void deleteByAccountName(String accountName);
}
