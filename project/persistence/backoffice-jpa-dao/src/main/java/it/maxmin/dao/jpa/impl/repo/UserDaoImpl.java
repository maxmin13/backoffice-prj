package it.maxmin.dao.jpa.impl.repo;

import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIELD_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.common.service.impl.MessageServiceImpl;
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
	private MessageServiceImpl messageService;

	@Autowired
	public UserDaoImpl(MessageServiceImpl messageService) {
		this.messageService = messageService;
	}

	@Override
	public Optional<User> find(Long id) {
		return Optional.ofNullable(em.find(User.class, id));
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return em.createQuery("from User", User.class).getResultList();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> findByAccountName(String accountName) {
		notNull(accountName, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "account name"));
		try {
			// Hibernate primary cache is flushed before execution.
			return Optional.of(em.createNamedQuery("User.findByAccountName", User.class)
					.setParameter("accountName", accountName).getSingleResult());
		}
		catch (NoResultException e) {
			return Optional.empty();
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findByFirstName(String firstName) {
		notNull(firstName, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "first name"));
		return em.createNamedQuery("User.findByFirstName", User.class).setParameter("firstName", firstName)
				.getResultList();
	}

	@Override
	public void refresh(User user) {
		notNull(user, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "user"));
		em.refresh(user);
	}

	@Override
	public void create(User user) {
		notNull(user, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "user"));
		notNull(user.getDepartment(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "department"));
		notNull(user.getAddresses(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "addresses"));
		notNull(user.getRoles(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "roles"));
		if (user.getId() == null) {
			LOGGER.info("inserting new user ...");
			em.persist(user);
			LOGGER.info("user created with ID: {}", user.getId());
		}
		else {
			throw new IllegalArgumentException(messageService.getMessage(ERROR_FIELD_NULL_MSG, "id"));
		}
	}

	@Override
	/**
	 * @returns the managed persistent entity. The entity passed to the method as an
	 *          argument must be discarded.
	 */
	public User update(User user) {
		notNull(user, messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "user"));
		notNull(user.getId(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "id"));
		notNull(user.getDepartment(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "department"));
		notNull(user.getAddresses(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "addresses"));
		notNull(user.getRoles(), messageService.getMessage(ERROR_FIELD_NOT_NULL_MSG, "roles"));

		LOGGER.info("Updating new user ...");
		User u = em.merge(user);
		LOGGER.info("user saved with ID: {}", user.getId());
		return u;
	}

	@Override
	public void delete(User user) {
		LOGGER.info("removing user {}", user);
		em.remove(user);
		LOGGER.info("user removed");
	}

}
