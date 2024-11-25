package it.maxmin.dao.jdbc.impl.repo;

import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.RoleDao;
import it.maxmin.dao.jdbc.impl.operation.role.SelectRoleByRoleName;
import it.maxmin.model.jdbc.dao.entity.Role;

@Transactional
@Repository("roleDao")
public class RoleDaoImpl implements RoleDao {
	
	private SelectRoleByRoleName selectRoleByRoleName;

	public RoleDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.selectRoleByRoleName = new SelectRoleByRoleName(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Role> selectByRoleName(String roleName) {
		notNull(roleName, "The role name must not be null");
		Role role = this.selectRoleByRoleName.execute(roleName);
		return role != null ? Optional.of(role) : Optional.empty();
	}

}
