package it.maxmin.service.jdbc.impl;

import static it.maxmin.common.constant.MessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_BIRTH_DATE_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_CREDENTIALS_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_DEPARTMENT_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_ALREADY_CREATED;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.common.constant.MessageConstants.ERROR_USER_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.AddressDao;
import it.maxmin.dao.jdbc.api.repo.DepartmentDao;
import it.maxmin.dao.jdbc.api.repo.RoleDao;
import it.maxmin.dao.jdbc.api.repo.StateDao;
import it.maxmin.dao.jdbc.api.repo.UserDao;
import it.maxmin.model.jdbc.dao.entity.Address;
import it.maxmin.model.jdbc.dao.entity.Department;
import it.maxmin.model.jdbc.dao.entity.Role;
import it.maxmin.model.jdbc.dao.entity.State;
import it.maxmin.model.jdbc.dao.entity.User;
import it.maxmin.model.jdbc.service.dto.AddressDto;
import it.maxmin.model.jdbc.service.dto.RoleDto;
import it.maxmin.model.jdbc.service.dto.UserDto;
import it.maxmin.service.jdbc.api.UserService;
import it.maxmin.service.jdbc.exception.JdbcServiceException;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	private UserDao userDao;
	private AddressDao addressDao;
	private DepartmentDao departmentDao;
	private StateDao stateDao;
	private RoleDao roleDao;

	@Autowired
	public UserServiceImpl(UserDao userDao, AddressDao addressDao, DepartmentDao departmentDao, StateDao stateDao,
			RoleDao roleDao) {
		this.userDao = userDao;
		this.addressDao = addressDao;
		this.departmentDao = departmentDao;
		this.stateDao = stateDao;
		this.roleDao = roleDao;
	}

	@Override
	public void createUser(UserDto userDto) {
		LOGGER.info("Creating new user {}", userDto);

		notNull(userDto, ERROR_USER_NOT_NULL_MSG);
		notNull(userDto.getCredentials(), ERROR_CREDENTIALS_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getAccountName(), ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getFirstName(), ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getLastName(), ERROR_LAST_NAME_NOT_NULL_MSG);
		notNull(userDto.getBirthDate(), ERROR_BIRTH_DATE_NOT_NULL_MSG);
		notNull(userDto.getDepartment().getName(), ERROR_NAME_NOT_NULL_MSG);

		// check the user doesn't exist
		userDao.selectByAccountName(userDto.getCredentials().getAccountName()).ifPresent(user -> {
			throw new JdbcServiceException(ERROR_USER_ALREADY_CREATED);
		});

		Department department = departmentDao.selectByDepartmentName(userDto.getDepartment().getName())
				.orElseThrow(() -> new JdbcServiceException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		User user = User.newInstance().withAccountName(userDto.getCredentials().getAccountName())
				.withFirstName(userDto.getCredentials().getFirstName())
				.withLastName(userDto.getCredentials().getLastName()).withBirthDate(userDto.getBirthDate())
				.withDepartment(Department.newInstance().withId(department.getId()));

		User newUser = userDao.insert(user);

		handleAddresses(newUser.getId(), userDto.getAddresses());
		handleRoles(newUser.getId(), userDto.getRoles());
	}

	@Override
	public void updateUser(UserDto userDto) {
		LOGGER.info("Updating user {}", userDto);

		notNull(userDto, ERROR_USER_NOT_NULL_MSG);
		notNull(userDto.getCredentials(), ERROR_CREDENTIALS_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getAccountName(), ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getFirstName(), ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getLastName(), ERROR_LAST_NAME_NOT_NULL_MSG);
		notNull(userDto.getBirthDate(), ERROR_BIRTH_DATE_NOT_NULL_MSG);
		notNull(userDto.getDepartment(), ERROR_DEPARTMENT_NOT_NULL_MSG);
		notNull(userDto.getDepartment().getName(), ERROR_NAME_NOT_NULL_MSG);

		// check the user exists
		User user = userDao.selectByAccountName(userDto.getCredentials().getAccountName())
				.orElseThrow(() -> new JdbcServiceException(ERROR_USER_NOT_FOUND_MSG));
		Department department = departmentDao.selectByDepartmentName(userDto.getDepartment().getName())
				.orElseThrow(() -> new JdbcServiceException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		Long userId = user.getId();

		// prepare the user
		User updatedUser = User.newInstance().withId(userId).withVersion(user.getVersion())
				.withAccountName(userDto.getCredentials().getAccountName())
				.withFirstName(userDto.getCredentials().getFirstName())
				.withLastName(userDto.getCredentials().getLastName()).withBirthDate(userDto.getBirthDate())
				.withDepartment(department);

		userDao.update(updatedUser);

		handleAddresses(userId, userDto.getAddresses());
		handleRoles(userId, userDto.getRoles());
	}

	// if the address exists, update it and associate it with the user
	// if the address doesn't exist, create it and associate it with the user
	void handleAddresses(Long userId, Set<AddressDto> addresses) {

		userDao.removeAllAddresses(userId);

		addresses.forEach(addressDto -> {

			State state = stateDao.selectByStateName(addressDto.getState().getName())
					.orElseThrow(() -> new JdbcServiceException(ERROR_STATE_NOT_FOUND_MSG));
			Address address = Address.newInstance().withPostalCode(addressDto.getPostalCode())
					.withRegion(addressDto.getRegion()).withState(state).withCity(addressDto.getCity())
					.withDescription(addressDto.getDescription());

			addressDao.selectAddressByPostalCode(address.getPostalCode()).ifPresentOrElse(a -> {
				// existing address
				address.withId(a.getId());
				address.withVersion(a.getVersion());
				addressDao.update(address);
				userDao.associateAddress(userId, a.getId());
			}, () -> {
				// new address
				address.withVersion(0);
				Address newAddress = addressDao.insert(address);
				userDao.associateAddress(userId, newAddress.getId());
			});
		});
	}

	void handleRoles(Long userId, Set<RoleDto> roles) {
		userDao.removeAllRoles(userId);
		roles.stream().forEach(roleDto -> {
			Role role = roleDao.selectByRoleName(roleDto.getName())
					.orElseThrow(() -> new JdbcServiceException(ERROR_ROLE_NOT_FOUND_MSG));
			userDao.associateRole(userId, role.getId());
		});
	}

	void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

}
