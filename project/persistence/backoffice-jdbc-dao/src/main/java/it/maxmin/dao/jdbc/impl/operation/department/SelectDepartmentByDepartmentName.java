package it.maxmin.dao.jdbc.impl.operation.department;

import static it.maxmin.dao.jdbc.constant.MessageConstants.DEPARTMENT_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_DEPARTMENT_BY_DEPARTMENT_NAME;
import static org.springframework.util.Assert.notNull;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import it.maxmin.model.jdbc.dao.entity.Department;

public class SelectDepartmentByDepartmentName {

	private NamedParameterJdbcTemplate jdbcTemplate;

	public SelectDepartmentByDepartmentName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Department execute(String name) {

		notNull(name, DEPARTMENT_NAME_NOT_NULL_MSG);

		SqlParameterSource param = new MapSqlParameterSource("name", name);

		List<Department> departments = jdbcTemplate.query(SELECT_DEPARTMENT_BY_DEPARTMENT_NAME, param,
				BeanPropertyRowMapper.newInstance(Department.class));

		return departments.isEmpty() ? null : departments.get(0);
	}
}
