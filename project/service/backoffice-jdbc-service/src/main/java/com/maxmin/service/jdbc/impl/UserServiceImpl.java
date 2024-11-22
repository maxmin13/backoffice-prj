package com.maxmin.service.jdbc.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maxmin.service.jdbc.api.UserService;

import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.model.jdbc.dao.entity.User;

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
	public void createUser(User user) {
		// TODO Auto-generated method stub
		
	}

}
