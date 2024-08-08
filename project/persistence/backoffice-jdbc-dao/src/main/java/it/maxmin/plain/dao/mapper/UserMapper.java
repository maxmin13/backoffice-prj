package it.maxmin.plain.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.RowMapper;

import it.maxmin.model.plain.pojos.User;

public class UserMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUserId(rs.getLong("UserId"));
		user.setAccountName(rs.getString("AccountName"));
		user.setBirthDate(rs.getDate("BirthDate").toLocalDate());
		user.setCreatedDate((new Timestamp(rs.getDate("CreatedDate").getTime()).toLocalDateTime()));
		user.setFirstName(rs.getString("FirstName"));
		user.setLastName(rs.getString("LastName"));
		return user;
	}

}
