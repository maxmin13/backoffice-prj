package it.maxmin.dao.jdbc.impl.operation.state;

import static it.maxmin.dao.jdbc.constant.MessageConstants.STATE_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_STATE_BY_STATE_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.dao.entity.State;

public class SelectStateByStateName {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectStateByStateName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public State execute(String name) {

		notNull(name, STATE_NAME_NOT_NULL_MSG);

		SqlParameterSource param = new MapSqlParameterSource("name", name);

		List<State> states = jdbcTemplate.query(SELECT_STATE_BY_STATE_NAME, param,
				BeanPropertyRowMapper.newInstance(State.class));

		return states.isEmpty() ? null : states.get(0);
	}
}
