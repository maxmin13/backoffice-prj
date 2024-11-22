package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_ROLE_BY_ROLE_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.dao.entity.Role;

public class SelectRoleByRoleName {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectRoleByRoleName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Role execute(String roleName) {

		notNull(roleName, "The role name must not be null");

		SqlParameterSource param = new MapSqlParameterSource("roleName", roleName);

		List<Role> roles = jdbcTemplate.query(SELECT_ROLE_BY_ROLE_NAME, param,
				BeanPropertyRowMapper.newInstance(Role.class));

		return roles.isEmpty() ? null : roles.get(0);
	}
}
