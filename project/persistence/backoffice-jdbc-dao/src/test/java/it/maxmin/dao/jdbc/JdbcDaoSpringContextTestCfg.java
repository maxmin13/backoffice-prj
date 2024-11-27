package it.maxmin.dao.jdbc;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.dao.jdbc.config.JdbcDaoSpringContextCfg;

/**
 * Starts an embedded MariaDB database and overrides the datasource
 * */
@Configuration
@Import(JdbcDaoSpringContextCfg.class)
public class JdbcDaoSpringContextTestCfg {

	@Bean
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}

	@SuppressWarnings("unchecked")
	@Bean
	@Primary
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		} catch (ManagedProcessException e) {
			throw new JdbcDaoTestException("Error creating the data source", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		var dataSource = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new JdbcDaoTestException("Error loading DB driver", e);
		}
		dataSource.setDriverClass(driver);
		dataSource.setUrl("jdbc:mariadb://localhost:" + config.getPort() + "/testDB");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		return dataSource;
	}

	@Bean
	public JdbcQueryTestUtil jdbcQueryTestUtil(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
		return new JdbcQueryTestUtil(jdbcTemplate, dataSource);
	}

	@Bean
	public JdbcDataSourceTestUtil jdbcDataSourceTestUtil() {
		return new JdbcDataSourceTestUtil();
	}

	@Bean
	public JdbcUserTestUtil jdbcUserTestUtil() {
		return new JdbcUserTestUtil();
	}
}
