package it.maxmin.dao.jdbc.api.repo;

import java.util.Optional;

import it.maxmin.model.jdbc.dao.entity.Role;

public interface RoleDao {

	Optional<Role> selectByRoleName(String name);
}
