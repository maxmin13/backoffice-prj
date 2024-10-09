package it.maxmin.dao.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import javax.sql.DataSource;

public class DataSourceTestUtil {

	public void testDataSource(DataSource dataSource) throws SQLException {
		try (var connection = dataSource.getConnection();
				var statement = connection.prepareStatement("SELECT 1");
				var resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				int mockVal = resultSet.getInt("1");
				assertEquals(1, mockVal);
			}
		}
	}
}
