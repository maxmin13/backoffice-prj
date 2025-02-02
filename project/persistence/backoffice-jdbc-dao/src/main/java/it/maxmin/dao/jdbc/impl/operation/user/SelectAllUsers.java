package it.maxmin.dao.jdbc.impl.operation.user;

import static it.maxmin.dao.jdbc.impl.operation.user.UserQueryConstants.SELECT_ALL_USERS;

import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetAddressBuilder;
import it.maxmin.dao.jdbc.impl.operation.builder.ResultSetUserBuilder;
import it.maxmin.model.jdbc.dao.entity.User;

public class SelectAllUsers {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private ResultSetExtractor<List<User>> resultSetExtractor;

	public SelectAllUsers(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		ResultSetUserBuilder resultSetUserBuilder = new ResultSetUserBuilder();
		ResultSetAddressBuilder resultSetAddressBuilder = new ResultSetAddressBuilder();
		SelectUserHelper helper = new SelectUserHelper(resultSetUserBuilder, resultSetAddressBuilder);
		this.resultSetExtractor = helper.getResultSetExtractor();
	}

	public List<User> execute() {
		return jdbcTemplate.getJdbcTemplate().query(SELECT_ALL_USERS, resultSetExtractor);
	}

}
