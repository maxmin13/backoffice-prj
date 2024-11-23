package it.maxmin.service.jdbc.impl;

import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.service.dto.UserDto;
import it.maxmin.service.jdbc.api.UserService;
import it.maxmin.service.jdbc.exception.ServiceException;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDao userDao;
	private AddressDao addressDao;

	@Autowired
	public UserServiceImpl(UserDao userDao, AddressDao addressDao) {
		this.userDao = userDao;
		this.addressDao = addressDao;
	}

	@Override
	public void createUser(UserDto user) {
		LOGGER.info("Creating new user {}", user);
		
		notNull(user, "The user must not be null");
		notNull(user.getCredentials(), "The user credentials must not be null");
		notNull(user.getCredentials().getAccountName(), "The user account name must not be null");
		notNull(user.getCredentials().getFirstName(), "The user first name must not be null");
		notNull(user.getCredentials().getLastName(), "The user last name must not be null");

		userDao.insert(user.toEntity());
		User newUser = userDao.selectByAccountName(user.getCredentials().getAccountName())
				.orElseThrow(() -> new ServiceException("Error creating the user"));

		// If an address doesn't exist in the database, create it and associate it to
		// the new user;
		// if the address already exists, associate it to the user.
		user.toEntity().getAddresses().stream().forEach(a -> {
			Optional<Address> address = addressDao.selectAddressByPostalCode(a.getPostalCode());
			address.ifPresentOrElse(ad -> userDao.associateAddress(newUser.getId(), ad.getId()),
					() -> userDao.associateAddress(newUser.getId(), addressDao.insert(a).getId()));
		});

		// Associate the roles to the user
		user.toEntity().getRoles().stream().forEach(r -> userDao.associateRole(newUser.getId(), r.getId()));
	}

}
