package it.maxmin.plain.dao.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.plain.dao.JndiTestSupport;

public class JndiDataSourceCfgTest extends JndiTestSupport {

	private static Logger LOGGER = LoggerFactory.getLogger(JndiDataSourceCfgTest.class);

	@Test
	public void testJndiDataSource() throws SQLException, IllegalStateException, NamingException {

		var springJdbcCtx = new AnnotationConfigApplicationContext(SpringJdbcTemplateCfg.class);
		var dataSource = springJdbcCtx.getBean("dataSource", DataSource.class);

		assertNotNull(dataSource);
		testDataSource(dataSource);

		springJdbcCtx.close();
	}

	private void testDataSource(DataSource dataSource) throws SQLException {
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
