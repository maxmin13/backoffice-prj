package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.domain.entity.User;

public class SelectUserByAccountName {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<User>> resultSetExtractor;

	public SelectUserByAccountName(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectUserHelper helper = new SelectUserHelper(resultSetUserBuilder, resultSetAddressBuilder);
		this.resultSetExtractor = helper.getResultSetExtractor();
	}

	public User execute(String accountName) {

		notNull(accountName, "The account name must not be null");

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("accountName", accountName, Types.VARCHAR);

		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, resultSetExtractor);

		return (users == null || users.isEmpty()) ? null : users.get(0);
	}
}
