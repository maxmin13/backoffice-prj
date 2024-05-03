package com.maxmin.dao.music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface CoreDao {

	default Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mariadb://localhost:3306/musicdb?useSSL=false", "prospring6",
				"prospring6");
	}

	default void closeConnection(Connection connection) throws SQLException {
		if (connection == null) {
			return;
		}
		connection.close();
	}

}
