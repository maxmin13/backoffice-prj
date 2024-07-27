package it.maxmin.plain.dao.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import it.maxmin.plain.dao.impl.UserDaoImpl;

public class DataSourceConfigTest {

	private static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfigTest.class);

	@BeforeEach
	public void setup() throws Exception {
		// creates a test directory service by:
		// loading jndi.properties file in the test/resource/ folder and creating a SimpleContextFactory JNDI context
		// populated with the objects described in the properties files in test/simple-jndi folder
		new InitialContext();
	}

	@Disabled("needs MariaDB running, set up container, comment this to run")
	@Test
	public void testSimpleDataSource() throws SQLException {
		var ctx = new AnnotationConfigApplicationContext(SimpleDataSourceCfg.class);
		var dataSource = ctx.getBean("dataSource", DataSource.class);
		assertNotNull(dataSource);
		testDataSource(dataSource);
		ctx.close();
	}

	@Test
	public void testJndiDataSource() throws SQLException, IllegalStateException, NamingException {

		var ctx = new AnnotationConfigApplicationContext(JndiDataSourceCfg.class);
		var dataSource = ctx.getBean("dataSource", DataSource.class);

		assertNotNull(dataSource);
		testDataSource(dataSource);
		ctx.close();
	}
	
	@Test
	public void testSpringDataSource() throws SQLException, IllegalStateException, NamingException {

		var ctx = new AnnotationConfigApplicationContext(SpringDatasourceCfg.class);
		var dataSource = ctx.getBean("dataSource", DataSource.class);

		assertNotNull(dataSource);
		testDataSource(dataSource);

		ctx.close();
	}
	
	@Test
	public void testUserDao() throws SQLException, IllegalStateException, NamingException {

		var ctx = new AnnotationConfigApplicationContext(SpringDatasourceCfg.class);
				
		var userDao = ctx.getBean("userDao", UserDaoImpl.class);
		assertNotNull(userDao);
		
		assertNotNull(userDao.getConnection());
		
		ctx.close();
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
