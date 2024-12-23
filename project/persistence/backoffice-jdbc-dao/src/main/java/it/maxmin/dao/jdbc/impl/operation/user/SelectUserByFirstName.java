package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.common.constant.MessageConstants.ERROR_FIRST_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USERS_BY_FIRST_NAME;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.User;
	
public class SelectUserByFirstName {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<User>> resultSetExtractor;

	public SelectUserByFirstName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectUserHelper helper = new SelectUserHelper(resultSetUserBuilder, resultSetAddressBuilder);
		this.resultSetExtractor = helper.getResultSetExtractor();
	}

	public List<User> execute(String firstName) {

		notNull(firstName, ERROR_FIRST_NAME_NOT_NULL_MSG);

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("firstName", firstName, Types.VARCHAR);

		return jdbcTemplate.query(SELECT_USERS_BY_FIRST_NAME, param, resultSetExtractor);
	}
}
