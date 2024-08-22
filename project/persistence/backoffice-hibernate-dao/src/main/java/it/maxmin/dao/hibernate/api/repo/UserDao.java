package it.maxmin.dao.hibernate.api.repo;

import java.util.List;
import java.util.Optional;

import it.maxmin.domain.hibernate.entity.User;

public interface UserDao {

	List<User> findAll();

	Optional<User> findByAccountName(String accountName);

	User save(User user);

}
