package it.maxmin.dao.jpa;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import it.maxmin.dao.jpa.config.JpaDataContextSpringCfg;

/**
 * Starts an embedded MariaDB database and creates a Spring context for the unit
 * tests.
 */

@Configuration
@Import(JpaDataContextSpringCfg.class)
public class UnitTestContextCfg {

	@Bean
	public MariaDB4jSpringService mariaDB4jSpringService() {
		return new MariaDB4jSpringService();
	}

	@SuppressWarnings("unchecked")
	@Bean
	public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService) {
		try {
			mariaDB4jSpringService.getDB().createDB("testDB");
		} catch (ManagedProcessException e) {
			throw new DaoTestException("Error creating the data source", e);
		}

		DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

		var ds = new SimpleDriverDataSource();
		Class<? extends Driver> driver;
		try {
			driver = (Class<? extends Driver>) Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DaoTestException("Error loading DB driver", e);
		}
		ds.setDriverClass(driver);
		ds.setUrl("jdbc:mariadb://localhost:" + config.getPort() + "/testDB");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;
	}

	@Bean
	public QueryTestUtil jdbcQueryTestUtil(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
		return new QueryTestUtil(jdbcTemplate, dataSource);
	}

	@Bean
	public DataSourceTestUtil dataSourceTestUtil() {
		return new DataSourceTestUtil();
	}

	@Bean
	public NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean
	public UserTestUtil jdbcUserTestUtil() {
		return new UserTestUtil();
	}

}
