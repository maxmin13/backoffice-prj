package it.maxmin.dao.jdbc.impl.repo;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ROLE_NAME_NOT_NULL_MSG;
import static org.springframework.util.Assert.notNull;

import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.maxmin.dao.jdbc.api.repo.RoleDao;
import it.maxmin.dao.jdbc.impl.operation.role.SelectRoleByName;
import it.maxmin.model.jdbc.dao.entity.Role;

@Transactional
@Repository("roleDao")
public class RoleDaoImpl implements RoleDao {
	
	private SelectRoleByName selectRoleByName;

	public RoleDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		super();
		this.selectRoleByName = new SelectRoleByName(jdbcTemplate);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Role> selectByRoleName(String name) {
		notNull(name, ERROR_ROLE_NAME_NOT_NULL_MSG);
		Role role = this.selectRoleByName.execute(name);
		return role != null ? Optional.of(role) : Optional.empty();
	}

}
