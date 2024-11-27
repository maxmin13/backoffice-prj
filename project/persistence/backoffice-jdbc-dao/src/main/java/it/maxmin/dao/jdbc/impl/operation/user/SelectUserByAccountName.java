package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.constant.JdbcDaoMessageConstants.ERROR_ACCOUNT_NAME_NOT_NULL_MSG;
import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_USER_BY_ACCOUNT_NAME;
import static org.springframework.util.Assert.notNull;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.User;

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

	public Optional<User> execute(String accountName) {

		notNull(accountName, ERROR_ACCOUNT_NAME_NOT_NULL_MSG);

		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("accountName", accountName, Types.VARCHAR);

		List<User> users = jdbcTemplate.query(SELECT_USER_BY_ACCOUNT_NAME, param, resultSetExtractor);

		return (users == null || users.isEmpty()) ? Optional.empty() : Optional.of(users.get(0));
	}
}
