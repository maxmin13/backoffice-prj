package it.maxmin.dao.hibernate.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.hibernate.api.repo.UserDao;
import it.maxmin.domain.hibernate.entity.Address;
import it.maxmin.domain.hibernate.entity.User;

@Transactional
@Repository("userDao")
public class UserDaoImpl implements UserDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

	private SessionFactory sessionFactory;

	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional(readOnly = true)
	@Override
	public List<User> findAll() {
		return sessionFactory.getCurrentSession().createQuery("from User", User.class).list();
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<User> findAllWithAddressAndRole() {
		return null; // use createNameQuery, verify eagerly loaded
		
//		return sessionFactory.getCurrentSession()
//				.createNamedQuery("Address.findAllWithUsersByAccountName", Address.class)
//				.setParameter("accountName", accountName).list();
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<User> findByAccountName(String accountName) {
		notNull(accountName, "The account name must not be null");
		User user = sessionFactory.getCurrentSession().createNamedQuery("User.findByAccountName", User.class)
				.setParameter("accountName", accountName).uniqueResult();
		return Optional.ofNullable(user);
	}

	@Transactional
	@Override
	public User save(User user) {
		notNull(user, "The user must not be null");
		var session = sessionFactory.getCurrentSession();
		if (user.getId() == null) {
			session.persist(user);
		} else {
			session.merge(user);
		}
		LOGGER.info("User saved with id: {}", user.getId());
		return user;
	}

}
