package it.maxmin.service.jdbc.impl;

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

		notNull(userDto, "The user must not be null");
		notNull(userDto.getCredentials(), "The user credentials must not be null");
		notNull(userDto.getCredentials().getAccountName(), "The user account name must not be null");
		notNull(userDto.getCredentials().getFirstName(), "The user first name must not be null");
		notNull(userDto.getCredentials().getLastName(), "The user last name must not be null");
		notNull(userDto.getBirthDate(), "The user birthday must not be null");
		notNull(userDto.getDepartment().getName(), "The department name must not be null");

		Department department = departmentDao.selectByDepartmentName(userDto.getDepartment().getName())
				.orElseThrow(() -> new ServiceException("Error department not found"));

		User user = User.newInstance().withAccountName(userDto.getCredentials().getAccountName())
				.withFirstName(userDto.getCredentials().getFirstName())
				.withLastName(userDto.getCredentials().getLastName()).withBirthDate(userDto.getBirthDate())
				.withDepartment(Department.newInstance().withId(department.getId()));

		User newUser = userDao.insert(user);

		userDto.getAddresses().forEach(adt -> {
			Optional<Address> address = addressDao.selectAddressByPostalCode(adt.getPostalCode());
			address.ifPresentOrElse(
					a -> userDao.associateAddress(requireNonNull(newUser).getId(), a.getId()), 
					() -> {
						State state = stateDao.selectByStateName(adt.getState().getName())
								.orElseThrow(() -> new ServiceException("Error state not found"));
						Address ad = Address.newInstance().withCity(adt.getCity()).withDescription(adt.getDescription())
								.withPostalCode(adt.getPostalCode()).withRegion(adt.getRegion()).withState(state);
						Address newAddress = addressDao.insert(ad);
						userDao.associateAddress(newUser.getId(), requireNonNull(newAddress).getId());
			});
		});

		userDto.getRoles().stream().forEach(rdt -> {
			Role role = roleDao.selectByRoleName(rdt.getRoleName()).orElseThrow(() -> new ServiceException("Error role not found"));
			userDao.associateRole(requireNonNull(newUser).getId(), role.getId());
		});
	}

	void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
}
