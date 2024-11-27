package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jpa.constant.JpaDaoMessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jpa.api.repo.UserDao;
import it.maxmin.model.jpa.dao.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

@Transactional
@Repository("userDao")
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return em.createQuery("from User", User.class).getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAllWithAddressAndRole() {
		return null; //TODO  use createNameQuery, verify eagerly loaded

	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> findByAccountName(String accountName) {
		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		try {
			return Optional.of(em.createNamedQuery("User.findByAccountName", User.class)
				.setParameter("accountName", accountName).getSingleResult());
		}
		catch(NoResultException e) {
			return Optional.empty();
		}
	}

	@Override
	public User create(User user) {	
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		if (user.getId() == null) {
			LOGGER.info("Inserting new user ...");
			em.persist(user);
			LOGGER.info("User created with id: {}", user.getId());
		} else {
			throw new IllegalArgumentException("The user ID must be null");
		}
		return user;
	}
	
	@Override
	public User update(User user) {	
		notNull(user, ERROR_USER_NOT_NULL_MSG);
		if (user.getId() == null) {
			throw new IllegalArgumentException("The user ID must not be null");
		} else {
			LOGGER.info("Updating new user ...");
			em.merge(user);
			LOGGER.info("User saved with id: {}", user.getId());
		}
		return user;
	}

}
