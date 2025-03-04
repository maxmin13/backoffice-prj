package it.maxmin.dao.jdbc.impl.operation.role;

import static it.maxmin.common.constant.MessageConstants.ERROR_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_ROLE_BY_ROLE_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.dao.entity.Role;

public class SelectRoleByName {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectRoleByName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Role execute(String name) {

		notNull(name, ERROR_NAME_NOT_NULL_MSG);

		SqlParameterSource param = new MapSqlParameterSource("name", name);

		List<Role> roles = jdbcTemplate.query(SELECT_ROLE_BY_ROLE_NAME, param,
				BeanPropertyRowMapper.newInstance(Role.class));

		return roles.isEmpty() ? null : roles.get(0);
	}
}
