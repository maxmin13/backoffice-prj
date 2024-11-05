package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USER_ROLE_BY_ROLE_NAME;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.domain.entity.UserRole;

public class SelectUserRoleByRoleName {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectUserRoleByRoleName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserRole execute(String name) {
		SqlParameterSource param = new MapSqlParameterSource("roleName", name);
		return jdbcTemplate.queryForObject(SELECT_USER_ROLE_BY_ROLE_NAME, param,
				BeanPropertyRowMapper.newInstance(UserRole.class));
	}
}
