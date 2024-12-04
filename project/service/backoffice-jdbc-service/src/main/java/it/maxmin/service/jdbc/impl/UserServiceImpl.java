package it.maxmin.service.jdbc.impl;

import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_BIRTHDAY_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_DEPARTMENT_NAME_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_DEPARTMENT_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_LAST_NAME_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_ROLE_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_STALE_DATA_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_STATE_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_ALREADY_CREATED;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_CREDENTIALS_NOT_NULL_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_NOT_FOUND_MSG;
import static it.maxmin.service.jdbc.constant.JdbcServiceMessageConstants.ERROR_USER_NOT_NULL_MSG;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.notNull;

import java.util.Optional;

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
import it.maxmin.model.jdbc.service.dto.UserDto;
import it.maxmin.service.jdbc.api.UserService;
import it.maxmin.service.jdbc.exception.ServiceException;

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
		notNull(userDto.getCredentials(), ERROR_USER_CREDENTIALS_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getAccountName(), ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getFirstName(), ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getLastName(), ERROR_LAST_NAME_NOT_NULL_MSG);
		notNull(userDto.getBirthDate(), ERROR_BIRTHDAY_NOT_NULL_MSG);
		notNull(userDto.getDepartment().getName(), ERROR_DEPARTMENT_NAME_NOT_NULL_MSG);

		// check the user doesn't exist
		userDao.selectByAccountName(userDto.getCredentials().getAccountName()).ifPresent(user -> {
			throw new ServiceException(ERROR_USER_ALREADY_CREATED);
		});

		Department department = departmentDao.selectByDepartmentName(userDto.getDepartment().getName())
				.orElseThrow(() -> new ServiceException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		User user = User.newInstance().withAccountName(userDto.getCredentials().getAccountName())
				.withFirstName(userDto.getCredentials().getFirstName())
				.withLastName(userDto.getCredentials().getLastName()).withBirthDate(userDto.getBirthDate())
				.withDepartment(Department.newInstance().withId(department.getId()));

		User newUser = userDao.insert(user);

		userDto.getAddresses().forEach(adt -> {
			addressDao.selectAddressByPostalCode(adt.getPostalCode())
					.ifPresentOrElse(a -> userDao.associateAddress(requireNonNull(newUser).getId(), a.getId()), () -> {
						State state = stateDao.selectByStateName(adt.getState().getName())
								.orElseThrow(() -> new ServiceException(ERROR_STATE_NOT_FOUND_MSG));
						Address ad = Address.newInstance().withCity(adt.getCity()).withDescription(adt.getDescription())
								.withPostalCode(adt.getPostalCode()).withRegion(adt.getRegion()).withState(state);
						Address newAddress = addressDao.insert(ad);
						userDao.associateAddress(newUser.getId(), requireNonNull(newAddress).getId());
					});
		});

		userDto.getRoles().stream().forEach(r -> {
			Role role = roleDao.selectByRoleName(r.getName())
					.orElseThrow(() -> new ServiceException(ERROR_ROLE_NOT_FOUND_MSG));
			userDao.associateRole(requireNonNull(newUser).getId(), role.getId());
		});
	}

	@Override
	public void updateUser(UserDto userDto) {
		LOGGER.info("Updating user {}", userDto);

		notNull(userDto, ERROR_USER_NOT_NULL_MSG);
		notNull(userDto.getCredentials(), ERROR_USER_CREDENTIALS_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getAccountName(), ERROR_ACCOUNT_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getFirstName(), ERROR_FIRST_NAME_NOT_NULL_MSG);
		notNull(userDto.getCredentials().getLastName(), ERROR_LAST_NAME_NOT_NULL_MSG);
		notNull(userDto.getBirthDate(), ERROR_BIRTHDAY_NOT_NULL_MSG);
		notNull(userDto.getDepartment().getName(), ERROR_DEPARTMENT_NAME_NOT_NULL_MSG);

		// check the user exists
		User user = userDao.selectByAccountName(userDto.getCredentials().getAccountName())
				.orElseThrow(() -> new ServiceException(ERROR_USER_NOT_FOUND_MSG));

		Long userId = user.getId();
		Long userInitialVersion = user.getVersion();

		Department department = departmentDao.selectByDepartmentName(userDto.getDepartment().getName())
				.orElseThrow(() -> new ServiceException(ERROR_DEPARTMENT_NOT_FOUND_MSG));

		User updatedUser = User.newInstance().withId(userId).withVersion(userInitialVersion)
				.withAccountName(userDto.getCredentials().getAccountName())
				.withFirstName(userDto.getCredentials().getFirstName())
				.withLastName(userDto.getCredentials().getLastName()).withBirthDate(userDto.getBirthDate())
				.withDepartment(Department.newInstance().withId(department.getId()));

		Integer updateCount = userDao.update(updatedUser);

		if (updateCount != 1) {
			throw new ServiceException(ERROR_STALE_DATA_MSG);
		}

		// TODO CONTINUE WITH DEPARTMENT, ADDRESSES, ROLES ......

	}

	void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

}
